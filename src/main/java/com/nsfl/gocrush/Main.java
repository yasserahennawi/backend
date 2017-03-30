package com.nsfl.gocrush;

import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Register.RegisterCrush;
import com.nsfl.gocrush.ApplicationLayer.Register.RegisterUser;
import com.nsfl.gocrush.ApplicationLayer.Factory.CrushFactory;
import com.nsfl.gocrush.ApplicationLayer.Factory.NormalUserFactory;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.nsfl.gocrush.Utility.Authentication;
import com.nsfl.gocrush.Utility.FacebookConfig;
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
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HTTPRequest httpRequest = new HTTPRequest();
        UserSQLRepository userSqlRepo = new UserSQLRepository();
        CrushSQLRepository crushSqlRepo = new CrushSQLRepository();
        NormalUserFactory normalUserFactory = new NormalUserFactory();
        CrushFactory crushFactory = new CrushFactory();
        Authentication auth = new Authentication("secret", userSqlRepo);
        FacebookConfig facebookConfig = new FacebookConfig(httpRequest, gson);
        RegisterUser regUser = new RegisterUser(userSqlRepo, normalUserFactory);
        RegisterCrush regCrush = new RegisterCrush(userSqlRepo, crushSqlRepo, crushFactory);

        get("/", (req, res) -> {

            return "Hello World!";

        });

        get("/api/users/session", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.getUser(jwtToken);

            if (normalUser != null) {
                String userData = facebookConfig.getUserData(normalUser);

                if (userData != null) {

                    res.type("application/json");
                    return userData;

                } else {

                    res.redirect("http://localhost:4567/api/users/login");
                    return null;

                }

            } else {
                return null;
            }

        });

        get("/api/users/login", (req, res) -> {

            res.redirect(facebookConfig.authenticate());
            return null;

        });

        get("/api/fb-redirect", (req, res) -> {

            String code = req.queryParams("code");
            String fbToken = facebookConfig.getFbToken(code);
            String userID = facebookConfig.getUserID(fbToken);

            NormalUser normalUser = regUser.registerUpdateUser(userID, fbToken);

            res.redirect("http://127.0.0.1:8080?token=" + auth.getJwtToken(normalUser.getAppUserID()));
            return null;

        });

        get("/api/users/:appUserID/crushes/count", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.getUser(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    return crushSqlRepo.getNumberOfCrushesByUserAppID(normalUser.getAppUserID());
                } else {
                    return "401 Unauthorized";
                }

            } else {
                return "401 Unauthorized";
            }
        });
        get("/api/users/:appUserID/crushes", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.getUser(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    res.type("application/json");
                    return gson.toJson(crushSqlRepo.getCrushesByUserAppID(normalUser.getAppUserID()));
                } else {
                    return "401 Unauthorized";
                }

            } else {

                return "401 Unauthorized";

            }
        });

        post("/api/users/:appUserID/crushes", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.getUser(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    String crushUrl = req.body();
                    res.type("application/json");
                    return gson.toJson(regCrush.register(normalUser, crushUrl));
                } else {
                    return "401 Unauthorized";
                }

            } else {

                return "401 Unauthorized";

            }
        });

        get("/api/users/:appUserID/crushes-on-me-count", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.getUser(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    return crushSqlRepo.getNumberOfCrushesOnUser(normalUser.getAppUserID());
                } else {
                    return "401 Unauthorized";
                }

            } else {
                return "401 Unauthorized";
            }

        });

        delete("/api/users/:appUserID/crushes/:fbCrushID", (req, res) -> {

            String jwtToken = req.headers("Authorization");
            NormalUser normalUser = auth.getUser(jwtToken);

            if (normalUser != null) {

                if (normalUser.getAppUserID().equals(req.params(":appUserID"))) {
                    res.type("application/json");
                    return gson.toJson(crushSqlRepo.deleteCrush(new Crush(auth.getUser(jwtToken).getAppUserID(), req.params(":fbCrushID"))));
                } else {
                    return "401 Unauthorized";
                }

            } else {

                return "401 Unauthorized";

            }
        });
    }

}
