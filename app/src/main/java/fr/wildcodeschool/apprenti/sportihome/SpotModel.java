package fr.wildcodeschool.apprenti.sportihome;


import java.lang.reflect.Array;

import static android.R.attr.rating;


/**
 * Created by Alban on 24/11/16.
 * This is the Spot Model
 */

public class SpotModel {

    private String _id;
    private OwnerModel owner;
    private String hobby;
    private String name;
    private double latitude;
    private double longitude;
    private String about;
    private int __v;
    private String modification;
    private CommentModel[] comments;
    private RateModel rating;
    private AddressModel address;
    private String pictures[];
    private String creation;

    public SpotModel(){}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public OwnerModel getOwner() {
        return owner;
    }

    public void setOwner(OwnerModel owner) {
        this.owner = owner;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public CommentModel[] getComments() {
        return comments;
    }

    public void setComments(CommentModel[] comments) {
        this.comments = comments;
    }

    public RateModel getRating() {
        return rating;
    }

    public void setRating(RateModel rating) {
        this.rating = rating;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }
}
