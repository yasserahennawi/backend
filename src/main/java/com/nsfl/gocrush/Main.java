package com.nsfl.gocrush;

import com.nsfl.gocrush.DBLayer.CrushSQLRepositry;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.User;

public class Main {

    public static void main(String[] args) {

        UserSQLRepository sqlUser = new UserSQLRepository();
        sqlUser.getUsers();
        sqlUser.getUserById("omaraya13");
        User nora = new User("nora@gmail.com", "nora@gmail3y434");
        //  sqlUser.addUser(nora);

        CrushSQLRepositry sqlCrush = new CrushSQLRepositry();
        Crush soso = new Crush("omaraya20", "soso@yahoo.com");
        //  sqlCrush.addCrush(soso);
        sqlCrush.getCrushesByUserID("omaraya11");
        sqlCrush.getNumberOfCrushesByUserID("omaraya11");
    }
}
