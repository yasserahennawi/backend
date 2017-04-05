package com.nsfl.gocrush.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsfl.gocrush.ApplicationLayer.Error.AuthenticationError;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authentication {

    private String secretKey;

    public Authentication(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getJwtToken(String appUserID) throws AuthenticationError {

        try {
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("appUserID", appUserID)
                    .sign(Algorithm.HMAC256(this.secretKey));
            return token;
        } catch (JWTCreationException | IllegalArgumentException | UnsupportedEncodingException exception) {
            throw new AuthenticationError(exception.getMessage());
        }
    }

    public void verifyJwtToken(String token) throws AuthenticationError {

        if (token != null) {
            token = token.replace("Bearer ", "");
            try {
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.secretKey))
                        .withIssuer("auth0")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);

            } catch (JWTVerificationException exception) {
                throw new AuthenticationError("Invalid signature/claims");
            } catch (IllegalArgumentException | UnsupportedEncodingException exception) {
                throw new AuthenticationError(exception.getMessage());
            }
        } else {
            throw new AuthenticationError("Token is null");
        }
    }

    public String getUserAppId(String jwtToken) throws AuthenticationError {

        try {
            JWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim("appUserID").asString();
        } catch (JWTDecodeException exception) {
            throw new AuthenticationError(exception.getMessage());
        }
    }
}
