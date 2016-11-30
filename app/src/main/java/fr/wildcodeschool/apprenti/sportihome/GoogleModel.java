package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 28/11/2016.
 */

public class GoogleModel {
    private String id;
    private String token;
    private String name;
    private String email;

    public GoogleModel(String id, String token, String name, String email) {
        setId(id);
        setToken(token);
        setName(name);
        setEmail(email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
