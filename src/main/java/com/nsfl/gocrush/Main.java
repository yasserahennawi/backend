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
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HTTPRequest httpRequest = new HTTPRequest();
        UserSQLRepository userSqlRepo = new UserSQLRepository();
        CrushSQLRepository crushSqlRepo = new CrushSQLRepository();
        NormalUserFactory normalUserFactory = new NormalUserFactory();
        CrushFactory crushFactory = new CrushFactory();
        Authentication auth = new Authentication("secret", userSqlRepo);
        FacebookConfig facebookConfig = new FacebookConfig(httpRequest, gson);
        RegisterUser regUser = new RegisterUser(userSqlRepo, normalUserFactory);
        RegisterCrush regCrush = new RegisterCrush(crushSqlRepo, crushFactory);

        get("/", (req, res) -> {

            return "Hello World!";

        });

        get("/api/users/session", (req, res) -> {

            String jwtToken = req.headers("Authorization");

            if (jwtToken != null && auth.verifyJwtToken(jwtToken)) {

                res.type("application/json");
                return facebookConfig.getUserData(auth.getUser(jwtToken));

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

            res.redirect("http://localhost:4567?token=" + auth.getJwtToken(normalUser.getUserID()));
            return null;

        });

        get("/api/users/crushes/number", (req, res) -> {

            String jwtToken = req.headers("Authorization");

            if (jwtToken != null && auth.verifyJwtToken(jwtToken)) {

                return crushSqlRepo.getNumberOfCrushesByUserID(auth.getUser(jwtToken).getUserID());

            } else {

                return "401 Unauthorized";

            }
        });

        get("/api/users/crushes", (req, res) -> {

            String jwtToken = req.headers("Authorization");

            if (jwtToken != null && auth.verifyJwtToken(jwtToken)) {

                res.type("application/json");
                return gson.toJson(crushSqlRepo.getCrushesByUserID(auth.getUser(jwtToken).getUserID()));

            } else {

                return "401 Unauthorized";

            }
        });

        post("/api/users/crushes", (req, res) -> {

            String jwtToken = req.headers("Authorization");

            if (jwtToken != null && auth.verifyJwtToken(jwtToken)) {

                Crush crush = gson.fromJson(req.body(), Crush.class);
                res.type("application/json");
                return gson.toJson(regCrush.register(auth.getUser(jwtToken).getUserID(), crush.getCrushID()));

            } else {

                return "401 Unauthorized";

            }
        });

        delete("/api/users/crushes/:crushID", (req, res) -> {

            String jwtToken = req.headers("Authorization");

            if (jwtToken != null && auth.verifyJwtToken(jwtToken)) {

                res.type("application/json");
                return gson.toJson(crushSqlRepo.deleteCrush(new Crush(auth.getUser(jwtToken).getUserID(), req.params(":crushID"))));

            } else {

                return "401 Unauthorized";

            }
        });
    }

}
