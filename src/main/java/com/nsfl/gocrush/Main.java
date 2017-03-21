package com.nsfl.gocrush;

import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;

public class Main {

    public static void main(String[] args) {

        UserSQLRepository sqlUser = new UserSQLRepository();
        sqlUser.getUsers();
        sqlUser.getUserById("omaraya13");
        NormalUser nora = new NormalUser("nora@gmail.com", "nora@gmail3y434");
//        sqlUser.addUser(nora);
        NormalUser Khaled = new NormalUser("Khaled43535", "Khaled@gmail.com");
//        sqlUser.addUser(Khaled);

        CrushSQLRepository sqlCrush = new CrushSQLRepository();
        Crush soso = new Crush("omaraya20", "soso@yahoo.com");
        //  sqlCrush.addCrush(soso);
        sqlCrush.getCrushesByUserID("omaraya11");
        sqlCrush.getNumberOfCrushesByUserID("omaraya11");
    }
}
