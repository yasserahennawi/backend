/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsfl.gocrush.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lenovo
 */
public class AppConfig {

    public Connection con;
    private static AppConfig instance;

    private AppConfig() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gocrush", "root", "NoraKhaled1932014");

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());

        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {

            instance = new AppConfig();

        }

        return instance;

    }
}
