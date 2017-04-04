
package com.nsfl.gocrush.ApplicationLayer.Error;


public class AuthorizationError extends Error {

    public AuthorizationError() {
        super("Unauthorized to perform this action", "ID_NOT_JWT", "Unauthorized to perform this action");
    }
       
}
