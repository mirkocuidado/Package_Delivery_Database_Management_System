/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author 38164
 */
public class LossDetails {
    double fuel;
    double perL;

    public LossDetails(double fuel, double perL) {
        this.fuel = fuel;
        this.perL = perL;
    }
    
    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getPerL() {
        return perL;
    }

    public void setPerL(double perL) {
        this.perL = perL;
    }
    
    
}
