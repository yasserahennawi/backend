package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.NormalUser;
import java.util.ArrayList;

public abstract class UserRepository {

    public abstract void addUser(NormalUser user);

    public abstract ArrayList<NormalUser> getUsers();

    public abstract NormalUser getUserById(String id);

}
