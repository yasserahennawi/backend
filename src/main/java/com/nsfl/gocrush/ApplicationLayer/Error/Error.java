package com.nsfl.gocrush.ApplicationLayer.Error;

public abstract class Error extends Throwable {

    private String message;
    private String code;
    private String details;

    public Error(String messag, String code, String details) {
        this.message = messag;
        this.code = code;
        this.details = details;
    }

    public String getMessag() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getDetails() {
        return details;
    }

}
