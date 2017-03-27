
package com.nsfl.gocrush.ApplicationLayer.Factory;

import com.nsfl.gocrush.ModelLayer.Crush;


public class CrushFactory {
    
    public Crush create(String userID, String crushID) {
        return new Crush(userID, crushID);
    }
    
}
