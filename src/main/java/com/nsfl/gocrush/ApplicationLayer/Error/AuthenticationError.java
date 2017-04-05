package com.nsfl.gocrush.ApplicationLayer.Error;

public class AuthenticationError extends Error {

    public AuthenticationError(String message) {
        super(message, "BAD_JWT", "Unauthenticated user");
    }

}
