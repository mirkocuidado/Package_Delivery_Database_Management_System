package sabprojekat;

import com.microsoft.sqlserver.jdbc.SQLServerException;
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
import rs.etf.sab.operations.CityOperations;

public class CuidadoCityOperations implements CityOperations{

    private Connection connection = DB.getInstance().getConnection();
    
    @Override
    public int insertCity(String name, String postal) {
        final String sqlQuery = "INSERT INTO Grad (Naziv, PostanskiBroj) VALUES (?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, name);
            ps.setString(2, postal);
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) return rs.getInt(1);
                else return -1;
            }
            
        } catch (SQLException ex) {
            if(ex instanceof SQLServerException) {
                //System.out.println("There already exists a city with this name or this postal code!");
                return -1;
            }
            //Logger.getLogger(CuidadoCityOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCityOperations.class.getName() + ", method -> insertCity");
        }
        return -1;
    }

    @Override
    public int deleteCity(String... names) {
        final String sqlQuery = "DELETE FROM Grad WHERE Naziv=?";
        int totalNumber = 0;
        for(String name : names) {
            try(PreparedStatement ps = connection.prepareStatement(sqlQuery);){
                ps.setString(1, name);
                totalNumber += ps.executeUpdate();
            } 
            catch (SQLException ex) {
                //Logger.getLogger(CuidadoCityOperations.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Exception at -> " + CuidadoCityOperations.class.getName()+ ", method -> deleteCity");
            }
        }
        return totalNumber;
    }

    @Override
    public boolean deleteCity(int idCity) {
        final String sqlQuery = "DELETE FROM Grad WHERE IdGrad=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idCity);
            int numberOfAffectedRows = ps.executeUpdate();
            if(numberOfAffectedRows == 1) {
                //System.out.println("Successfuly deleted city with id " + idCity);
                return true;
            }
            else return false;
	} catch (SQLException ex) {
            //Logger.getLogger(CuidadoCityOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCityOperations.class.getName() + ", method -> deleteCity");
            return false;
	}
    }

    @Override
    public List<Integer> getAllCities() {
        final String sqlQuery = "SELECT idGrad FROM Grad";
        List<Integer> resultList = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);) {
            
            while(rs.next()) {
                resultList.add(rs.getInt("idGrad"));
            }
            
            return resultList;
            
        } catch (SQLException ex) {
            //Logger.getLogger(CuidadoCityOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception at -> " + CuidadoCityOperations.class.getName() + ", method -> getAllCities");
        }
        return null;
    }
    
}
