package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.Crush;
import java.sql.*;

public abstract class CrushRepository {

    protected Connection con;
    protected Statement stat;
    protected ResultSet rs;

    public CrushRepository() {
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

    public abstract void addCrush(Crush crush);
    public abstract void getCrushesByUserID(String id);
    public abstract void getNumberOfCrushesByUserID(String id);

}
