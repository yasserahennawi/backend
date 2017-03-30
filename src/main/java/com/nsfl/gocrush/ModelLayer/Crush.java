package com.nsfl.gocrush.ModelLayer;

import java.util.Date;

public class Crush {

    private String appUserID;
    private String fbCrushID;
    private Date createdAtd;

    public Crush(String appUserID, String fbCrushID) {
        this.appUserID = appUserID;
        this.fbCrushID = fbCrushID;
    }

    public String getAppUserID() {
        return appUserID;
    }

    public String getfbCrushID() {
        return fbCrushID;
    }

    public Date getCreatedAtd() {
        return createdAtd;
    }

}
