package org.wahlzeit_revisited.auth;

import org.wahlzeit_revisited.model.User;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * An PrincipalUser is an that allows to inject the user entity,
 * which was determined for the current jakarta REST call
 */
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
