/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabprojekat;

import database.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author 38164
 */
public class CuidadoCourierOperations implements CourierOperations{

    private Connection connection = DB.getInstance().getConnection();
     
    @Override
    public boolean insertCourier(String username, String regBr) {
        String sqlQuery = "SELECT * FROM Kurir WHERE RegistarskiBroj = ?";
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery);){
            ps.setString(1, regBr);
            try(ResultSet rs = ps.executeQuery();) {
                if(rs.next()) return false;
                
                sqlQuery = "INSERT INTO Kurir (KorisnickoIme, BrojPaketa, Profit, Status, RegistarskiBroj) VALUES (?, 0, 0, 0, ?)";
		try (PreparedStatement ps2 = connection.prepareStatement(sqlQuery);) {
                    ps2.setString(1, username);
                    ps2.setString(2, regBr);
                    return (ps2.executeUpdate() == 1);
		} 
                catch (SQLException e) {
                    //Logger.getLogger(CuidadoCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Exception at -> " + CuidadoCourierOperations.class.getName() + ", method -> insertCourier");
		}
            }
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierOperations.class.getName() + ", method -> insertCourier");
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String username) {
        final String sqlQuery = "DELETE FROM Kurir WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.setString(1, username);
            return (ps.executeUpdate() == 1);
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierOperations.class.getName() + ", method -> deleteCourier");
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        List<String> resultList = new ArrayList<>();
	final String sqlQuery = "SELECT KorisnickoIme FROM Kurir WHERE Status=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultList.add(rs.getString("KorisnickoIme"));
                }
            }
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierOperations.class.getName() + ", method -> deleteCourierWithStatus");
	}
	return resultList;
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> listResult = new ArrayList<>();
	final String sqlQuery = "SELECT KorisnickoIme FROM Kurir";
	try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
		listResult.add(rs.getString("KorisnickoIme"));
            }
            return listResult;
	} catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierOperations.class.getName() + ", method -> getAllCouriers");
	}
	return null;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int moreThan) {
        final String sqlQuery = "SELECT AVG(Profit) FROM Kurir WHERE BrojPaketa >= ?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, moreThan);
            try (ResultSet rs = ps.executeQuery()) {
		if (rs.next())
                    return rs.getBigDecimal(1);
		else
                    return new BigDecimal(0);
            }
	} 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoCourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCourierOperations.class.getName() + ", method -> getAverageCourierProfit");
	}
        return new BigDecimal(0);
    }
    
}
