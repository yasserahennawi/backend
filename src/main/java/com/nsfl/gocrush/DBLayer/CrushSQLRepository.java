package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.Utility.SQLConfig;
import com.nsfl.gocrush.ModelLayer.Crush;
import java.sql.*;
import java.util.ArrayList;

public class CrushSQLRepository extends CrushRepository {

    private Statement stat;
    private ResultSet rs;

    public CrushSQLRepository() {
        try {
            SQLConfig config = SQLConfig.getInstance();
            System.out.println("Database connection established");
            stat = config.con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Crush addCrush(Crush crush) {
        try {

            String insert = "INSERT INTO crush(userID,crushID) VALUES('" + crush.getUserID() + "','" + crush.getCrushID() + "')";
            stat.executeUpdate(insert);
        } catch (Exception e) {
            System.out.println(e);
        }
        return crush;
    }

    public Crush deleteCrush(Crush crush) {
        try {
            String delete = "DELETE FROM crush WHERE userID = '" + crush.getUserID() + "' AND crushID = '" + crush.getCrushID() + "'";
            stat.executeUpdate(delete);
        } catch (Exception e) {
            System.out.println(e);
        }
        return crush;
    }

    @Override
    public ArrayList<Crush> getCrushesByUserID(String id) {
        try {
            String query = "SELECT * FROM crush WHERE userID='" + id + "'";
            rs = stat.executeQuery(query);
            ArrayList<Crush> crushes = new ArrayList<>();
            while (rs.next()) {
                String userID = rs.getString("userID");
                String crushID = rs.getString("crushID");
                Crush crush = new Crush(userID, crushID);
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
            String query = "SELECT COUNT(crushID) FROM crush WHERE userID='" + id + "'";
            rs = stat.executeQuery(query);
            while (rs.next()) {

                int crushes = rs.getInt("count(crushID)");
                return crushes;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
}
