/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabprojekat;

import database.DB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author 38164
 */
public class CuidadoCourierRequestOperations implements CourierRequestOperation {

    private Connection connection = DB.getInstance().getConnection();
    
    @Override
    public boolean insertCourierRequest(String username, String regBr) {
        final String sqlQuery = "INSERT INTO Zahtev (KorisnickoIme, RegistarskiBroj) VALUES (?, ?)";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, username);
            ps.setString(2, regBr);
            return (ps.executeUpdate() == 1);
	} catch (SQLException ex) {
            Logger.getLogger(CuidadoCourierRequestOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierRequestOperations.class.getName() + ", method -> insertCourierRequest");
	}
        return false;
    }

    @Override
    public boolean deleteCourierRequest(String username) {
        final String sqlQuery = "{call spDeleteRequestsForCourierWithGivenUsername(?)}";
	try (CallableStatement cs = connection.prepareCall(sqlQuery);) {
            cs.setString(1, username);
            cs.execute();
            return (cs.getUpdateCount() > 0);
        }
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierRequestOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierRequestOperations.class.getName() + ", method -> deleteCourierRequest");
        }
        return false;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String regBr, String username) {
        final String sqlQuery = "UPDATE Zahtev SET RegistarskiBroj=? WHERE KorisnickoIme=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.setString(1, regBr);
            ps.setString(2, username);
            return (ps.executeUpdate() == 1);
	} catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierRequestOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierRequestOperations.class.getName() + ", method -> changeVehicleInCourierRequest");
	}
        return false;
    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> resultList = new ArrayList<>();
	final String sqlQuery = "SELECT KorisnickoIme FROM Zahtev";
	try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);) {
            while (rs.next()) {
		resultList.add(rs.getString("KorisnickoIme"));
            }
            return resultList;
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierRequestOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierRequestOperations.class.getName() + ", method -> getAllCourierRequests");
	}
        return null;
    }

    @Override
    public boolean grantRequest(String username) {
        final String sqlQuery = "{call spAdminAcceptedUsersRequestForBecomingCourier(?)}";
	try (CallableStatement cs = connection.prepareCall(sqlQuery)) {
            cs.setString(1, username);
            cs.execute();
            return (cs.getUpdateCount() > 0);
        } catch (SQLException ex) {
            Logger.getLogger(CuidadoCourierRequestOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierRequestOperations.class.getName());
        }
        return false;
    }
    
}
