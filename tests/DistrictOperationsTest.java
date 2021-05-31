package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.GeneralOperations;

public class DistrictOperationsTest {
  private GeneralOperations generalOperations;
  
  private DistrictOperations districtOperations;
  
  private CityOperations cityOperations;
  
  private TestHandler testHandler;
  
  @Before
  public void setUp() throws Exception {
    this.testHandler = TestHandler.getInstance();
    Assert.assertNotNull(this.testHandler);
    this.cityOperations = this.testHandler.getCityOperations();
    Assert.assertNotNull(this.cityOperations);
    this.districtOperations = this.testHandler.getDistrictOperations();
    Assert.assertNotNull(this.districtOperations);
    this.generalOperations = this.testHandler.getGeneralOperations();
    Assert.assertNotNull(this.generalOperations);
    this.generalOperations.eraseAll();
  }
  
  @After
  public void tearDown() throws Exception {
    this.generalOperations.eraseAll();
  }
  
  @Test
  public void insertDistrict_ExistingCity() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int idDistrict = this.districtOperations.insertDistrict("Palilula", idCity, 10, 10);
    Assert.assertNotEquals(-1L, idDistrict);
    Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(Integer.valueOf(idDistrict)));
  }
  
  @Test
  public void deleteDistricts_multiple_existing() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    String districtOneName = "Palilula";
    String districtTwoName = "Vozdovac";
    int idDistrict1 = this.districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
    int idDistrict2 = this.districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
    Assert.assertEquals(2L, this.districtOperations.deleteDistricts(new String[] { districtOneName, districtTwoName }));
  }
  
  @Test
  public void deleteDistrict() {
    int idCity = this.cityOperations.insertCity("Belgrade", "11000");
    Assert.assertNotEquals(-1L, idCity);
    int idDistrict1 = this.districtOperations.insertDistrict("Vozdovac", idCity, 10, 10);
    Assert.assertTrue(this.districtOperations.deleteDistrict(idDistrict1));
  }
  
  @Test
  public void deleteAllDistrictsFromCity() {
    String cityName = "Belgrade";
    int idCity = this.cityOperations.insertCity(cityName, "11000");
    Assert.assertNotEquals(-1L, idCity);
    String districtOneName = "Palilula";
    String districtTwoName = "Vozdovac";
    int idDistrict1 = this.districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
    int idDistrict2 = this.districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
    Assert.assertEquals(2L, this.districtOperations.deleteAllDistrictsFromCity(cityName));
  }
  
  @Test
  public void getAllDistrictsFromCity() {
    String cityName = "Belgrade";
    int idCity = this.cityOperations.insertCity(cityName, "11000");
    Assert.assertNotEquals(-1L, idCity);
    String districtOneName = "Palilula";
    String districtTwoName = "Vozdovac";
    int idDistrict1 = this.districtOperations.insertDistrict(districtOneName, idCity, 10, 10);
    int idDistrict2 = this.districtOperations.insertDistrict(districtTwoName, idCity, 1, 10);
    Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(Integer.valueOf(idDistrict1)));
    Assert.assertTrue(this.districtOperations.getAllDistrictsFromCity(idCity).contains(Integer.valueOf(idDistrict2)));
  }
}
