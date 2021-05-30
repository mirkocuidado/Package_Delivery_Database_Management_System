package tests;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.VehicleOperations;

public class PackageOperationsTest {
  private UserOperations userOperations;
  
  private GeneralOperations generalOperations;
  
  private PackageOperations packageOperations;
  
  private TestHandler testHandler;
  
  private DistrictOperations districtOperations;
  
  private CityOperations cityOperations;
  
  private VehicleOperations vehicleOperations = null;
  
  private CourierOperations courierOperations = null;
  
  private CourierRequestOperation courierRequestOperation = null;
  
  @Before
  public void setUp() throws Exception {
    this.testHandler = TestHandler.getInstance();
    Assert.assertNotNull(this.testHandler);
    this.cityOperations = this.testHandler.getCityOperations();
    Assert.assertNotNull(this.cityOperations);
    this.districtOperations = this.testHandler.getDistrictOperations();
    Assert.assertNotNull(this.districtOperations);
    this.userOperations = this.testHandler.getUserOperations();
    Assert.assertNotNull(this.userOperations);
    this.packageOperations = this.testHandler.getPackageOperations();
    Assert.assertNotNull(this.packageOperations);
    this.generalOperations = this.testHandler.getGeneralOperations();
    Assert.assertNotNull(this.generalOperations);
    this.courierOperations = this.testHandler.getCourierOperations();
    Assert.assertNotNull(this.courierOperations);
    this.vehicleOperations = this.testHandler.getVehicleOperations();
    Assert.assertNotNull(this.vehicleOperations);
    this.courierRequestOperation = this.testHandler.getCourierRequestOperation();
    Assert.assertNotNull(this.courierRequestOperation);
    this.generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception {
    this.generalOperations.eraseAll();
  }
  
  private void insertCourier(String courierUsername) {
    String firstName = "Svetislav";
    String lastName = "Kisprdilov";
    String password = "sisatovac123";
    this.userOperations.insertUser(courierUsername, firstName, lastName, password);
    String licencePlate = "BG323WE";
    int fuelType = 0;
    BigDecimal fuelConsumption = new BigDecimal(8.3D);
    this.testHandler.getVehicleOperations().insertVehicle(licencePlate, fuelType, fuelConsumption);
    this.testHandler.getCourierRequestOperation().insertCourierRequest(courierUsername, licencePlate);
    this.testHandler.getCourierRequestOperation().grantRequest(courierUsername);
  }
  
  private void insertUser(String username) {
    String firstName = "Svetislav";
    String lastName = "Kisprdilov";
    String password = "sisatovac123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
  }
  
  public int insertPackageH(int packageType) {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    return this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
  }
  
  @Test
  public void insertPackage() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    Assert.assertNotEquals(-1L, this.packageOperations
        .insertPackage(districtFrom, districtTo, username, packageType, weight));
  }
  
