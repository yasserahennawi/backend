package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.Crush;
import java.sql.*;

public class CrushSQLRepositry extends CrushRepository {

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
    public void getCrushesByUserID(String id) {
        try {
            String query = "select * from crush where userID='" + id + "'";
            rs = stat.executeQuery(query);
            while (rs.next()) {
                String userID = rs.getString("userID");
                String crushID = rs.getString("crushID");
                System.out.println("User ID: " + userID + "   " + "Crush ID: " + crushID);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getNumberOfCrushesByUserID(String id) {
        try {
            String query = "select count(crushID) from crush where userID='" + id + "'";
            rs = stat.executeQuery(query);
            while (rs.next()) {
                int crushes = rs.getInt("count(crushID)");
                System.out.println("Number of "+id+"'s "+"crushes= "+crushes);
                
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
