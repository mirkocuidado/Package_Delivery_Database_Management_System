package tests;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PublicModuleTest {
  private TestHandler testHandler;
  
  @Before
  public void setUp() {
    this.testHandler = TestHandler.getInstance();
    Assert.assertNotNull(this.testHandler);
    Assert.assertNotNull(this.testHandler.getGeneralOperations());
    this.testHandler.getGeneralOperations().eraseAll();
  }
  
  @After
  public void tearUp() {
    this.testHandler.getGeneralOperations().eraseAll();
  }
  
  @Test
  public void publicOne() {
    //this.testHandler.getGeneralOperations().eraseAll();
    String courierLastName = "Ckalja";
    String courierFirstName = "Pero";
    String courierUsername = "perkan";
    String password = "sabi2018";
    this.testHandler.getUserOperations().insertUser(courierUsername, courierFirstName, courierLastName, password);
    
    String licencePlate = "BG323WE";
    int fuelType = 0;
    BigDecimal fuelConsumption = new BigDecimal(8.3D);
    this.testHandler.getVehicleOperations().insertVehicle(licencePlate, fuelType, fuelConsumption);
    
    this.testHandler.getCourierRequestOperation().insertCourierRequest(courierUsername, licencePlate);
    
    this.testHandler.getCourierRequestOperation().grantRequest(courierUsername);
    
    Assert.assertTrue(this.testHandler.getCourierOperations().getAllCouriers().contains(courierUsername));
    
    String senderUsername = "masa";
    String senderFirstName = "Masana";
    String senderLastName = "Leposava";
    password = "lepasampasta1";
    this.testHandler.getUserOperations()
      .insertUser(senderUsername, senderFirstName, senderLastName, password);
    
    int cityId = this.testHandler.getCityOperations().insertCity("Novo Milosevo", "21234");
    
    int cordXd1 = 10;
    int cordYd1 = 2;
    int districtIdOne = this.testHandler.getDistrictOperations().insertDistrict("Novo Milosevo", cityId, cordXd1, cordYd1);
    
    int cordXd2 = 2;
    int cordYd2 = 10;
    int districtIdTwo = this.testHandler.getDistrictOperations().insertDistrict("Vojinovica", cityId, cordXd2, cordYd2);
    
    int type1 = 0;
    BigDecimal weight1 = new BigDecimal(123);
    int packageId1 = this.testHandler.getPackageOperations().insertPackage(districtIdOne, districtIdTwo, courierUsername, type1, weight1);
    
    BigDecimal packageOnePrice = Util.getPackagePrice(type1, weight1, 
        Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
    int offerId = this.testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId1, new BigDecimal(5));
    
    this.testHandler.getPackageOperations().acceptAnOffer(offerId);
    
    int type2 = 1;
    BigDecimal weight2 = new BigDecimal(321);
    int packageId2 = this.testHandler.getPackageOperations().insertPackage(districtIdTwo, districtIdOne, courierUsername, type2, weight2);
    BigDecimal packageTwoPrice = Util.getPackagePrice(type2, weight2, 
        Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
    offerId = this.testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId2, new BigDecimal(5));
    this.testHandler.getPackageOperations().acceptAnOffer(offerId);
    int type3 = 1;
    BigDecimal weight3 = new BigDecimal(222);
    int packageId3 = this.testHandler.getPackageOperations().insertPackage(districtIdTwo, districtIdOne, courierUsername, type3, weight3);
    BigDecimal packageThreePrice = Util.getPackagePrice(type3, weight3, 
        Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
    offerId = this.testHandler.getPackageOperations().insertTransportOffer(courierUsername, packageId3, new BigDecimal(5));
    this.testHandler.getPackageOperations().acceptAnOffer(offerId);
    Assert.assertEquals(1L, this.testHandler
        .getPackageOperations().getDeliveryStatus(packageId1).intValue());
    Assert.assertEquals(packageId1, this.testHandler
        .getPackageOperations().driveNextPackage(courierUsername));
    Assert.assertEquals(3L, this.testHandler
        .getPackageOperations().getDeliveryStatus(packageId1).intValue());
    Assert.assertEquals(2L, this.testHandler
        .getPackageOperations().getDeliveryStatus(packageId2).intValue());
    Assert.assertEquals(packageId2, this.testHandler
        .getPackageOperations().driveNextPackage(courierUsername));
    Assert.assertEquals(3L, this.testHandler
        .getPackageOperations().getDeliveryStatus(packageId2).intValue());
    Assert.assertEquals(2L, this.testHandler
        .getPackageOperations().getDeliveryStatus(packageId3).intValue());
    Assert.assertEquals(packageId3, this.testHandler
        .getPackageOperations().driveNextPackage(courierUsername));
    Assert.assertEquals(3L, this.testHandler
        .getPackageOperations().getDeliveryStatus(packageId3).intValue());
    
    BigDecimal gain = packageOnePrice.add(packageTwoPrice).add(packageThreePrice);
    BigDecimal loss = (new BigDecimal(Util.euclidean(cordXd1, cordYd1, cordXd2, cordYd2) * 4.0D * 15.0D)).multiply(fuelConsumption);
    
    BigDecimal actual = this.testHandler.getCourierOperations().getAverageCourierProfit(0);
    Assert.assertTrue((gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(1.001D))) < 0));
    Assert.assertTrue((gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(0.999D))) > 0));
  }
}
