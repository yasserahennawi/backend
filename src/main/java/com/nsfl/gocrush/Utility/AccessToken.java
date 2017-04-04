package com.nsfl.gocrush.Utility;

public class AccessToken {

    private String access_token;
    private String tokenType;
    private String expiryDate;

    public AccessToken(String accessToken, String tokenType, String expiryDate) {
        this.access_token = accessToken;
        this.tokenType = tokenType;
        this.expiryDate = expiryDate;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
