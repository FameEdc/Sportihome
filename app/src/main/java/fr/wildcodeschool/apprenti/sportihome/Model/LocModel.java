package fr.wildcodeschool.apprenti.sportihome.Model;

/**
 * Created by chantome on 09/01/2017.
 */

public class LocModel {
    private String type;
    private double[] coordinates;

    public LocModel(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
