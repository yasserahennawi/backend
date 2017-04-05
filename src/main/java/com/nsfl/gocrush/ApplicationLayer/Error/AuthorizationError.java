
package com.nsfl.gocrush.ApplicationLayer.Error;


public class AuthorizationError extends Error {

    public AuthorizationError(String message) {
        super(message, "ID_NOT_JWT", "Unauthorized to perform this action");
    }
       
}
