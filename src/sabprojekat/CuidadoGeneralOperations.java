/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabprojekat;

import database.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author 38164
 */
public class CuidadoGeneralOperations implements GeneralOperations {

    private Connection connection = DB.getInstance().getConnection();
	
    private void eraseAllCities() {
        String sqlQuery = "DELETE FROM Grad";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllUsers() {
        String sqlQuery = "DELETE FROM Korisnik";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        }  
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllCouriers() {
        String sqlQuery = "DELETE FROM Kurir";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllAdmins() {
        String sqlQuery = "DELETE FROM Administrator";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllRequests() {
        String sqlQuery = "DELETE FROM Zahtev";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllOpstinas() {
        String sqlQuery = "DELETE FROM Opstina";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllPackages() {
        String sqlQuery = "DELETE FROM Paket";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllOffers() {
        String sqlQuery = "DELETE FROM Ponuda";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
	
    private void eraseAllVehicles() {
        String sqlQuery = "DELETE FROM Vozilo";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        } 
        catch (SQLException e) {
            return;
        }
    }
    
    private void eraseAllDrives() {
        String sqlQuery = "DELETE FROM Voznja";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.executeUpdate();
        }  
        catch (SQLException e) {
            return;
        }
    }
	
    @Override
    public void eraseAll() {
        eraseAllRequests();
        eraseAllOffers();
        eraseAllDrives();
        eraseAllPackages();
        eraseAllOpstinas();
        eraseAllCities();
        eraseAllVehicles();
        eraseAllCouriers();
        eraseAllAdmins();
        eraseAllUsers();
    }
    
}