  @Test
  public void insertTransportOffer() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    String usernameCourier = "Alpi";
    String firstNameCourier = "Pero";
    String lastNameCourier = "Simic";
    String passwordCourier = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
    String licencePlate = "BG213KH";
    int fuelType = 1;
    BigDecimal fuelConsumption = new BigDecimal(12.3D);
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
    Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
    Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
    BigDecimal pricePercentage = new BigDecimal(3.3D);
    Assert.assertNotEquals(-1L, this.packageOperations
        .insertTransportOffer(usernameCourier, idPackage, pricePercentage));
  }
  
  @Test
  public void acceptAnOffer() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    String usernameCourier = "Alpi";
    String firstNameCourier = "Pero";
    String lastNameCourier = "Simic";
    String passwordCourier = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
    String licencePlate = "BG213KH";
    int fuelType = 1;
    BigDecimal fuelConsumption = new BigDecimal(12.3D);
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
    Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
    Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
    BigDecimal pricePercentage = new BigDecimal(3.3D);
    int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
    Assert.assertNotEquals(-1L, offerId);
    Assert.assertTrue(this.packageOperations.acceptAnOffer(offerId));
  }
  
  @Test
  public void getAllOffers() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    String usernameCourier = "Alpi";
    String firstNameCourier = "Pero";
    String lastNameCourier = "Simic";
    String passwordCourier = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
    String licencePlate = "BG213KH";
    int fuelType = 1;
    BigDecimal fuelConsumption = new BigDecimal(12.3D);
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
    Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
    Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
    BigDecimal pricePercentage = new BigDecimal(3.3D);
    int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
    Assert.assertNotEquals(-1L, offerId);
    Assert.assertEquals(1L, this.packageOperations.getAllOffers().size());
  }
  
  @Test
  public void getAllOffersForPackage() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    String usernameCourier = "Alpi";
    String firstNameCourier = "Pero";
    String lastNameCourier = "Simic";
    String passwordCourier = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
    String licencePlate = "BG213KH";
    int fuelType = 1;
    BigDecimal fuelConsumption = new BigDecimal(12.3D);
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
    Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
    Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
    BigDecimal pricePercentage = new BigDecimal(3.3D);
    int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
    Assert.assertNotEquals(-1L, offerId);
    Assert.assertEquals(1L, this.packageOperations.getAllOffersForPackage(idPackage).size());
  }
  
  @Test
  public void deletePackage() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    Assert.assertTrue(this.packageOperations.deletePackage(idPackage));
  }
  
  @Test
  public void changeWeight() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    Assert.assertTrue(this.packageOperations.changeWeight(idPackage, new BigDecimal(0.4D)));
  }
  
  @Test
  public void changeType() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    Assert.assertTrue(this.packageOperations.changeType(idPackage, 2));
    Assert.assertTrue(this.packageOperations.changeType(idPackage, 1));
    Assert.assertTrue(this.packageOperations.changeType(idPackage, 0));
  }
  
  @Test
  public void changeType_wrongType() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    Assert.assertFalse(this.packageOperations.changeType(idPackage, 3));
    Assert.assertFalse(this.packageOperations.changeType(idPackage, -1));
    Assert.assertFalse(this.packageOperations.changeType(idPackage, 323));
  }
  
  @Test
  public void getPriceOfDelivery() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    String usernameCourier = "Alpi";
    String firstNameCourier = "Pero";
    String lastNameCourier = "Simic";
    String passwordCourier = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
    String licencePlate = "BG213KH";
    int fuelType = 1;
    BigDecimal fuelConsumption = new BigDecimal(12.3D);
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
    Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
    Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
    BigDecimal pricePercentage = new BigDecimal(3.3D);
    int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
    Assert.assertNotEquals(-1L, offerId);
    Assert.assertTrue(this.packageOperations.acceptAnOffer(offerId));
    Assert.assertNotNull(this.packageOperations.getPriceOfDelivery(idPackage));
  }
  
  @Test
  public void getAcceptanceTime() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    String usernameCourier = "Alpi";
    String firstNameCourier = "Pero";
    String lastNameCourier = "Simic";
    String passwordCourier = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(usernameCourier, firstNameCourier, lastNameCourier, passwordCourier));
    String licencePlate = "BG213KH";
    int fuelType = 1;
    BigDecimal fuelConsumption = new BigDecimal(12.3D);
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
    Assert.assertTrue(this.courierRequestOperation.insertCourierRequest(usernameCourier, licencePlate));
    Assert.assertTrue(this.courierRequestOperation.grantRequest(usernameCourier));
    BigDecimal pricePercentage = new BigDecimal(3.3D);
    int offerId = this.packageOperations.insertTransportOffer(usernameCourier, idPackage, pricePercentage);
    Assert.assertNotEquals(-1L, offerId);
    Assert.assertTrue(this.packageOperations.acceptAnOffer(offerId));
    Assert.assertNotNull(this.packageOperations.getAcceptanceTime(idPackage));
  }
  
  @Test
  public void getAllPackagesWithSpecificType() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    username = "rope123";
    firstName = "Pero";
    lastName = "Simic";
    password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    packageType = 2;
    idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    Assert.assertEquals(1L, this.packageOperations.getAllPackagesWithSpecificType(2).size());
    Assert.assertEquals(1L, this.packageOperations.getAllPackagesWithSpecificType(1).size());
  }
  
  @Test
  public void getAllPackages() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int districtFrom = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    int districtTo = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    String username = "rope";
    String firstName = "Pero";
    String lastName = "Simic";
    String password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    BigDecimal weight = new BigDecimal(0.2D);
    int packageType = 1;
    int idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    username = "rope123";
    firstName = "Pero";
    lastName = "Simic";
    password = "tralalalala123";
    Assert.assertTrue(this.userOperations.insertUser(username, firstName, lastName, password));
    packageType = 2;
    idPackage = this.packageOperations.insertPackage(districtFrom, districtTo, username, packageType, weight);
    Assert.assertNotEquals(-1L, idPackage);
    Assert.assertEquals(2L, this.packageOperations.getAllPackages().size());
  }
}
