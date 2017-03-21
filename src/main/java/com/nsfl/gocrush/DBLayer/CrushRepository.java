package com.nsfl.gocrush.DBLayer;

import com.nsfl.gocrush.ModelLayer.Crush;
import java.util.ArrayList;

public abstract class CrushRepository {

    public abstract void addCrush(Crush crush);

    public abstract ArrayList<Crush> getCrushesByUserID(String id);

    public abstract int getNumberOfCrushesByUserID(String id);

}
