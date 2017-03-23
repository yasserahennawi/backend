package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.Utility.AppConfig;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import java.sql.*;
import java.util.ArrayList;

public class UserSQLRepository extends UserRepository {

    private Statement stat;
    private ResultSet rs;

    public UserSQLRepository() {
        try {
            AppConfig config = AppConfig.getInstance();
            System.out.println("Database connection established");
            stat = config.con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public NormalUser addUser(NormalUser user) {
        try {
            String userID = user.getUserID();
            String fbToken = user.getFbToken();
            String insert = "insert into user(userID,fbToken) values('" + userID + "','" + fbToken + "')";
            stat.executeUpdate(insert);

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    @Override
    public NormalUser updateUser(NormalUser user) {
        try {
            String userID = user.getUserID();
            String fbToken = user.getFbToken();
            String query = "UPDATE user SET fbToken = '" + fbToken + "' WHERE userID = '" + userID + "';";
            stat.executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    @Override
    public ArrayList<NormalUser> getUsers() {
        try {
            String query = "select * from user";
            rs = stat.executeQuery(query);
            ArrayList<NormalUser> users = new ArrayList<>();
            while (rs.next()) {
                String userID = rs.getString("userID");
                String fbToken = rs.getString("fbToken");
                NormalUser user = new NormalUser(userID, fbToken);
                users.add(user);
                System.out.println(user.getUserID() + "  " + user.getFbToken());
            }
            return users;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public NormalUser getUserById(String id) {
        try {
            String query = "select userID,fbToken from user where userID ='" + id + "'";
            rs = stat.executeQuery(query);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String fbToken = rs.getString("fbToken");
                NormalUser user = new NormalUser(userID, fbToken);
                System.out.println(user.getUserID() + "  " + user.getFbToken());
                return user;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
