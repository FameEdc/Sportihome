package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class PlaceModel {
    private String _id;
    private boolean isActive=false;
    private OwnerModel owner;
    private double latitude;
    private double longitude;
    private String name;
    private String about;
    private int __v;
    private String modification;
    private CommentModel[] comments;
    private RateModel rating;
    private HomeModel home;
    private AddressModel address;
    private String[] pictures;
    private String creation;
    private String[] hobbies;
    private boolean mPrivate;
    private boolean finished;
    private int step;

    public PlaceModel() {
    }

    public PlaceModel(String _id, boolean isActive, OwnerModel owner, double latitude, double longitude, String name, String about, int __v, String modification, CommentModel[] comments, RateModel rating, HomeModel home, AddressModel address, String[] pictures, String creation, String[] hobbies, boolean mPrivate, boolean finished, int step) {
        set_id(_id);
        setActive(isActive);
        setOwner(owner);
        setLatitude(latitude);
        setLongitude(longitude);
        setName(name);
        setAbout(about);
        set__v(__v);
        setModification(modification);
        setComments(comments);
        setRating(rating);
        setHome(home);
        setAddress(address);
        setPictures(pictures);
        setCreation(creation);
        setHobbies(hobbies);
        setmPrivate(mPrivate);
        setFinished(finished);
        setStep(step);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public OwnerModel getOwner() {
        return owner;
    }

    public void setOwner(OwnerModel owner) {
        this.owner = owner;
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

    public HomeModel getHome() {
        return home;
    }

    public void setHome(HomeModel home) {
        this.home = home;
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

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public boolean ismPrivate() {
        return mPrivate;
    }

    public void setmPrivate(boolean mPrivate) {
        this.mPrivate = mPrivate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getFirstPicture(){
        return pictures[0];
    }
}

