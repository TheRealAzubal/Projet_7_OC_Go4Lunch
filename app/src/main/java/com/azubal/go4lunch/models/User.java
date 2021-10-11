package com.azubal.go4lunch.models;

import androidx.annotation.Nullable;

import java.util.Map;

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String email;
    Map<String, Restaurant> listRestaurant;

    public User(String uid, String username, @Nullable String urlPicture,String email,Map<String, Restaurant> listRestaurant) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.email = email;
        this.listRestaurant = listRestaurant;
    }

    public User() {

    }


    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getEmail() { return email; }
    public Map<String, Restaurant> getListRestaurant(){ return listRestaurant;}

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setEmail(String email) { this.email = email; }
    public void setListRestaurant(Map<String, Restaurant> listRestaurant) { this.listRestaurant = listRestaurant; }
}
