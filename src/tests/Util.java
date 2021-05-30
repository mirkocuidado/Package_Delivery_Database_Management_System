package tests;

import java.math.BigDecimal;

public class Util {
  static double euclidean(int x1, int y1, int x2, int y2) {
    return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
  }
  
  static BigDecimal getPackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
    percentage = percentage.divide(new BigDecimal(100));
    switch (type) {
      case 0:
        return (new BigDecimal(10.0D * distance)).multiply(percentage.add(new BigDecimal(1)));
      case 1:
        return (new BigDecimal((25.0D + weight.doubleValue() * 100.0D) * distance)).multiply(percentage.add(new BigDecimal(1)));
      case 2:
        return (new BigDecimal((75.0D + weight.doubleValue() * 300.0D) * distance)).multiply(percentage.add(new BigDecimal(1)));
    } 
    return null;
  }
}
