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
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author 38164
 */
public class CuidadoVehicleOperations implements VehicleOperations{

    private Connection connection = DB.getInstance().getConnection();
    
    @Override
    public boolean insertVehicle(String regBr, int fuelType, BigDecimal fuelConsumption) {
        if (fuelType < 0 || fuelType > 2 || fuelConsumption.doubleValue() < 0.0) return false;
        final String sqlQuery = "INSERT INTO Vozilo (RegistarskiBroj, TipGoriva, Potrosnja) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, regBr);
            ps.setInt(2, fuelType);
            ps.setBigDecimal(3, fuelConsumption);
            return (ps.executeUpdate() == 1);
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoVehicleOperations.class.getName() + ", method -> insertVehicle");
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... regBrs) {
        final String sqlQuery = "DELETE FROM Vozilo WHERE RegistarskiBroj=?";
        int totalNum = 0;
        for(String regBr : regBrs) {
            try(PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
                ps.setString(1, regBr);
                totalNum += ps.executeUpdate();
            } catch (SQLException ex) {
                //Logger.getLogger(CuidadoVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception at -> " + CuidadoVehicleOperations.class.getName() + ", method -> deleteVehicles");
            }
        }
        return totalNum;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> resultStrings = new ArrayList<>();
        final String sqlQuery = "SELECT RegistarskiBroj FROM Vozilo";
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);){
            
            while(rs.next()) {
                resultStrings.add(rs.getString("RegistarskiBroj"));
            }
            
            return resultStrings;
        }
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoVehicleOperations.class.getName() + ", method -> getAllVehichles");
        }
        return null;
    }

    @Override
    public boolean changeFuelType(String regBr, int fuelType) {
        if(fuelType < 0 || fuelType > 2) return false;
        final String sqlQuery = "UPDATE Vozilo SET TipGoriva = ? WHERE RegistarskiBroj=?";
        try(PreparedStatement ps = connection.prepareCall(sqlQuery);) {
            ps.setInt(1, fuelType);
            ps.setString(2, regBr);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoVehicleOperations.class.getName() + ", method -> changeFuelType");
        }
        return false;
    }

    @Override
    public boolean changeConsumption(String regBr, BigDecimal consumption) {
        if(consumption.doubleValue() < 0.0) return false;
        final String sqlQuery = "UPDATE Vozilo SET Potrosnja = ? WHERE RegistarskiBroj=?";
        try(PreparedStatement ps = connection.prepareCall(sqlQuery);) {
            ps.setBigDecimal(1, consumption);
            ps.setString(2, regBr);
            return ps.executeUpdate() == 1;
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoVehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoVehicleOperations.class.getName() + ", method -> changeConsumption");
        }
        return false;
    }
    
}
