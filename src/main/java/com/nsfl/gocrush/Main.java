package com.nsfl.gocrush;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Input.UserInput;
import com.nsfl.gocrush.Utility.Authentication;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Authentication auth = new Authentication("secret");

        get("/", (req, res) -> {
            return "Hello World!";
        });

        post("/users/login", (req, res) -> {
            // TODO I assumed somewhere in login procedure we will return the token for the user
            UserInput userInput = gson.fromJson(req.body(), UserInput.class);
            String token = auth.getToken(userInput);
            // TODO token returns in header
            return "User token: " + token;
        });

        get("/users/:userID/crushes", (req, res) -> {
            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                return "User " + req.params(":userID") + "'s Crushes are";
            } else {
                return "401 Unauthorized";
            }
        });

        post("/users/:userID/crushes", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                UserInput userInput = gson.fromJson(req.body(), UserInput.class);
                return "User " + req.params(":userID") + " crushed on " + userInput.getCrushID();
            } else {
                return "401 Unauthorized";
            }
        });

        delete("/users/:userID/crushes/:crushID", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                return "User " + req.params(":userID") + " deleted his/her crush " + req.params(":crushID");
            } else {
                return "401 Unauthorized";
            }
        });

    }

}
