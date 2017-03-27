package com.nsfl.gocrush.ApplicationLayer.Factory;

import com.nsfl.gocrush.ModelLayer.NormalUser;

public class NormalUserFactory extends UserFactory {

    @Override
    public NormalUser create(String userID, String fbToken) {
        return new NormalUser(userID, fbToken);
    }

}
