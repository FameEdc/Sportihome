package fr.wildcodeschool.apprenti.sportihome.Model;

/**
 * Created by chantome on 23/11/2016.
 */

public class OwnerModel {
    private String _id;
    private String email;
    private int __v;
    private String avatar;
    //private CommentModel[] comments;
    private boolean isAdmin;
    //private RateModel rating;
    private String[] hobbies;
    private String engagement;
    private IdentityModel identity;
    private FacebookModel facebook;
    private GoogleModel google;
    private boolean isValidate;
    private String creation;

    public OwnerModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public String getEngagement() {
        return engagement;
    }

    public void setEngagement(String engagement) {
        this.engagement = engagement;
    }

    public IdentityModel getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityModel identity) {
        this.identity = identity;
    }

    public FacebookModel getFacebook() {
        return facebook;
    }

    public void setFacebook(FacebookModel facebook) {
        this.facebook = facebook;
    }

    public boolean isValidate() {
        return isValidate;
    }

    public void setValidate(boolean validate) {
        isValidate = validate;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public GoogleModel getGoogle() {
        return google;
    }

    public void setGoogle(GoogleModel google) {
        this.google = google;
    }
}
