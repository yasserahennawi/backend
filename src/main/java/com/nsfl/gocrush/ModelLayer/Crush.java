package com.nsfl.gocrush.ModelLayer;

import java.util.Date;

public class Crush {

    private String userID;
    private String crushID;
    private Date createdAtd;

    public Crush(String userID, String crushID) {
        this.userID = userID;
        this.crushID = crushID;
    }

    public String getUserID() {
        return userID;
    }

    public String getCrushID() {
        return crushID;
    }

    public Date getCreatedAtd() {
        return createdAtd;
    }

}
