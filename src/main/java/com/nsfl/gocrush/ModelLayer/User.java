package com.nsfl.gocrush.ModelLayer;

public abstract class User {

    private String userID;
    private String fbToken;

    public User(String userID, String fbToken) {
        this.userID = userID;
        this.fbToken = fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }
    

    public String getUserID() {
        return userID;
    }

    public String getFbToken() {
        return fbToken;
    }

}
