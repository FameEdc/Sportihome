package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by Alban on 24/11/16.
 */

public class UserModel {
    private String _id;
    private String email;
    private int __v;
    private CommentModel[] comments;
    private boolean isAdmin;
    private RateModel rating;
    private String[] hobbies;
    private String engagement;
    private IdentityModel identity;
    private FacebookModel facebook;
    private boolean isValidate;
    private String creation;

    public UserModel() {
    }

    public UserModel(String _id, String email, int __v, CommentModel[] comments, boolean isAdmin, RateModel rating, String[] hobbies, String engagement, IdentityModel identity, FacebookModel facebook, boolean isValidate, String creation) {
        set_id(_id);
        setEmail(email);
        set__v(__v);
        setComments(comments);
        setAdmin(isAdmin);
        setRating(rating);
        setHobbies(hobbies);
        setEngagement(engagement);
        setIdentity(identity);
        setFacebook(facebook);
        setValidate(isValidate);
        setCreation(creation);
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

    public CommentModel[] getComments() {
        return comments;
    }

    public void setComments(CommentModel[] comments) {
        this.comments = comments;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public RateModel getRating() {
        return rating;
    }

    public void setRating(RateModel rating) {
        this.rating = rating;
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
}
