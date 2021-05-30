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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author 38164
 */
public class CuidadoDistrictOperations implements DistrictOperations{

    private Connection connection = DB.getInstance().getConnection();
    
    @Override
    public int insertDistrict(String name, int cityID, int xCoord, int yCoord) {
        final String sqlQuery = "INSERT INTO Opstina (Naziv, XKoordinata, YKoordinata, IdGrad) VALUES (?,?,?,?)";
        int resultNum = 0;
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, name);
            ps.setInt(2, xCoord);
            ps.setInt(3, yCoord);
            ps.setInt(4, cityID);
            resultNum = ps.executeUpdate();
            
            if(resultNum == 0) return -1;
            
            try(ResultSet rs = ps.getGeneratedKeys();) {
                if (rs.next()) return rs.getInt(1);
                else return -1;
            }
        } 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> insertDistrict");
        }
        return -1;
    }

    @Override
    public int deleteDistricts(String... districts) {
        final String sqlQuery = "DELETE FROM Opstina WHERE Naziv=?";
        int numResult = 0;
        for (String name : districts) {
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                ps.setString(1, name);
                numResult += ps.executeUpdate();
            } 
            catch (SQLException ex) {
                //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> deleteDistricts");
            }
        }
        return numResult;
    }

    @Override
    public boolean deleteDistrict(int idO) {
        final String sqlQuery = "DELETE FROM Opstina WHERE IdOpstina=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idO);
            return (ps.executeUpdate() == 1);
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> deleteDistrict");
        }
        return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String string) {
        String sqlQuery = "SELECT IdGrad FROM Grad WHERE Naziv=?";
        List<Integer> ids = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery);){
            ps.setString(1,string);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next()) {
                    ids.add(rs.getInt("IdGrad"));
                }
            }
        }
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> deleteAllDistrictsFromCity");
        }
        
        sqlQuery = "DELETE FROM Opstina WHERE IdGrad=?";
        int numResult = 0;
        for(int id : ids) {
            try(PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
                ps.setInt(1, id);
                numResult = ps.executeUpdate();
            } 
            catch (SQLException ex) {
                //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> deleteAllDistrictsFromCity");
            }
        }
        return numResult;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int cityID) {
        List<Integer> districtsResult = new ArrayList<>();
	final String sqlQuery = "SELECT IdOpstina FROM Opstina WHERE IdGrad=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, cityID);
            try (ResultSet rs = ps.executeQuery()) {
		while (rs.next()) {
                    districtsResult.add(rs.getInt("IdOpstina"));
		}
            }
            return districtsResult;
	} 
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> getAllDistrictsFromCity");   
	}
        return null;
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> districtsResult = new ArrayList<>();
        final String sqlQuery = "SELECT IdOps FROM Opstina";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                districtsResult.add(rs.getInt("IdOps"));
            }
            return districtsResult;
        }
        catch (SQLException ex) {
            //Logger.getLogger(CuidadoDistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoDistrictOperations.class.getName() + ", method -> getAllDistricts");
        }
        return districtsResult;
    }
    
}
