package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.Utility.AppConfig;
import com.nsfl.gocrush.ModelLayer.Crush;
import java.sql.*;
import java.util.ArrayList;

public class CrushSQLRepository extends CrushRepository {

    private Statement stat;
    private ResultSet rs;

    public CrushSQLRepository() {
        try {
            AppConfig config = AppConfig.getInstance();
            System.out.println("Database connection established");
            stat = config.con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void addCrush(Crush crush) {
        try {
            String userID = crush.getUserID();
            String crushID = crush.getCrushID();
            String insert = "insert into crush(userID,crushID) values('" + userID + "','" + crushID + "')";
            stat.executeUpdate(insert);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public ArrayList<Crush> getCrushesByUserID(String id) {
        try {
            String query = "select * from crush where userID='" + id + "'";
            rs = stat.executeQuery(query);
            ArrayList<Crush> crushes = new ArrayList<>();
            while (rs.next()) {
                String userID = rs.getString("userID");
                String crushID = rs.getString("crushID");
                Crush crush = new Crush(userID, crushID);
                System.out.println(crush.getCrushID());
                crushes.add(crush);
            }
            return crushes;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public int getNumberOfCrushesByUserID(String id) {
        try {
            String query = "select count(crushID) from crush where userID='" + id + "'";
            rs = stat.executeQuery(query);
            while (rs.next()) {
                int crushes = rs.getInt("count(crushID)");
                System.out.println("Number of " + id + "'s " + "crushes= " + crushes);
                return crushes;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
}
