package com.nsfl.gocrush.ApplicationLayer.Command;

import com.google.gson.Gson;
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

    public HomepageDataCommand(String token, Authentication auth, HTTPRequest httpRequest, Gson gson) {
        this.gson = gson;
        this.httpRequest = httpRequest;
        this.token = token;
        this.auth = auth;
    }

    public String excute() throws UnsupportedEncodingException {
        System.out.println("ay 7kaya");
        ArrayList<String> homepageData = new ArrayList();
        String userID = auth.decode(token);
        String token = "EAACEdEose0cBAIucTRhLRZBqVSMA0QFBPlNgNq7PkDZBuIZBcoK8sRTfBub5hFNJTa24cZArMMaZAWYSSZBlFyHuHqe25rImx3qFA7YZAbqSAZCqaHu4Grf04BhRRQ33sHfOPPkq3wJPOTcvvgb5c4NykYejeZASZB6wDzyZA6cPWcUskWoPxWfqdDBiccIZCZBJ1S8gZD";
        FacebookClient facebookClient = new DefaultFacebookClient(token, Version.LATEST);
        User user = facebookClient.fetchObject("me", User.class);
        System.out.println(user.getName());
        homepageData.add(user.getName());
        homepageData.add("Photo");
        return gson.toJson(homepageData);
    }
}
