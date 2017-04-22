package com.nsfl.gocrush.Utility;

import com.google.gson.Gson;
import com.nsfl.gocrush.ApplicationLayer.Error.FbError;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class FacebookApi {

    private String appId = "1358490720863885";
    private String domain;
    private String scope = "email";
    private String appSecret = "843b37358ee826856b240a40c9cc7e94";
    private String responseType = "code";

    private HTTPRequest httpRequest;
    private Gson gson;

    public FacebookApi(HTTPRequest httpRequest, Gson gson, String backendServerUrl) {

        this.domain = backendServerUrl + "/api/fb-redirect";
        this.gson = gson;
        this.httpRequest = httpRequest;

    }

    public String authenticate() {
        return "https://www.facebook.com/v2.8/dialog/oauth?client_id=" + this.appId + "&redirect_uri=" + this.domain + "&scope=" + this.scope + "&response_type=" + this.responseType;
    }

    public String getFbToken(String code) throws Exception {
        String accessTokenRequest = "https://graph.facebook.com/v2.8/oauth/access_token?client_id=" + this.appId + "&redirect_uri=" + this.domain + "&client_secret=" + this.appSecret + "&code=" + code;
        String resBody = httpRequest.sendGet(accessTokenRequest);
        AccessToken accessToken = gson.fromJson(resBody, AccessToken.class);
        return accessToken.getAccessToken();
    }

    public String getUserID(String fbToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
        User user = facebookClient.fetchObject("me", User.class);
        return user.getId();
    }

    public String getUserData(NormalUser normalUser) throws FbError {

        try {
            FacebookClient facebookClient = new DefaultFacebookClient(normalUser.getFbToken(), Version.LATEST);
            JsonObject picture = facebookClient.fetchObject("me/picture", JsonObject.class, Parameter.with("height", "500"), Parameter.with("width", "500"), Parameter.with("redirect", "false"));
            User user = facebookClient.fetchObject("me", User.class);
            String userData = "{\"displayName\": \"" + user.getName() + "\", \"pictureUrl\": \"" + picture.getJsonObject("data").getString("url") + "\", \"appUserID\": \"" + user.getId() + "\", \"fbUserID\": \"" + normalUser.getFbUserID() + "\"}";
            return userData;
        } catch (Exception e) {
            //Throws exception when token expired
            throw new FbError(e.getMessage());
        }

    }

    public String getCrushesData(ArrayList<Crush> crushes, String fbToken) throws FbError {
        try {
            String crushesData = "[";

            FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
            for (Crush crush : crushes) {
                JsonObject picture = facebookClient.fetchObject(crush.getfbCrushID() + "/picture", JsonObject.class, Parameter.with("height", "500"), Parameter.with("width", "500"), Parameter.with("redirect", "false"));
                User user = facebookClient.fetchObject(crush.getfbCrushID(), User.class);
                crushesData = crushesData + "{\"appUserID\": \"" + crush.getAppUserID() + "\", \"fbCrushID\": \"" + crush.getfbCrushID() + "\", \"crushDisplayName\": \"" + user.getName() + "\", \"crushPictureUrl\": \"" + picture.getJsonObject("data").getString("url") + "\", \"createdAt\": \"" + getIsoDate(crush.getCreatedAt()) + "\"}, ";
            }
            if (crushes.size() >= 1) {
                crushesData = crushesData.substring(0, crushesData.length() - 2);
            }
            return crushesData + " ]";
        } catch (Exception e) {
            throw new FbError(e.getMessage());
        }
    }

    public String getCrushData(Crush crush, String fbToken) throws FbError {
        try {
            FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
            JsonObject picture = facebookClient.fetchObject(crush.getfbCrushID() + "/picture", JsonObject.class, Parameter.with("height", "500"), Parameter.with("width", "500"), Parameter.with("redirect", "false"));
            User user = facebookClient.fetchObject(crush.getfbCrushID(), User.class);
            String crushData = "{\"appUserID\": \"" + crush.getAppUserID() + "\", \"fbCrushID\": \"" + crush.getfbCrushID() + "\", \"crushDisplayName\": \"" + user.getName() + "\", \"crushPictureUrl\": \"" + picture.getJsonObject("data").getString("url") + "\", \"createdAt\": \"" + getIsoDate(new Date()) + "\"}";
            return crushData;
        } catch (Exception e) {
            throw new FbError(e.getMessage());
        }

    }

    public boolean sameUser(NormalUser normalUser, String fbUserID) throws FbError {
        try {
            FacebookClient facebookClient = new DefaultFacebookClient(normalUser.getFbToken(), Version.LATEST);
            JsonObject picture1 = facebookClient.fetchObject("me/picture", JsonObject.class, Parameter.with("height", "500"), Parameter.with("width", "500"), Parameter.with("redirect", "false"));
            JsonObject picture2 = facebookClient.fetchObject(fbUserID + "/picture", JsonObject.class, Parameter.with("height", "500"), Parameter.with("width", "500"), Parameter.with("redirect", "false"));
            return picture1.getJsonObject("data").getString("url").equals(picture2.getJsonObject("data").getString("url"));
        } catch (Exception e) {
            throw new FbError(e.getMessage());
        }

    }

    private String getIsoDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }
}
