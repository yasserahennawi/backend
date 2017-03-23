package com.nsfl.gocrush.ApplicationLayer.Command;

import com.nsfl.gocrush.Utility.AccessToken;
import com.nsfl.gocrush.Utility.HTTPRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Input.UserInput;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.nsfl.gocrush.Utility.Authentication;
import com.restfb.*;
import com.restfb.types.*;
import java.util.ArrayList;

public class AccessTokenCommand {

    // TODO move to App config
    private String code;
    private String appId = "1358490720863885";
    private String domain = "http://localhost:4567/api/fb-redirect";
    private String scope = "email";
    private String appSecret = "843b37358ee826856b240a40c9cc7e94";
    private HTTPRequest httpRequest;
    private Gson gson;
    private Authentication auth;
    private UserSQLRepository sqlUser;

    public AccessTokenCommand(String code, HTTPRequest httpRequest, Gson gson, Authentication auth, UserSQLRepository sqlUser) {
        this.sqlUser = sqlUser;
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

    public NormalUser RegisterUser() throws Exception {

        String fbToken = this.getAccessToken();
        FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
        User user = facebookClient.fetchObject("me", User.class);
        String userID = user.getId();
        NormalUser normalUser = sqlUser.getUserById(userID);
        if (normalUser == null) {
            NormalUser newUser = new NormalUser(userID, fbToken);
            return sqlUser.addUser(newUser);
        } else {
            normalUser.setFbToken(fbToken);
            return sqlUser.updateUser(normalUser);
        }
    }

    public String excute() throws Exception {
        NormalUser user = this.RegisterUser();
        System.out.println(user.getFbToken());
        UserInput userInput = new UserInput(user.getUserID());
        return auth.getToken(userInput);
    }

}
