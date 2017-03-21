package com.nsfl.gocrush.ApplicationLayer.Command;

import com.google.gson.Gson;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.nsfl.gocrush.Utility.Authentication;
import com.nsfl.gocrush.Utility.HTTPRequest;
import com.restfb.*;
import com.restfb.types.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HomepageDataCommand {

    private String token;
    private Authentication auth;
    private HTTPRequest httpRequest;
    private Gson gson;
    private UserSQLRepository sqlUser;

    public HomepageDataCommand(String token, Authentication auth, HTTPRequest httpRequest, Gson gson, UserSQLRepository sqlUser) {
        this.sqlUser = sqlUser;
        this.gson = gson;
        this.httpRequest = httpRequest;
        this.token = token;
        this.auth = auth;
    }

    public String excute() throws UnsupportedEncodingException {
        ArrayList<String> homepageData = new ArrayList();
        String fbToken = this.sqlUser.getUserById(auth.getUserID(token)).getFbToken();
        // TODO validate facebook token isn't expired
        FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
        User user = facebookClient.fetchObject("me", User.class);
        System.out.println(user.getName());
        homepageData.add(user.getName());
        homepageData.add("Photo");
        // TODO return data as key value Json not just array 
        return gson.toJson(homepageData);
    }
}
