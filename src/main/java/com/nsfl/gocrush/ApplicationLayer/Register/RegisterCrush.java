package com.nsfl.gocrush.ApplicationLayer.Register;

import com.nsfl.gocrush.ApplicationLayer.Error.DbError;
import com.nsfl.gocrush.ApplicationLayer.Error.FbError;
import com.nsfl.gocrush.ApplicationLayer.Error.ValidationError;
import com.nsfl.gocrush.ApplicationLayer.Factory.CrushFactory;
import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.Crush;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import com.nsfl.gocrush.Utility.FacebookApi;

public class RegisterCrush {

    private CrushSQLRepository crushSqlRepo;
    private UserSQLRepository userSqlRepo;
    private CrushFactory crushFactory;
    private FacebookApi facebookApi;

    public RegisterCrush(UserSQLRepository userSqlRepo, CrushSQLRepository crushSqlRepo, CrushFactory crushFactory, FacebookApi facebookApi) {
        this.userSqlRepo = userSqlRepo;
        this.crushSqlRepo = crushSqlRepo;
        this.crushFactory = crushFactory;
        this.facebookApi = facebookApi;
    }

    public String register(NormalUser normalUser, String crushUrl) throws ValidationError, DbError {

        try {
            int index1 = crushUrl.indexOf("lst=") + 4;
            int index2 = crushUrl.indexOf("%3A", index1);
            int index3 = index2 + 3;
            int index4 = crushUrl.indexOf("%3A", index3);
            String fbUserID = crushUrl.substring(index1, index2);
            String fbCrushID = crushUrl.substring(index3, index4);

            if (normalUser.getFbUserID() == null) {
                if (facebookApi.sameUser(normalUser, fbUserID)) {
                    normalUser.setFbUserID(fbUserID);
                    this.userSqlRepo.setUserFbUserID(normalUser);
                } else {
                    throw new ValidationError("Invalid url");
                }
            }

            Crush crush = this.crushFactory.create(normalUser.getAppUserID(), fbCrushID);
            String crushjson = facebookApi.getCrushData(crush, normalUser.getFbToken());
            if (this.crushSqlRepo.getNumberOfCrushesByUserAppID(normalUser.getAppUserID()) >= 2) {
                throw new DbError("Max number of crushes reached");
            } else {
                this.crushSqlRepo.addCrush(crush);
            }
            return crushjson;

        } catch (Exception | FbError e) {

            throw new ValidationError("Invalid url");
        }
    }

}
