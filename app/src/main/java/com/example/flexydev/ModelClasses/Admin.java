package com.example.flexydev.ModelClasses;

public class Admin {

    private String id;
    private String firebaseId;
    private String googleId;
    private String name;
    private String email;
    private int loginType;

    public Admin(){}

    public Admin(String id, String firebaseId, String googleId, String name, String email, int loginType) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.googleId = googleId;
        this.name = name;
        this.email = email;
        this.loginType = loginType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
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

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }
}
