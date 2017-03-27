package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.Crush;
import java.util.ArrayList;

public abstract class CrushRepository {

    public abstract Crush addCrush(Crush crush);
    
    public abstract Crush deleteCrush(Crush crush);

    public abstract ArrayList<Crush> getCrushesByUserID(String id);

    public abstract int getNumberOfCrushesByUserID(String id);

}
