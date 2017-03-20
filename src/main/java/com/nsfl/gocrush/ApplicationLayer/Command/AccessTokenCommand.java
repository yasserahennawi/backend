package com.nsfl.gocrush.ApplicationLayer.Command;

import com.nsfl.gocrush.Utility.AccessToken;
import com.nsfl.gocrush.Utility.HTTPRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Input.UserInput;
import com.nsfl.gocrush.Utility.Authentication;
import com.restfb.*;
import com.restfb.types.*;
import java.util.ArrayList;

public class AccessTokenCommand {

    private String code;
    private String appId = "1358490720863885";
    private String domain = "http://localhost:4567/api/fb-redirect";
    private String scope = "email";
    private String appSecret = "843b37358ee826856b240a40c9cc7e94";
    private HTTPRequest httpRequest;
    private Gson gson;
    private Authentication auth;

    public AccessTokenCommand(String code, HTTPRequest httpRequest, Gson gson, Authentication auth) {
        this.auth = auth;
        this.gson = gson;
        this.httpRequest = httpRequest;
        this.code = code;
    }

    private String getAccessToken() throws Exception {
        String accessTokenRequest = "https://graph.facebook.com/v2.8/oauth/access_token?client_id=" + appId + "&redirect_uri=" + domain + "&client_secret=" + appSecret + "&code=" + code;
        String resBody = httpRequest.sendGet(accessTokenRequest);
        AccessToken accessToken = gson.fromJson(resBody, AccessToken.class);
        return accessToken.getAccess_token();
    }

    public String excute() throws Exception {
        ArrayList<String> data = new ArrayList();
        String fbToken = this.getAccessToken();
        FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
        User user = facebookClient.fetchObject("me", User.class);
        UserInput userInput = new UserInput(user.getId());
        System.out.println(fbToken);
        // TODO sava ID and fbToken in database
        data.add(auth.getToken(userInput));
        return auth.getToken(userInput);
    }

}
