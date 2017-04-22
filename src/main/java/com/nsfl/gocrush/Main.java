package com.nsfl.gocrush;

import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsfl.gocrush.ApplicationLayer.Error.AuthenticationError;
import com.nsfl.gocrush.ApplicationLayer.Error.AuthorizationError;
import com.nsfl.gocrush.ApplicationLayer.Error.DbError;
import com.nsfl.gocrush.ApplicationLayer.Error.FbError;
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
import java.util.ArrayList;
import java.util.Date;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.options;
import static spark.Spark.path;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        enableCORS("*", "*", "*");
        String backendServerUrl = "http://localhost:4567";
        String fronendServerUrl = "http://127.0.0.1:1111";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HTTPRequest httpRequest = new HTTPRequest();
        UserSQLRepository userSqlRepo = new UserSQLRepository();
        CrushSQLRepository crushSqlRepo = new CrushSQLRepository();
        NormalUserFactory normalUserFactory = new NormalUserFactory();
        CrushFactory crushFactory = new CrushFactory();
        Authentication auth = new Authentication("secret");
        FacebookApi facebookApi = new FacebookApi(httpRequest, gson, backendServerUrl);
        RegisterUser regUser = new RegisterUser(userSqlRepo, normalUserFactory);
        RegisterCrush regCrush = new RegisterCrush(userSqlRepo, crushSqlRepo, crushFactory, facebookApi);

        get("/", (req, res) -> {

            return "Hello World!";

        });

        get("/api/login", (req, res) -> {
            res.redirect(facebookApi.authenticate());
            return null;
        });

        get("/api/fb-redirect", (req, res) -> {

            try {
                String code = req.queryParams("code");
                String fbToken = facebookApi.getFbToken(code);
                String userID = facebookApi.getUserID(fbToken);
                NormalUser normalUser = regUser.registerUpdateUser(userID, fbToken);
                res.redirect(fronendServerUrl + "?token=" + auth.getJwtToken(normalUser.getAppUserID()));
                res.status(204);
                return "";
            } catch (AuthenticationError | DbError ex) {
                res.status(400);
                res.type("application/json");
                return gson.toJson(ex);
            }
        });

        path("/api/users", () -> {

            before("/session", (req, res) -> {

                if (!req.requestMethod().equals("OPTIONS")) {
                    try {

                        String jwtToken = req.headers("Authorization");
                        auth.verifyJwtToken(jwtToken);
                        String appUserID = auth.getUserAppId(jwtToken);
                        req.attribute("appUserID", appUserID);
                    } catch (AuthenticationError e) {
                        halt(403, gson.toJson(e));
                    }

                }
            }
            );

            before("/:appUserID/*", (req, res) -> {

                if (!req.requestMethod().equals("OPTIONS")) {
                    try {
                        String jwtToken = req.headers("Authorization");
                        auth.verifyJwtToken(jwtToken);
                        String appUserID = auth.getUserAppId(jwtToken);
                        if (!appUserID.equals(req.params(":appUserID"))) {
                            AuthorizationError e = new AuthorizationError("This user unauthorized to this action");
                            halt(401, gson.toJson(e));
                        } else {
                            req.attribute("appUserID", appUserID);
                        }
                    } catch (AuthenticationError e) {
                        halt(403, gson.toJson(e));
                    }

                }
            }
            );

            get("/session", (req, res) -> {

                try {

                    NormalUser normalUser = userSqlRepo.getUserByAppID(req.attribute("appUserID"));
                    String userData = facebookApi.getUserData(normalUser);
                    res.type("application/json");
                    return userData;
                } catch (FbError | DbError e) {
                    res.redirect(backendServerUrl + "/api/login");
                    return null;
                }
            });

            get("/:appUserID/crushes-on-me-count", (req, res) -> {
                try {
                    return crushSqlRepo.getNumberOfCrushesOnUser(req.attribute("appUserID"));
                } catch (DbError e) {
                    res.status(400);
                    res.body(gson.toJson(e));
                    res.type("application/json");
                    return res.body();
                }

            });

            path("/:appUserID/crushes", () -> {

                get("", (req, res) -> {

                    try {
                        ArrayList<Crush> crushes = crushSqlRepo.getCrushesByUserAppID(req.attribute("appUserID"));
                        res.type("application/json");
                        return facebookApi.getCrushesData(crushes, userSqlRepo.getUserByAppID(req.attribute("appUserID")).getFbToken());
                    } catch (DbError | FbError e) {
                        res.status(400);
                        res.body(gson.toJson(e));
                        res.type("application/json");
                        return res.body();
                    }

                });

                post("", (req, res) -> {
                    try {
                        String crushUrl = req.body();
                        NormalUser normalUser = userSqlRepo.getUserByAppID(req.attribute("appUserID"));
                        String crush = regCrush.register(normalUser, crushUrl);
                        res.type("application/json");
                        return crush;
                    } catch (ValidationError | DbError ex) {
                        res.status(400);
                        res.body(gson.toJson(ex));
                        res.type("application/json");
                        return res.body();
                    }

                });

                delete("/:fbCrushID", (req, res) -> {
                    try {

                        Crush crush = new Crush(req.attribute("appUserID"), req.params(":fbCrushID"));
                        Crush savedCrush = crushSqlRepo.getCrush(crush);
                        if (savedCrush == null) {
                            throw new DbError("Crush not found");
                        }
                        if (new Date().getTime() >= savedCrush.getCreatedAt().getTime() + 172800000 || new Date().getTime() <= savedCrush.getCreatedAt().getTime() + 3600000) {
                            res.type("application/json");
                            return gson.toJson(crushSqlRepo.deleteCrush(crush));
                        } else {
                            throw new DbError("You can delete this crush on " + new Date(savedCrush.getCreatedAt().getTime() + 172800000));
                        }
                    } catch (DbError e) {
                        res.status(400);
                        res.body(gson.toJson(e));
                        res.type("application/json");
                        return res.body();
                    }

                });

                get("/count", (req, res) -> {
                    try {
                        return crushSqlRepo.getNumberOfCrushesByUserAppID(req.attribute("appUserID"));
                    } catch (DbError e) {
                        res.status(400);
                        res.body(gson.toJson(e));
                        res.type("application/json");
                        return res.body();
                    }
                });

            });

        }
        );

    }

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

}
