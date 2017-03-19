package com.nsfl.gocrush.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsfl.gocrush.ApplicationLayer.Input.UserInput;
import java.io.UnsupportedEncodingException;

public class Authentication {

    private String secretKey;

    public Authentication(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken(UserInput userInput) throws UnsupportedEncodingException {
        String token = "";
        try {
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userID", userInput.getUserID())
                    .sign(Algorithm.HMAC256(this.secretKey));
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return "Bearer " + token;
    }

    public boolean verifyToken(String token) throws UnsupportedEncodingException {
        token = token.replace("Bearer ", "");
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.secretKey))
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            return false;
        }
    }

}
