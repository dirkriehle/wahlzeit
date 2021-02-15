package org.wahlzeit_revisited.auth;

import org.wahlzeit_revisited.model.User;

import javax.security.auth.Subject;
import java.security.Principal;

public class PrincipalUser implements Principal {

    private final User user;

    public PrincipalUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /*
     * Principal contract
     */

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
