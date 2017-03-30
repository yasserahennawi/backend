package com.nsfl.gocrush.ModelLayer;

public abstract class User {

    private String appUserID;
    private String fbToken;

    public User(String appUserID, String fbToken) {
        this.appUserID = appUserID;
        this.fbToken = fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }
    

    public String getAppUserID() {
        return appUserID;
    }

    public String getFbToken() {
        return fbToken;
    }

}
