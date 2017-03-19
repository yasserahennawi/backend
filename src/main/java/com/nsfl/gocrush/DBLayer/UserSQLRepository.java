package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.User;

public class UserSQLRepository extends UserRepository {


    @Override
    public void addUser(User user) {
        try {
            String userID = user.getUserID();
            String fbToken = user.getFbToken();
            String insert = "insert into user(userID,fbToken) values('" + userID + "','" + fbToken + "')";
            stat.executeUpdate(insert);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getUsers() {
        try {
            String query = "select * from user";
            rs = stat.executeQuery(query);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String fbToken = rs.getString("fbToken");
                System.out.println("User ID: " + userID + "   " + "FB Token: " + fbToken);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getUserById(String id) {
        try {

            String query = "select userID,fbToken from user where userID ='" + id + "'";
            rs = stat.executeQuery(query);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String fbToken = rs.getString("fbToken");
                System.out.println("User ID: " + userID + "   " + "FB Token: " + fbToken);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
