package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public final class TestRunner {
  private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
  
  private static final Class[] UNIT_TEST_CLASSES = new Class[] { CityOperationsTest.class, DistrictOperationsTest.class, UserOperationsTest.class, VehicleOperationsTest.class, PackageOperationsTest.class };
  
  private static final Class[] MODULE_TEST_CLASSES = new Class[] { PublicModuleTest.class };
  
  private static double runUnitTestsPublic() {
    double numberOfSuccessfulCases = 0.0D;
    double numberOfAllCases = 0.0D;
    double points = 0.0D;
    JUnitCore jUnitCore = new JUnitCore();
    for (Class testClass : UNIT_TEST_CLASSES) {
      System.out.println("\n" + testClass.getName());
      Request request = Request.aClass(testClass);
      Result result = jUnitCore.run(request);
      System.out.println("Successful:" + (result.getRunCount() - result.getFailureCount()));
      System.out.println("All:" + result.getRunCount());
      numberOfAllCases = result.getRunCount();
      numberOfSuccessfulCases = (result.getRunCount() - result.getFailureCount());
      points += numberOfSuccessfulCases / numberOfAllCases;
    } 
    return points;
  }
  
  private static double runModuleTestsPublic() {
    double numberOfSuccessfulCases = 0.0D;
    double numberOfAllCases = 0.0D;
    double points = 0.0D;
    JUnitCore jUnitCore = new JUnitCore();
    for (Class testClass : MODULE_TEST_CLASSES) {
      System.out.println("\n" + testClass.getName());
      Request request = Request.aClass(testClass);
      Result result = jUnitCore.run(request);
      System.out.println("Successful:" + (result.getRunCount() - result.getFailureCount()));
      System.out.println("All:" + result.getRunCount());
      numberOfAllCases = result.getRunCount();
      numberOfSuccessfulCases = (result.getRunCount() - result.getFailureCount());
      points += numberOfSuccessfulCases / numberOfAllCases;
    } 
    return points;
  }
  
  private static double runPublic() {
    double res = 0.0D;
    res += runUnitTestsPublic() * 2.0D;
    res += runModuleTestsPublic() * 2.0D;
    return res;
  }
  
  public static void runTests() {
    double resultsPublic = runPublic();
    System.out.println("Points won on public tests is: " + resultsPublic + " out of 10");
  }
}
