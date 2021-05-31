/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabprojekat;

import database.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;
import utils.CuidadoPair;
import utils.DistrictLocationDetails;
import utils.LossDetails;
import utils.Offer;

/**
 *
 * @author 38164
 */
public class CuidadoPackageOperations implements PackageOperations{

    private Connection connection = DB.getInstance().getConnection();
    
    @Override
    public int insertPackage(int d1, int d2, String username, int type, BigDecimal weight) {
        if(type < 0 || type > 2 || weight.doubleValue() < 0.0) return -1;
        final String sqlQuery = "INSERT INTO Paket (IdOpstinaOd, IdOpstinaDo, KorisnikPosaljilac, TipPaketa, TezinaPaketa, StatusIsporuke) VALUES (?,?,?,?,?,0)";
        int numResult = 0;
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1,d1);
            ps.setInt(2,d2);
            ps.setString(3,username);
            ps.setInt(4,type);
            ps.setBigDecimal(5,weight);
            numResult = ps.executeUpdate();
            if(numResult == 0) return -1;
            
            try(ResultSet rs = ps.getGeneratedKeys();) {
                if(rs.next()) return rs.getInt(1);
                else return -1;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int insertTransportOffer(String usernameCourier, int idPaket, BigDecimal percent) {
        BigDecimal pom = percent;
        if(pom == null) {
            double d = Math.random()*(10 + 10) - 10;
            pom = new BigDecimal(d);
        }
        
        boolean isAvailable = false;
        
        String sqlQuery = "SELECT * FROM Kurir WHERE KorisnickoIme=? AND Status=0";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) isAvailable = true;
            }
	} catch (SQLException e) {
            return -1;
	}
        
        double percentValue = pom.doubleValue();
        if(percentValue < -10.0 || percentValue > 10.0 || isAvailable == false) return -1;
        
        int numResult = 0;
        
        sqlQuery = "INSERT INTO Ponuda (KorisnickoIme, IdPaket, Procenat) VALUES (?, ?, ?)";
        
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, usernameCourier);
            ps.setInt(2, idPaket);
            ps.setBigDecimal(3, pom);
            numResult = ps.executeUpdate();
            if(numResult == 0) return -1;
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
		if (rs.next())
                    return rs.getInt(1);
                else return -1;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private DistrictLocationDetails getCoordinates(int idOpstina) {
        final String sqlQuery = "SELECT XKoordinata, YKoordinata FROM Opstina WHERE IdOpstina=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idOpstina);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DistrictLocationDetails(rs.getInt("XKoordinata"), rs.getInt("YKoordinata"));
                }
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private BigDecimal calculatePrice(int idPaket, double discount) {
        final String sqlQuery = "SELECT IdOpstinaOd, IdOpstinaDo, TipPaketa, TezinaPaketa FROM Paket WHERE IdPaket=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // dohvati opstinu OD
                    int opstinaOdID = rs.getInt("IdOpstinaOd");
                    DistrictLocationDetails opstinaOd = getCoordinates(opstinaOdID);
                    
                    // dohvati opstinu DO
                    int opstinaDoID = rs.getInt("IdOpstinaDo");
                    DistrictLocationDetails opstinaDo = getCoordinates(opstinaDoID);
                    
                    if(opstinaOd == null || opstinaDo == null) return null;
                    
                    // dohvati tip paketa i cene
                    int typeOfPackage = rs.getInt("TipPaketa");
                    double start, weight, perKG;
                    switch(typeOfPackage) {
                        case 0: {
                            start = 10.0;
                            weight = 0.0;
                            perKG = 0.0;
                            break;
                        }
                        case 1: {
                            start = 25.0;
                            weight = 1.0;
                            perKG = 100.0;
                            break;
                        }
                        case 2: {
                            start = 75.0;
                            weight = 2.0;
                            perKG = 300.0;
                            break;
                        }
                        default: return null;
                    }
                    
                    // dohvati tezinu paketa
                    double weightOfPackage = rs.getBigDecimal("TezinaPaketa").doubleValue();
					
                    // racunaj cenu
                    double price = (start+weight*weightOfPackage*perKG) * calculateEuclid(opstinaOd, opstinaDo);
                    
                    // racunaj popust
                    double priceWithDiscount = price * (1.0 + discount / 100);
                    
                    return new BigDecimal(priceWithDiscount);
                }
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private double calculateEuclid(DistrictLocationDetails opstinaOd, DistrictLocationDetails opstinaDo) {
        double part1 = Math.pow((opstinaOd.getxCoord() - opstinaDo.getxCoord()), 2);
        double part2 = Math.pow((opstinaOd.getyCoord() - opstinaDo.getyCoord()), 2);
        return Math.sqrt(part1 + part2);
    }
    
    private Offer getCurrentOfferForThisID(int idOffer) {
        final String sqlQuery = "SELECT KorisnickoIme, IdPaket, Procenat FROM Ponuda WHERE IdPonuda=?";
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.setInt(1, idOffer);
            try(ResultSet rs = ps.executeQuery();) {
                if(rs.next()) {
                    return new Offer(rs.getString("KorisnickoIme"), rs.getInt("IdPaket"), rs.getBigDecimal("Procenat").doubleValue());
                }
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean increaseNumberOfSentPackages(int idPaket) {
        final String sqlQuery = "UPDATE Korisnik SET BrojPoslatihPaketa=BrojPoslatihPaketa+1 WHERE KorisnickoIme=(SELECT KorisnikPosaljilac FROM Paket WHERE IdPaket=?)";
        try (PreparedStatement ps3 = connection.prepareStatement(sqlQuery)) {
            ps3.setInt(1, idPaket);
            return ps3.executeUpdate() == 1;
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private boolean updateStatusOfPackageToBePickedUp(Offer currentOffer) {
        final String sqlQuery = "UPDATE Paket SET StatusIsporuke=1, VremePrihvatanja=GETDATE(), Cena=?, Kurir=? WHERE IdPaket=?";
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery);) {
            ps.setBigDecimal(1, calculatePrice(currentOffer.getIdPackage(), currentOffer.getPercentage()));
            ps.setString(2, currentOffer.getCourierUsername());
            ps.setInt(3, currentOffer.getIdPackage());
            int numRows = ps.executeUpdate();
            if(numRows != 1) return false;
            else return increaseNumberOfSentPackages(currentOffer.getIdPackage());
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean acceptAnOffer(int idOffer) {
        Offer currentOffer = getCurrentOfferForThisID(idOffer);
        if(currentOffer == null) return false;

        return updateStatusOfPackageToBePickedUp(currentOffer);
    }

    @Override
    public List<Integer> getAllOffers() {
        List<Integer> resultIDs = new ArrayList<>();
	final String sqlQuery = "SELECT IdPonuda FROM Ponuda";
	try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                resultIDs.add(rs.getInt("IdPonuda"));
            }
            return resultIDs;
	} catch (SQLException ex) {
            
	}
	return null;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int idPaket) {
        List<Pair<Integer, BigDecimal>> returnPairPackageOffers = new ArrayList<>();
	final String sqlQuery = "SELECT IdPonuda, Procenat FROM Ponuda WHERE IdPaket=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    returnPairPackageOffers.add(new CuidadoPair(rs.getInt("IdPonuda"), rs.getBigDecimal("Procenat")));
                }
                return returnPairPackageOffers;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean deletePackage(int idPaket) {
        final String sqlQuery = "DELETE FROM Paket WHERE IdPaket=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            return (ps.executeUpdate() == 1);
        } catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override // proveri da ne zeli da menja tezinu paketa koji je vec pokupljen i to na javnom ili tajnom testu!
    public boolean changeWeight(int idPaket, BigDecimal newWeight) {
        if (newWeight.doubleValue() < 0.0) return false;
        final String sqlQuery = "UPDATE Paket SET TezinaPaketa=? WHERE IdPaket=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setBigDecimal(1, newWeight);
            ps.setInt(2, idPaket);
            return (ps.executeUpdate() == 1); 
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override // proveri da ne zeli da menja tezinu paketa koji je vec pokupljen i to na javnom ili tajnom testu!
    public boolean changeType(int idpaket, int newType) {
        if (newType < 0 || newType > 2) return false;
        final String sqlQuery = "UPDATE Paket SET TipPaketa=? WHERE IdPaket=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, newType);
            ps.setInt(2, idpaket);
            return (ps.executeUpdate() == 1); 
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
	return false;
    }

    @Override
    public Integer getDeliveryStatus(int idPaket) {
        final String sqlQuery = "SELECT StatusIsporuke FROM Paket WHERE IdPaket=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("StatusIsporuke");
                else return null;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int idPaket) {
        final String sqlQuery = "SELECT Cena FROM Paket WHERE IdPaket=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal("Cena");
                else return null;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int idPaket) {
        final String sqlQuery = "SELECT VremePrihvatanja FROM Paket WHERE IdPaket=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDate("VremePrihvatanja");
                else return null;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        List<Integer> returnPackagesList = new ArrayList<>();
        if (type < 0 || type > 2) return returnPackagesList;
        final String sqlQuery = "SELECT IdPaket FROM Paket WHERE TipPaketa=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    returnPackagesList.add(rs.getInt("IdPaket"));
                }
                return returnPackagesList;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackages() {
        List<Integer> returnPackageIds = new ArrayList<>();
        final String sqlQuery = "SELECT IdPaket FROM Paket";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                returnPackageIds.add(rs.getInt("IdPaket"));
            }
            return returnPackageIds;
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getDrive(String usernameCourier) {
        String sqlQuery = "SELECT Status FROM Kurir WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if(rs.getInt("Status") != 1) return null;
                }
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        List<Integer> returnPackageIds = new ArrayList<>();
        sqlQuery = "SELECT IdPaket FROM Paket WHERE Kurir=? AND Status=2";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    returnPackageIds.add(rs.getInt("IdPaket"));
                }
                return returnPackageIds;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
	return null;	
    }

    private void updateCurrentCourierStatusToDriving(String usernameCourier) throws SQLException {
        final String sqlQuery = "UPDATE Kurir SET Status=1 WHERE KorisnickoIme=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            if(ps.executeUpdate() != 1) throw new SQLException("Exception at -> updateCurrentCourierStatusToDriving()");
        }	
    }
    
    private void updateAllPackagesOfCurrentCourierToPickedUp(String usernameCourier) throws SQLException {
        final String sqlQuery = "UPDATE Paket SET StatusIsporuke=2 WHERE StatusIsporuke=1 AND Kurir=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            if( ps.executeUpdate() == 0) throw new SQLException("Exception at -> updateAllPackagesOfCurrentCourierToPickedUp()");
        }
    }

    private void addAllPackagesToBeDriven(String usernameCourier) throws SQLException {
        final String sqlQuery = "SELECT IdPaket FROM Paket WHERE StatusIsporuke=2 AND Kurir=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPaket = rs.getInt("IdPaket");
                    final String sqlQuery2 = "INSERT INTO Voznja(KorisnickoIme, IdPaket) VALUES (?, ?)";
                    try (PreparedStatement ps2 = connection.prepareStatement(sqlQuery2)) {
                        ps2.setString(1, usernameCourier);
                        ps2.setInt(2, idPaket);
                        if (ps2.executeUpdate() == 0) throw new SQLException("Exception at -> addAllPackagesToBeDriven()");
                    }
                }
            }
        }
    }
    
    public boolean startDrive(String usernameCourier) {
        try {
            updateCurrentCourierStatusToDriving(usernameCourier);
            updateAllPackagesOfCurrentCourierToPickedUp(usernameCourier);
            addAllPackagesToBeDriven(usernameCourier);
        }
        catch(SQLException ex) {
            return false;
        }
        return true;
    }

    private int ifTheCourierIsNotDrivingSetHimToDrive(String usernameCourier) {
        final String sqlQuery = "SELECT Status FROM Kurir WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("Status") !=1) 
                    if(startDrive(usernameCourier) == false) return -2;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        return 100;
    }
    
    private int getNextPackageInFCFSOrder(String usernameCourier) {
        String sqlQuery = "SELECT TOP 1 IdPaket FROM Paket WHERE Kurir=? AND StatusIsporuke=2 ORDER BY VremePrihvatanja";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idPaket = rs.getInt("IdPaket");
                    return idPaket;
                }
                else {
                    if(!stopDrive(usernameCourier)) return -100;
                    return -1;
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -100;
    }
    
    private boolean deliverCurrentPackageBySettingHisStatusToDelieveredAndIncreasingCourierNumberOfPackagesByOne(int idPaket, String usernameCourier) {
        boolean successfulStatusUpdate = false;
        final String sqlQuery = "UPDATE Paket SET StatusIsporuke=3 WHERE IdPaket=?";
	try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setInt(1, idPaket);
            successfulStatusUpdate = (ps.executeUpdate() == 1);
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       
        if(successfulStatusUpdate == false) return false;
        
        final String sqlQuery2 = "UPDATE Kurir SET BrojPaketa=BrojPaketa+1 WHERE KorisnickoIme=?";
        try (PreparedStatement ps2 = connection.prepareStatement(sqlQuery2)) {
            ps2.setString(1, usernameCourier);
            return (ps2.executeUpdate() == 1); 
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
    
    private boolean isThereMorePackagesToBeDrivenByThisCourier(String usernameCourier) {
        final String sqlQuery = "SELECT TOP 1 IdPaket FROM Paket WHERE Kurir=? AND StatusIsporuke=2";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return true;
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private boolean stopDrive(String usernameCourier) {
        boolean successfulUpdateOfCurrentCourier = updateProfitForCurrentCourier(usernameCourier);
        if(!successfulUpdateOfCurrentCourier) return false;
        boolean successfulDeleteOfCurrentDrive = deleteDriveForCurrentCourier(usernameCourier);
        return successfulDeleteOfCurrentDrive;
    }
    
    @Override
    public int driveNextPackage(String usernameCourier) {
        if( ifTheCourierIsNotDrivingSetHimToDrive(usernameCourier)!=100 ) return -2;
        
        int idPaket = getNextPackageInFCFSOrder(usernameCourier);
        if(idPaket == -100) return -2;
        else if(idPaket == -1) return -1;
        
        if(!deliverCurrentPackageBySettingHisStatusToDelieveredAndIncreasingCourierNumberOfPackagesByOne(idPaket, usernameCourier)) return -2;
        
        if(!isThereMorePackagesToBeDrivenByThisCourier(usernameCourier)) {
            if(!stopDrive(usernameCourier)) return -2;
        }
        
        return idPaket;
    }

    private BigDecimal caclulateProfitForCurrentCourier(String usernameCourier) {
        final String sqlQuery = "SELECT Paket.IdOpstinaOd, Paket.IdOpstinaDo, Paket.Cena FROM Voznja, Paket WHERE Voznja.IdPaket=Paket.IdPaket AND Voznja.KorisnickoIme=?";
        double profit = 0.0;
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    profit += rs.getBigDecimal("Cena").doubleValue();
                }
                return new BigDecimal(profit);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private LossDetails findOutWhatPredefinedValuesAreUsedForLoss(String usernameCourier) {
        final String sqlQuery = "SELECT Vozilo.Potrosnja, Vozilo.TipGoriva FROM Kurir, Vozilo WHERE Kurir.RegistarskiBroj=Vozilo.RegistarskiBroj AND KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int fuelType = rs.getInt("TipGoriva");
                    double fuel = rs.getBigDecimal("Potrosnja").doubleValue();
                    switch(fuelType) {
                        case 0: return new LossDetails(fuel, 15.0);
                        case 1: return new LossDetails(fuel, 32.0);
                        case 2: return new LossDetails(fuel, 36.0);
                    }
                }
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private BigDecimal calculateLossBetweenEachTwoDistrictsOnTheRoad(String usernameCourier, LossDetails lossDetails) {
        final String sqlQuery = "SELECT Paket.IdOpstinaOd, Paket.IdOpstinaDo FROM Voznja, Paket WHERE Voznja.IdPaket=Paket.IdPaket AND Voznja.KorisnickoIme=? ORDER BY Paket.VremePrihvatanja";
        double totalLoss = 0.0;
        
        DistrictLocationDetails districtCurrent = null;
    
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int o1 = rs.getInt("IdOpstinaOd");
                    DistrictLocationDetails d1 = getCoordinates(o1);
                    if(d1 == null) return null;
                    
                    int o2 = rs.getInt("IdOpstinaDo");
                    DistrictLocationDetails d2 = getCoordinates(o2);
                    if(d2 == null) return null;
                    
                    if(districtCurrent != null) {
                        BigDecimal euclid = new BigDecimal(calculateEuclid(districtCurrent, d1));
                        totalLoss += euclid.doubleValue() * lossDetails.getFuel() * lossDetails.getPerL();
                    }
                    
                    BigDecimal euclid = new BigDecimal(calculateEuclid(d1, d2));
                    totalLoss += euclid.doubleValue() * lossDetails.getFuel() * lossDetails.getPerL();
                    
                    districtCurrent = d2;
                }
                return new BigDecimal(totalLoss);
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private BigDecimal caclulateLossForCurrentCourier(String usernameCourier) {
        LossDetails lossDetailsForLoss = findOutWhatPredefinedValuesAreUsedForLoss(usernameCourier);
        return calculateLossBetweenEachTwoDistrictsOnTheRoad(usernameCourier, lossDetailsForLoss);
    }
    
    private boolean updateProfitForCurrentCourier(String usernameCourier) {
        final String sqlQuery = "UPDATE Kurir SET Status=0, Profit=Profit+? WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            
            // izracunaj profit za kurira
            BigDecimal profit = caclulateProfitForCurrentCourier(usernameCourier);
            if(profit == null) return false;
            
            // izracunaj gubitak za kurira
            BigDecimal loss = caclulateLossForCurrentCourier(usernameCourier);
            if(loss == null) return false;
            
            // izracunaj pravi profit
            double realProfitValue = profit.doubleValue() - loss.doubleValue();
            
            ps.setBigDecimal(1, new BigDecimal(realProfitValue));
            ps.setString(2, usernameCourier);
            return ps.executeUpdate() == 1;
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean deleteDriveForCurrentCourier(String usernameCourier) {
        final String sqlQuery = "DELETE FROM Voznja WHERE KorisnickoIme=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, usernameCourier);
            return (ps.executeUpdate() != 0);
        } 
        catch (SQLException ex) {
            Logger.getLogger(CuidadoPackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
