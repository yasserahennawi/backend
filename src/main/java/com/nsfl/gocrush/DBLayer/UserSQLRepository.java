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

            String insert = "INSERT INTO user(userID,fbToken) VALUES('" + user.getUserID() + "','" + user.getFbToken() + "')";
            stat.executeUpdate(insert);

        } catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }

    @Override
    public NormalUser updateUser(NormalUser user) {
        try {

            String query = "UPDATE user SET fbToken = '" + user.getFbToken() + "' WHERE userID = '" + user.getUserID() + "';";
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
                
                String userID = rs.getString("userID");
                String fbToken = rs.getString("fbToken");
                NormalUser user = new NormalUser(userID, fbToken);
                users.add(user);
                
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

            String query = "SELECT userID,fbToken FROM user WHERE userID ='" + id + "'";
            rs = stat.executeQuery(query);

            while (rs.next()) {

                String userID = rs.getString("userID");
                String fbToken = rs.getString("fbToken");
                return new NormalUser(userID, fbToken);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return null;
        
    }

}
