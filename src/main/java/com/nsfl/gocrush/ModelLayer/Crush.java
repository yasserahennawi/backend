package com.nsfl.gocrush.ModelLayer;

import java.util.Date;

public class Crush {

    private String appUserID;
    private String fbCrushID;
    private Date createdAt;

    public Crush(String appUserID, String fbCrushID) {
        this.appUserID = appUserID;
        this.fbCrushID = fbCrushID;
    }
    
    public Crush(String appUserID, String fbCrushID, Date createdAtd) {
        this.appUserID = appUserID;
        this.fbCrushID = fbCrushID;
        this.createdAt = createdAtd;
    }

    public String getAppUserID() {
        return appUserID;
    }

    public String getfbCrushID() {
        return fbCrushID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
