package com.nsfl.gocrush.ApplicationLayer.Register;

import com.nsfl.gocrush.ApplicationLayer.Factory.CrushFactory;
import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;

public class RegisterCrush {

    private CrushSQLRepository crushSqlRepo;
    private UserSQLRepository userSqlRepo;
    private CrushFactory crushFactory;

    public RegisterCrush(UserSQLRepository userSqlRepo, CrushSQLRepository crushSqlRepo, CrushFactory crushFactory) {
        this.userSqlRepo = userSqlRepo;
        this.crushSqlRepo = crushSqlRepo;
        this.crushFactory = crushFactory;
    }

    public Crush register(NormalUser normalUser, String crushUrl) {

        try {
            int index1 = crushUrl.indexOf("lst=") + 4;
            int index2 = crushUrl.indexOf("%3A", index1);
            int index3 = index2 + 3;
            int index4 = crushUrl.indexOf("%3A", index3);
            String fbUserID = crushUrl.substring(index1, index2);
            String fbCrushID = crushUrl.substring(index3, index4);
            Crush crush = this.crushFactory.create(normalUser.getAppUserID(), fbCrushID);
            normalUser.setFbUserID(fbUserID);
            this.userSqlRepo.setUserFbUserID(normalUser);
            return this.crushSqlRepo.addCrush(crush);
        } catch (Exception e) {
            System.out.println("Please Enter Valid URL!!");
            return null;
        }

    }

}
