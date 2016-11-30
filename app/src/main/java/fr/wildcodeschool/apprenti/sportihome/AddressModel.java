package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class AddressModel {
    private String administrative_area_level_1;
    private String country;
    private int street_number;
    private String route;
    private String locality;
    private String postal_code;
    private String complement;

    public AddressModel() {
    }

    public String getAdministrative_area_level_1() {
        return administrative_area_level_1;
    }

    public void setAdministrative_area_level_1(String administrative_area_level_1) {
        this.administrative_area_level_1 = administrative_area_level_1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStreet_number() {
        return street_number;
    }

    public void setStreet_number(int street_number) {
        this.street_number = street_number;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }
}
