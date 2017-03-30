package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.Utility.SQLConfig;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import java.sql.*;
import java.util.ArrayList;

public class UserSQLRepository extends UserRepository {

    private Statement stat;
    private ResultSet rs;

    public UserSQLRepository() {
        try {
            SQLConfig config = SQLConfig.getInstance();
            System.out.println("Database connection established");
            stat = config.con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public NormalUser addUser(NormalUser user) {
        try {

            String insert = "INSERT INTO user(appUserID,fbToken) VALUES('" + user.getAppUserID() + "','" + user.getFbToken() + "')";
            stat.executeUpdate(insert);

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    @Override
    public NormalUser updateUserFbToken(NormalUser user) {
        try {

            String query = "UPDATE user SET fbToken = '" + user.getFbToken() + "' WHERE appUserID = '" + user.getAppUserID() + "';";
            stat.executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    public NormalUser setUserFbUserID(NormalUser user) {
        try {

            String query = "UPDATE user SET fbUserID = '" + user.getFbUserID() + "' WHERE appUserID = '" + user.getAppUserID() + "';";
            stat.executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    @Override
    public ArrayList<NormalUser> getUsers() {
        try {
            String query = "SELECT * FROM user";
            rs = stat.executeQuery(query);
            ArrayList<NormalUser> users = new ArrayList<>();
            while (rs.next()) {

                String appUserID = rs.getString("appUserID");
                String fbToken = rs.getString("fbToken");
                NormalUser user = new NormalUser(appUserID, fbToken);
                users.add(user);

            }
            return users;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public NormalUser getUserByAppID(String id) {
        try {

            String query = "SELECT appUserID,fbToken FROM user WHERE appUserID ='" + id + "'";
            rs = stat.executeQuery(query);

            while (rs.next()) {

                String appUserID = rs.getString("appUserID");
                String fbToken = rs.getString("fbToken");
                return new NormalUser(appUserID, fbToken);

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;

    }

}
