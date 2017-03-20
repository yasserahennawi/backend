package com.nsfl.gocrush.ApplicationLayer.Command;

public class AuthenticateWithFacebookCommand {

    private String appId = "1358490720863885";
    private String domain = "http://localhost:4567/api/fb-redirect";
    private String scope = "email";
    private String responseType = "code";

    public String execute() {
        return "https://www.facebook.com/v2.8/dialog/oauth?client_id=" + appId + "&redirect_uri=" + domain + "&scope=" + scope + "&response_type=" + responseType;
    }

}
