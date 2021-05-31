
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
import sabprojekat.CuidadoCityOperations;
import sabprojekat.CuidadoCourierOperations;
import sabprojekat.CuidadoCourierRequestOperations;
import sabprojekat.CuidadoDistrictOperations;
import sabprojekat.CuidadoGeneralOperations;
import sabprojekat.CuidadoPackageOperations;
import sabprojekat.CuidadoUserOperations;
import sabprojekat.CuidadoVehicleOperations;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 38164
 */
public class studentMain {
    public static void main(String[] args) {
        CityOperations cityOperations = new CuidadoCityOperations();
        DistrictOperations districtOperations = new CuidadoDistrictOperations();
        CourierOperations courierOperations = new CuidadoCourierOperations();
        CourierRequestOperation courierRequestOperation = new CuidadoCourierRequestOperations();
        GeneralOperations generalOperations = new CuidadoGeneralOperations();
        UserOperations userOperations = new CuidadoUserOperations();
        VehicleOperations vehicleOperations = new CuidadoVehicleOperations();
        PackageOperations packageOperations = new CuidadoPackageOperations();
        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);
        TestRunner.runTests();
    }
}
