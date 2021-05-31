package tests;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.VehicleOperations;

public class VehicleOperationsTest {
  private GeneralOperations generalOperations;
  
  private VehicleOperations vehicleOperations;
  
  private TestHandler testHandler;
  
  @Before
  public void setUp() throws Exception {
    this.testHandler = TestHandler.getInstance();
    Assert.assertNotNull(this.testHandler);
    this.vehicleOperations = this.testHandler.getVehicleOperations();
    Assert.assertNotNull(this.vehicleOperations);
    this.generalOperations = this.testHandler.getGeneralOperations();
    Assert.assertNotNull(this.generalOperations);
    this.generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception {}
  
  @Test
  public void insertVehicle() {
    String licencePlateNumber = "BG234DU";
    BigDecimal fuelConsumption = new BigDecimal(6.3D);
    int fuelType = 1;
    Assert.assertTrue(this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption));
  }
  
  @Test
  public void deleteVehicles() {
    String licencePlateNumber = "BG234DU";
    BigDecimal fuelConsumption = new BigDecimal(6.3D);
    int fuelType = 1;
    this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
    Assert.assertEquals(1L, this.vehicleOperations.deleteVehicles(new String[] { licencePlateNumber }));
  }
  
  @Test
  public void getAllVehichles() {
    String licencePlateNumber = "BG234DU";
    BigDecimal fuelConsumption = new BigDecimal(6.3D);
    int fuelType = 1;
    this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
    Assert.assertTrue(this.vehicleOperations.getAllVehichles().contains(licencePlateNumber));
  }
  
  @Test
  public void changeFuelType() {
    String licencePlateNumber = "BG234DU";
    BigDecimal fuelConsumption = new BigDecimal(6.3D);
    int fuelType = 1;
    this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
    Assert.assertTrue(this.vehicleOperations.changeFuelType(licencePlateNumber, 2));
  }
  
  @Test
  public void changeConsumption() {
    String licencePlateNumber = "BG234DU";
    BigDecimal fuelConsumption = new BigDecimal(6.3D);
    int fuelType = 1;
    this.vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption);
    Assert.assertTrue(this.vehicleOperations.changeConsumption(licencePlateNumber, new BigDecimal(7.3D)));
  }
}
