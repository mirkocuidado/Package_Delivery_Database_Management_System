/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabprojekat;

import database.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author 38164
 */
public class CuidadoUserOperations implements UserOperations{

    private Connection connection = DB.getInstance().getConnection();
    
    @Override
    public boolean insertUser(String username, String first, String last, String password) {
        if(!(first.charAt(0) > 'A' && first.charAt(0) < 'Z')) {
            System.out.println("First name does not start with a capital letter!");
            return false;
        }
        if(!(last.charAt(0) > 'A' && last.charAt(0) < 'Z')) {
            System.out.println("Last name does not start with a capital letter!");
            return false;
        }
        if(password.length() < 8) {
            System.out.println("Password must containt at least 8 letters!");
            return false;
        }
        
        boolean hasChar = false;
        boolean hasNumber = false;
        for(int i = 0; i < password.length(); ++i) {
            char c = password.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) hasChar = true;
            else if (c >= '0' && c <= '9') hasNumber = true;
        }
        if(!hasChar) {
            System.out.println("Password must containt at least one letter!");
            return false;
        }
        if(!hasNumber) {
            System.out.println("Password must containt at least one number!");
            return false;
        }
        
        final String sqlQuery = "INSERT INTO Korisnik (KorisnickoIme, Ime, Prezime, Sifra) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, username);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, password);
            return (ps.executeUpdate() == 1);
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> insertUser");
        }
        return false;
    }

    @Override
    public int declareAdmin(String username) {
        /*** PROVERI DA LI POSTOJI TAKAV KORISNIK ***/
        String sqlQuery = "SELECT * FROM Korisnik WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() == false) return 2;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> declareAdmin");
            return -1;
        }
        /*** PROVERI DA LI JE ADMIN ***/
        sqlQuery = "SELECT * FROM Administrator WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return 1;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> declareAdmin");
            return -1;
        }
        /*** DODAJ DA JE ADMIN ***/
        sqlQuery = "INSERT INTO Administrator (KorisnickoIme) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, username);
            return (ps.executeUpdate() == 1) ? 0 : -1;
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> declareAdmin");
            return -1;
        }
    }

    @Override
    public Integer getSentPackages(String... usernames) {
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(usernames));
        
        boolean anyUser = false;
        int retValue = 0;
        final String sqlQuery = "SELECT BrojPoslatihPaketa FROM Korisnik WHERE KorisnickoIme=?";
        
        for (String username : set) {
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        anyUser = true;
                        retValue += rs.getInt("BrojPoslatihPaketa");
                    }
                }
            } catch (SQLException ex) {
                //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> getSentPackages");
            }
        }
        return (anyUser == true) ? retValue : null;
    }

    @Override
    public int deleteUsers(String... usernames) {
        final String sqlQuery = "DELETE FROM Korisnik WHERE KorisnickoIme=?";
        int retValue = 0;
        for (String username : usernames) {
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(1, username);
                retValue += ps.executeUpdate();
            } 
            catch (SQLException ex) {
                //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> deleteUsers");
            }
        }
        return retValue;
    }

    @Override
    public List<String> getAllUsers() {
        final String sqlQuery = "SELECT KorisnickoIme FROM Korisnik";
        List<String> usernames = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery)) {
            
            while (rs.next()) {
                String user = rs.getString("KorisnickoIme");
                usernames.add(user);
            }
            
            return usernames;
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoUserOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoUserOperations.class.getName() + ", method -> getAllUsers");
        }
        return null;
    }
    
}
