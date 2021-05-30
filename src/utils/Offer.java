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
public class Offer {
    private String courierUsername;
    private int idPackage;
    private double percentage;
    
    public Offer(String courierUsername, int idPackage, double percentage) {
        this.courierUsername = courierUsername;
        this.idPackage = idPackage;
        this.percentage = percentage;
    }

    public String getCourierUsername() {
        return courierUsername;
    }

    public void setCourierUsername(String courierUsername) {
        this.courierUsername = courierUsername;
    }

    public int getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(int idPackage) {
        this.idPackage = idPackage;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
}
