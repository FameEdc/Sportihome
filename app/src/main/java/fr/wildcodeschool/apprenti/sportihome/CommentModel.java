package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 23/11/2016.
 */

public class CommentModel {
    private String _id;
    private String date;
    private OwnerModel owner;
    private String comment;
    private int cleanness;
    private int location;
    private int valueForMoney;

    public CommentModel(){}

    public CommentModel(String _id, String date, OwnerModel owner, String comment, int cleanness, int location, int valueForMoney) {
        set_id(_id);
        setDate(date);
        setOwner(owner);
        setComment(comment);
        setCleanness(cleanness);
        setLocation(location);
        setValueForMoney(valueForMoney);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OwnerModel getOwner() {
        return owner;
    }

    public void setOwner(OwnerModel owner) {
        this.owner = owner;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
