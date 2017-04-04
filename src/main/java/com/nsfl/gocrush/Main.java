package com.nsfl.gocrush;

import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Error.AuthenticationError;
import com.nsfl.gocrush.ApplicationLayer.Error.AuthorizationError;
import com.nsfl.gocrush.ApplicationLayer.Error.ValidationError;
import com.nsfl.gocrush.ApplicationLayer.Register.RegisterCrush;
import com.nsfl.gocrush.ApplicationLayer.Register.RegisterUser;
import com.nsfl.gocrush.ApplicationLayer.Factory.CrushFactory;
import com.nsfl.gocrush.ApplicationLayer.Factory.NormalUserFactory;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.nsfl.gocrush.Utility.Authentication;
import com.nsfl.gocrush.Utility.FacebookApi;
import com.nsfl.gocrush.Utility.HTTPRequest;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

public class Main {

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    public static void main(String[] args) {

        enableCORS("*", "*", "*");

        String backendServerUrl = "http://localhost:4567";
        String fronendServerUrl = "http://127.0.0.1:8080";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HTTPRequest httpRequest = new HTTPRequest();
        UserSQLRepository userSqlRepo = new UserSQLRepository();
        CrushSQLRepository crushSqlRepo = new CrushSQLRepository();
        NormalUserFactory normalUserFactory = new NormalUserFactory();
        CrushFactory crushFactory = new CrushFactory();
        Authentication auth = new Authentication("secret", userSqlRepo);
        FacebookApi facebookApi = new FacebookApi(httpRequest, gson, backendServerUrl);
        RegisterUser regUser = new RegisterUser(userSqlRepo, normalUserFactory);
        RegisterCrush regCrush = new RegisterCrush(userSqlRepo, crushSqlRepo, crushFactory);

        get("/", (req, res) -> {

            return "Hello World!";

        });

        get("/api/users/session", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.verifyJwtToken(jwtToken);

            if (normalUser != null) {
                String userData = facebookApi.getUserData(normalUser);

                if (userData != null) {

                    res.type("application/json");
                    return userData;

                } else {

                    res.redirect(backendServerUrl + "/api/users/login");
                    return null;

                }

            } else {
                res.status(403);
                res.body(gson.toJson(new AuthenticationError()));
                res.type("application/json");
                return res.body();
            }

        });

        get("/api/users/login", (req, res) -> {

            res.redirect(facebookApi.authenticate());
            return null;

        });

        get("/api/fb-redirect", (req, res) -> {

            String code = req.queryParams("code");
            String fbToken = facebookApi.getFbToken(code);
            String userID = facebookApi.getUserID(fbToken);
            NormalUser normalUser = regUser.registerUpdateUser(userID, fbToken);
            res.redirect(fronendServerUrl + "?token=" + auth.getJwtToken(normalUser.getAppUserID()));
            res.status(204);
            return "";

        });

        get("/api/users/:appUserID/crushes/count", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.verifyJwtToken(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    return crushSqlRepo.getNumberOfCrushesByUserAppID(normalUser.getAppUserID());
                } else {
                    res.status(401);
                    res.body(gson.toJson(new AuthorizationError()));
                    res.type("application/json");
                    return res.body();
                }

            } else {
                res.status(403);
                res.body(gson.toJson(new AuthenticationError()));
                res.type("application/json");
                return res.body();
            }
        });
        get("/api/users/:appUserID/crushes", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.verifyJwtToken(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    res.type("application/json");
                    return gson.toJson(crushSqlRepo.getCrushesByUserAppID(normalUser.getAppUserID()));
                } else {
                    res.status(401);
                    res.body(gson.toJson(new AuthorizationError()));
                    res.type("application/json");
                    return res.body();
                }

            } else {

                res.status(403);
                res.body(gson.toJson(new AuthenticationError()));
                res.type("application/json");
                return res.body();

            }
        });

        post("/api/users/:appUserID/crushes", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.verifyJwtToken(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    String crushUrl = req.body();
                    Crush crush = regCrush.register(normalUser, crushUrl);
                    
                    if (crush != null) {
                        res.type("application/json");
                        return gson.toJson(crush);
                    } else {
                        res.status(400);
                        res.body(gson.toJson(new ValidationError()));
                        res.type("application/json");
                        return res.body();
                    }

                } else {
                    res.status(401);
                    res.body(gson.toJson(new AuthorizationError()));
                    res.type("application/json");
                    return res.body();
                }

            } else {

                res.status(403);
                res.body(gson.toJson(new AuthenticationError()));
                res.type("application/json");
                return res.body();

            }
        });

        get("/api/users/:appUserID/crushes-on-me-count", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.verifyJwtToken(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    return crushSqlRepo.getNumberOfCrushesOnUser(normalUser.getAppUserID());
                } else {
                    res.status(401);
                    res.body(gson.toJson(new AuthorizationError()));
                    res.type("application/json");
                    return res.body();
                }

            } else {
                res.status(403);
                res.body(gson.toJson(new AuthenticationError()));
                res.type("application/json");
                return res.body();
            }

        });

        delete("/api/users/:appUserID/crushes/:fbCrushID", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.verifyJwtToken(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    res.type("application/json");
                    return gson.toJson(crushSqlRepo.deleteCrush(new Crush(auth.verifyJwtToken(jwtToken).getAppUserID(), req.params(":fbCrushID"))));
                } else {
                    res.status(401);
                    res.body(gson.toJson(new AuthorizationError()));
                    res.type("application/json");
                    return res.body();
                }

            } else {

                res.status(403);
                res.body(gson.toJson(new AuthenticationError()));
                res.type("application/json");
                return res.body();

            }
        });
    }

}
