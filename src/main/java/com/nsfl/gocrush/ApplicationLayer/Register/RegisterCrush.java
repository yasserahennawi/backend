package com.nsfl.gocrush.ApplicationLayer.Register;

import com.nsfl.gocrush.ApplicationLayer.Factory.CrushFactory;
import com.nsfl.gocrush.DBLayer.CrushSQLRepository;
import com.nsfl.gocrush.ModelLayer.Crush;

public class RegisterCrush {

    private CrushSQLRepository crushSqlRepo;
    private CrushFactory crushFactory;

    public RegisterCrush(CrushSQLRepository crushSqlRepo, CrushFactory crushFactory) {
        this.crushSqlRepo = crushSqlRepo;
        this.crushFactory = crushFactory;
    }

    public Crush register(String userID, String crushID) {
        //TODO here check how many user should have
        Crush crush = this.crushFactory.create(userID, crushID);
        return this.crushSqlRepo.addCrush(crush);

    }

}
