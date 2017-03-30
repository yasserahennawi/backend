package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.Crush;
import java.util.ArrayList;

public abstract class CrushRepository {

    public abstract Crush addCrush(Crush crush);
    
    public abstract Crush deleteCrush(Crush crush);

    public abstract ArrayList<Crush> getCrushesByUserAppID(String id);

    public abstract int getNumberOfCrushesByUserAppID(String id);
    
    public abstract int getNumberOfCrushesOnUser(String id);

}
