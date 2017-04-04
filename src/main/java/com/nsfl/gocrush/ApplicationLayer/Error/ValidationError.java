
package com.nsfl.gocrush.ApplicationLayer.Error;

public class ValidationError extends Error {
    
    public ValidationError() {
        super("Input date not valid", "INVALID_URL", "Input date not valid");
    }
    
}
