package com.nsfl.gocrush.ModelLayer;

import java.util.Date;

public class NormalUser extends User {

    private String fbUserID;
    private Date createdAt;
    private Date lastLogIn;

    public NormalUser(String appUserID, String fbToken) {
        super(appUserID, fbToken);
    }

    public NormalUser(String appUserID, String fbUserID, String fbToken) {
        super(appUserID, fbToken);
        this.fbUserID = fbUserID;
    }

    public void setFbUserID(String fbUserID) {
        this.fbUserID = fbUserID;
    }

    public void setLastLogIn(Date lastLogIn) {
        this.lastLogIn = lastLogIn;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getLastLogIn() {
        return lastLogIn;
    }

    public String getFbUserID() {
        return fbUserID;
    }

}
