package com.nsfl.gocrush;

import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;

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

//        UserSQLRepository sqlUser = new UserSQLRepository();
//        sqlUser.getUsers();
//        sqlUser.getUserById("omaraya13");
//        NormalUser nora = new NormalUser("nora@gmail.com", "nora@gmail3y434");
//        //        sqlUser.addUser(nora);
//        NormalUser Khaled = new NormalUser("Khaled43535", "Khaled@gmail.com");
//        //        sqlUser.addUser(Khaled);
//
//        CrushSQLRepository sqlCrush = new CrushSQLRepository();
//        Crush soso = new Crush("omaraya20", "soso@yahoo.com");
//        //  sqlCrush.addCrush(soso);
//        sqlCrush.getCrushesByUserID("omaraya11");
//        sqlCrush.getNumberOfCrushesByUserID("omaraya11");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Authentication auth = new Authentication("secret");
        HTTPRequest httpRequest = new HTTPRequest();
        UserSQLRepository sqlUser = new UserSQLRepository();
        CrushSQLRepository sqlCrush = new CrushSQLRepository();

        get("/api/users/session", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                HomepageDataCommand homepageDataCommand = new HomepageDataCommand(token, auth, httpRequest, gson, sqlUser);
                return homepageDataCommand.excute();
            } else {

                return null;
            }
        });

        get("/", (req, res) -> {

            return "Hello World!";

        });

        get("/api/users/login", (req, res) -> {

            AuthenticateWithFacebookCommand authFacebook = new AuthenticateWithFacebookCommand();
            res.redirect(authFacebook.execute());
            return null;

        });

        get("/api/fb-redirect", (req, res) -> {
            // TODO rename/change Command classes
            AccessTokenCommand accessToken = new AccessTokenCommand(req.queryParams("code"), httpRequest, gson, auth, sqlUser);
            String jwtToken = accessToken.excute();
            res.redirect("http://localhost:4567?token=" + jwtToken);
            return null;
        });;

        get("/api/users/crushes/number", (req, res) -> {
            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                String userID = auth.getUserID(token);
                System.out.println(userID);
                return "User " + userID + "'s Crushes are " + sqlCrush.getNumberOfCrushesByUserID(userID);
            } else {
                return "401 Unauthorized";
            }
        });

        get("/api/users/crushes", (req, res) -> {
            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                String userID = auth.getUserID(token);
                return "User " + userID + "'s Crushes are";
            } else {
                return "401 Unauthorized";
            }
        });

        post("/api/users/crushes", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                UserInput userInput = gson.fromJson(req.body(), UserInput.class);
                return "User " + auth.getUserID(token) + " crushed on " + userInput.getCrushID();
            } else {
                return "401 Unauthorized";
            }
        });

        delete("/api/users/crushes/:crushID", (req, res) -> {

            String token = req.headers("Authorization");
            if (token != null && auth.verifyToken(token)) {
                return "User " + auth.getUserID(token) + " deleted his/her crush " + req.params(":crushID");
            } else {
                return "401 Unauthorized";
            }
        });
    }

}
