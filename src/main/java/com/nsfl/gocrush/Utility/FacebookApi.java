package com.nsfl.gocrush.Utility;

import com.google.gson.Gson;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.types.User;

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

    public String getUserData(NormalUser normalUser) {

        try {
            FacebookClient facebookClient = new DefaultFacebookClient(normalUser.getFbToken(), Version.LATEST);
            JsonObject picture = facebookClient.fetchObject("me/picture", JsonObject.class, Parameter.with("height", "500"), Parameter.with("width", "500"), Parameter.with("redirect", "false"));
            User user = facebookClient.fetchObject("me", User.class);
            String userData = "{\"displayName\": \"" + user.getName() + "\", \"pictureUrl\": \"" + picture.getJsonObject("data").getString("url") + "\", \"appUserID\": \"" + user.getId()+ "\"}";
            return userData;
        } catch (Exception e) {
            //Throws exception when token expired
            return null;
        }

    }

}
