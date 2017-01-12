package fr.wildcodeschool.apprenti.sportihome.Model;


import java.util.ArrayList;

import fr.wildcodeschool.apprenti.sportihome.Model.AddressModel;
import fr.wildcodeschool.apprenti.sportihome.Model.OwnerModel;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotCommentModel;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotRatingModel;

/**
 * Created by Alban on 24/11/16.
 * This is the Spot Model
 */

public class SpotModel {

    private String _id;
    private OwnerModel owner;
    private String hobby;
    private String name;
    private String about;
    private int __v;
    private String modification;
    private ArrayList<SpotCommentModel> comments;
    private SpotRatingModel rating;
    private AddressModel address;
    private LocModel loc;
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

    public ArrayList<SpotCommentModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<SpotCommentModel> comments) {
        this.comments = comments;
    }

    public SpotRatingModel getRating() {
        return rating;
    }

    public void setRating(SpotRatingModel rating) {
        this.rating = rating;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public LocModel getLoc() {
        return loc;
    }

    public void setLoc(LocModel loc) {
        this.loc = loc;
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

    public String getFirstPicture(){
        return pictures[0];
    }

}
