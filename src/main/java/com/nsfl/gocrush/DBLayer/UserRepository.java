package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ApplicationLayer.Error.DbError;
import com.nsfl.gocrush.ModelLayer.NormalUser;
import java.util.ArrayList;

public abstract class UserRepository {

    public abstract NormalUser addUser(NormalUser user) throws DbError;

    public abstract NormalUser updateUserFbToken(NormalUser user) throws DbError;

    public abstract ArrayList<NormalUser> getUsers() throws DbError;

    public abstract NormalUser getUserByAppID(String id) throws DbError;

}
