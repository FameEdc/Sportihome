package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class HomeModel {
    private int travellers;
    private String propertyType;
    private PriceModel price;

    public HomeModel() {
    }

    public int getTravellers() {
        return travellers;
    }

    public void setTravellers(int travellers) {
        this.travellers = travellers;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public PriceModel getPrice() {
        return price;
    }

    public void setPrice(PriceModel price) {
        this.price = price;
    }
}
