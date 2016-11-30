package fr.wildcodeschool.apprenti.sportihome;

import java.util.Date;

/**
 * Created by chantome on 23/11/2016.
 */

public class PlaceModel {
    private String _id;
    private boolean isActive=false;
    private User owner;
    private String engagement;
    private String hobbies;
    private Date creation;
    private Date modification;
    private String name;
    private String about;
    private String[] pictures;
    private float latitude;
    private float longitude;
    private AddressModel address;
    private HomeModel home;
    private Rate rating;
    private PlaceComment[] comments;

}

