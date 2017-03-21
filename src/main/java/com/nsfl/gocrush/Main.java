package com.nsfl.gocrush;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Command.AccessTokenCommand;
import com.nsfl.gocrush.ApplicationLayer.Command.HomepageDataCommand;
import com.nsfl.gocrush.ApplicationLayer.Command.AuthenticateWithFacebookCommand;
import com.nsfl.gocrush.ApplicationLayer.Input.UserInput;
import com.nsfl.gocrush.Utility.Authentication;
import com.nsfl.gocrush.Utility.HTTPRequest;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class Main {


    public static void main(String[] args) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Authentication auth = new Authentication("secret");
        HTTPRequest httpRequest = new HTTPRequest();

        get("/api/users/session", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                HomepageDataCommand homepageDataCommand = new HomepageDataCommand(token, auth, httpRequest, gson);
                return homepageDataCommand.excute();
            } else {

                return null;
            }
        });
  
          get("/", (req, res) -> {

            return "Hello World!";

        });

        get("/api/users/login", (req, res) -> {
            // TODO I assumed somewhere in login procedure we will return the token for the user
            //UserInput userInput = gson.fromJson(req.body(), UserInput.class);
            //String token = auth.getToken(userInput);
            // TODO token returns in header
            AuthenticateWithFacebookCommand authFacebook = new AuthenticateWithFacebookCommand();
            res.redirect(authFacebook.execute());
            return null;

        });

        get("/api/fb-redirect", (req, res) -> {
            AccessTokenCommand accessToken = new AccessTokenCommand(req.queryParams("code"), httpRequest, gson, auth);
            res.redirect("http://localhost:4567?token=" + accessToken.excute());
            return null;
        });;

        get("/api/users/crushes", (req, res) -> {
            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                return "User " + req.params(auth.decode(token)) + "'s Crushes are";
            } else {
                return "401 Unauthorized";
            }
        });

        post("/api/users/crushes", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                UserInput userInput = gson.fromJson(req.body(), UserInput.class);
                return "User " + req.params(auth.decode(token)) + " crushed on " + userInput.getCrushID();
            } else {
                return "401 Unauthorized";
            }
        });

        delete("/api/users/crushes/:crushID", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                return "User " + req.params(auth.decode(token)) + " deleted his/her crush " + req.params(":crushID");
            } else {
                return "401 Unauthorized";
            }
        });

    }

}
