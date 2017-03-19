package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.User;
import java.sql.*;

public abstract class UserRepository {

    protected Connection con;
    protected Statement stat;
    protected ResultSet rs;

    public UserRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gocrush", "root", "NoraKhaled1932014");
            System.out.println("Database connection established");
            stat = con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public abstract void addUser(User user);
    public abstract void getUsers();
    public abstract void getUserById(String id);

}
