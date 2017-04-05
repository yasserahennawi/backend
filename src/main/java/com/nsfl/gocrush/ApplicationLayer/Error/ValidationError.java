package com.nsfl.gocrush.ApplicationLayer.Error;

public class ValidationError extends Error {

    public ValidationError(String message) {
        super(message, "INVALID_URL", "Input date not valid");
    }

}
