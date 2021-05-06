package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.repository.PersistentFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

/*
 * UserFactory creates a new Photo, or instantiates a entity from a resultSet
 */
public class UserFactory implements PersistentFactory<User> {

    /*
     * PersistentFactory contract
     */

    @Override
    public User createPersistent(ResultSet resultSet) throws SQLException {
        return new User(resultSet);
    }

    /**
     *
     * @return
     */
    public User createUser() {
        String name = Instant.now().toEpochMilli() + "NoUsername";
        String email = Instant.now().toEpochMilli() + "noEmail@fau.de";
        String password = "NoPassword";
        AccessRights accessRights = AccessRights.NONE;

        User user = new User(name, email, password);
        user.setRights(accessRights);
        return user;
    }

    public User createUser(String name, String email, String password, AccessRights rights) {
        User user = new User(name, email, password);
        user.setRights(rights);
        return user;
    }

}
