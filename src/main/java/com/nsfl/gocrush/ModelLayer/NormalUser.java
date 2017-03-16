package com.nsfl.gocrush.ModelLayer;

import java.util.Date;

public class NormalUser extends User {

    private Date createdAt;
    private Date lastLogIn;

    public NormalUser(String userID, String fbToken) {
        super(userID, fbToken);
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

}
