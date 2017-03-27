
package com.nsfl.gocrush.ApplicationLayer.Factory;

import com.nsfl.gocrush.ModelLayer.User;




public abstract class UserFactory {
    
    public abstract User create(String userID, String fbToken);
    
}
