package tests;

import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.VehicleOperations;

public class TestHandler {
  private static TestHandler testHandler = null;
  
  private CityOperations cityOperations;
  
  private CourierOperations courierOperations;
  
  private CourierRequestOperation courierRequestOperation;
  
  private DistrictOperations districtOperations;
  
  private GeneralOperations generalOperations;
  
  private UserOperations userOperations;
  
  private VehicleOperations vehicleOperations;
  
  private PackageOperations packageOperations;
  
  private TestHandler(CityOperations cityOperations, CourierOperations courierOperations, CourierRequestOperation courierRequestOperation, DistrictOperations districtOperations, GeneralOperations generalOperations, UserOperations userOperations, VehicleOperations vehicleOperations, PackageOperations packageOperations) {
    this.cityOperations = cityOperations;
    this.courierOperations = courierOperations;
    this.courierRequestOperation = courierRequestOperation;
    this.districtOperations = districtOperations;
    this.generalOperations = generalOperations;
    this.userOperations = userOperations;
    this.vehicleOperations = vehicleOperations;
    this.packageOperations = packageOperations;
  }
  
  public static void createInstance(CityOperations cityOperations, CourierOperations courierOperations, CourierRequestOperation courierRequestOperation, DistrictOperations districtOperations, GeneralOperations generalOperations, UserOperations userOperations, VehicleOperations vehicleOperations, PackageOperations packageOperations) {
    testHandler = new TestHandler(cityOperations, courierOperations, courierRequestOperation, districtOperations, generalOperations, userOperations, vehicleOperations, packageOperations);
  }
  
  static TestHandler getInstance() {
    return testHandler;
  }
  
  CityOperations getCityOperations() {
    return this.cityOperations;
  }
  
  CourierOperations getCourierOperations() {
    return this.courierOperations;
  }
  
  CourierRequestOperation getCourierRequestOperation() {
    return this.courierRequestOperation;
  }
  
  DistrictOperations getDistrictOperations() {
    return this.districtOperations;
  }
  
  GeneralOperations getGeneralOperations() {
    return this.generalOperations;
  }
  
  UserOperations getUserOperations() {
    return this.userOperations;
  }
  
  VehicleOperations getVehicleOperations() {
    return this.vehicleOperations;
  }
  
  PackageOperations getPackageOperations() {
    return this.packageOperations;
  }
}
