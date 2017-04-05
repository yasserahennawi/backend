
package com.nsfl.gocrush.ApplicationLayer.Error;


public class FbError extends Error {
    
    public FbError(String message) {
        super(message, "FB_ERROR", "Something went wrong");
    }
    
}
