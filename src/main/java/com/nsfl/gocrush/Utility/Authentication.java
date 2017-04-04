package com.nsfl.gocrush.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import java.io.UnsupportedEncodingException;

public class Authentication {

    private String secretKey;
    private UserSQLRepository userSqlRepo;

    public Authentication(String secretKey, UserSQLRepository userSqlRepo) {
        this.secretKey = secretKey;
        this.userSqlRepo = userSqlRepo;
    }

    public String getJwtToken(String appUserID) throws UnsupportedEncodingException {
        String token = "";
        try {
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("appUserID", appUserID)
                    .sign(Algorithm.HMAC256(this.secretKey));
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }

    private boolean validateSignature(String token) throws UnsupportedEncodingException {

        if (token != null) {
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
        } else {
            return false;
        }
    }

    public NormalUser verifyJwtToken(String jwtToken) throws UnsupportedEncodingException {

        if (this.validateSignature(jwtToken)) {
            try {
                JWT jwt = JWT.decode(jwtToken);
                String appUserID = jwt.getClaim("appUserID").asString();
                return userSqlRepo.getUserByAppID(appUserID);

            } catch (JWTDecodeException exception) {
                return null;
            }
        } else {
            //Authentication error
            return null;
        }

    }
}
