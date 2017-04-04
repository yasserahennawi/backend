
package com.nsfl.gocrush.ApplicationLayer.Error;

public class AuthenticationError extends Error {

    public AuthenticationError() {
        super("Unauthenticated user", "BAD_JWT", "Unauthenticated user");
    }
   
}
