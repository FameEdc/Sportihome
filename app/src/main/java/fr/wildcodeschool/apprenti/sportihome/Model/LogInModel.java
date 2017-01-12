package fr.wildcodeschool.apprenti.sportihome.Model;

import java.io.Serializable;

/**
 * Created by chantome on 11/01/2017.
 */

public class LogInModel implements Serializable {

    private boolean success;
    private OwnerModel user;
    private String token;

    public LogInModel() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public OwnerModel getUser() {
        return user;
    }

    public void setUser(OwnerModel user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
