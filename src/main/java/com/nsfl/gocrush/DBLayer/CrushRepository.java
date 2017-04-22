package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ApplicationLayer.Error.DbError;
import com.nsfl.gocrush.ModelLayer.Crush;
import java.util.ArrayList;

public abstract class CrushRepository {

    public abstract Crush getCrush(Crush crush) throws DbError;

    public abstract Crush addCrush(Crush crush) throws DbError;

    public abstract Crush deleteCrush(Crush crush) throws DbError;

    public abstract ArrayList<Crush> getCrushesByUserAppID(String id) throws DbError;

    public abstract int getNumberOfCrushesByUserAppID(String id) throws DbError;

    public abstract int getNumberOfCrushesOnUser(String id) throws DbError;

}
