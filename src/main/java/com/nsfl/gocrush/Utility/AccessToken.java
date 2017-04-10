package com.nsfl.gocrush.Utility;

public class AccessToken {

    private String access_token;
    private String token_type;
    private String expiry_in;

    public AccessToken(String accessToken, String tokenType, String expiryDate) {
        this.access_token = accessToken;
        this.token_type = tokenType;
        this.expiry_in = expiryDate;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getExpiryDate() {
        return expiry_in;
    }
}
