package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ApplicationLayer.Error.DbError;
import com.nsfl.gocrush.Utility.SQLConfig;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import java.sql.*;
import java.util.ArrayList;

public class UserSQLRepository extends UserRepository {

    private Statement stat;
    private ResultSet rs;

    public UserSQLRepository()  {
        try {
            SQLConfig config = SQLConfig.getInstance();
            System.out.println("Database connection established");
            stat = config.con.createStatement();

        } catch (Exception | DbError e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public NormalUser addUser(NormalUser user) throws DbError {
        try {

            String insert = "INSERT INTO user(appUserID,fbToken) VALUES('" + user.getAppUserID() + "','" + user.getFbToken() + "')";
            stat.executeUpdate(insert);

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }

        return user;
    }

    @Override
    public NormalUser updateUserFbToken(NormalUser user) throws DbError {
        try {

            String query = "UPDATE user SET fbToken = '" + user.getFbToken() + "' WHERE appUserID = '" + user.getAppUserID() + "';";
            stat.executeUpdate(query);

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }

        return user;
    }

    public NormalUser setUserFbUserID(NormalUser user) throws DbError {
        try {

            String query = "UPDATE user SET fbUserID = '" + user.getFbUserID() + "' WHERE appUserID = '" + user.getAppUserID() + "';";
            stat.executeUpdate(query);

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }

        return user;
    }

    @Override
    public ArrayList<NormalUser> getUsers() throws DbError {
        try {
            String query = "SELECT * FROM user";
            rs = stat.executeQuery(query);
            ArrayList<NormalUser> users = new ArrayList<>();
            while (rs.next()) {

                String appUserID = rs.getString("appUserID");
                String fbUserID = rs.getString("fbUserID");
                String fbToken = rs.getString("fbToken");
                NormalUser user = new NormalUser(appUserID, fbUserID, fbToken);
                users.add(user);

            }
            return users;

        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }

    }

    @Override
    public NormalUser getUserByAppID(String id) throws DbError {
        try {

            String query = "SELECT * FROM user WHERE appUserID ='" + id + "'";
            rs = stat.executeQuery(query);

            while (rs.next()) {

                String appUserID = rs.getString("appUserID");
                String fbUserID = rs.getString("fbUserID");
                String fbToken = rs.getString("fbToken");
                return new NormalUser(appUserID, fbUserID, fbToken);

            }
        } catch (Exception e) {
            throw new DbError(e.getMessage());
        }

        return null;

    }

}
