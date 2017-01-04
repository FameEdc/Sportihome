package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class RateModel {
    private int valueForMoney=0;
    private int location=0;
    private int cleanness=0;
    private int overallRating=0;
    private int numberOfRatings=0;

    public RateModel() {}

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public int getCleanness() {
        return cleanness;
    }

    public void setCleanness(int cleanness) {
        this.cleanness = cleanness;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getValueForMoney() {
        return valueForMoney;
    }

    public void setValueForMoney(int valueForMoney) {
        this.valueForMoney = valueForMoney;
    }
}
