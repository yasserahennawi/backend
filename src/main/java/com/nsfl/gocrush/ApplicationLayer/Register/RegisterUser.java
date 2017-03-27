package com.nsfl.gocrush.ApplicationLayer.Register;

import com.nsfl.gocrush.ApplicationLayer.Factory.NormalUserFactory;
import com.nsfl.gocrush.DBLayer.UserSQLRepository;
import com.nsfl.gocrush.ModelLayer.NormalUser;

public class RegisterUser {

    private UserSQLRepository userSqlRepo;
    private NormalUserFactory normalUserFactory;

    public RegisterUser(UserSQLRepository userSqlRepo, NormalUserFactory normalUserFactory) {
        this.userSqlRepo = userSqlRepo;
        this.normalUserFactory = normalUserFactory;
    }

    public NormalUser registerUpdateUser(String userID, String fbToken) {

        NormalUser normalUser = userSqlRepo.getUserById(userID);
        if (normalUser == null) {

            NormalUser newUser = normalUserFactory.create(userID, fbToken);
            return userSqlRepo.addUser(newUser);

        } else {

            normalUser.setFbToken(fbToken);
            return userSqlRepo.updateUser(normalUser);

        }
    }

}
