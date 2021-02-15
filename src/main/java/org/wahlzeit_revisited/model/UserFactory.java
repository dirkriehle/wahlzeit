package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.auth.AccessRights;

import java.time.Instant;

public class UserFactory {

    public User createUser() {
        String name = "No Username";
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

    public User createUser(Long id, Long creationTime, String name, String email, String password, AccessRights rights) {
        return new User(id, creationTime, name, email, password, rights);
    }

}
