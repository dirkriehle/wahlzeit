package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.repository.Persistent;

public class User extends Client implements Persistent {

    private Long id;
    private String name;
    private String email;
    private String password;

    private final long creationTime;

    /*
     * constructor
     */

    User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        creationTime = System.currentTimeMillis();
    }

    User(Long id, Long creationTime, String name, String email, String password, AccessRights rights) {
        this.id = id;
        this.creationTime = creationTime;
        this.name = name;
        this.email = email;
        this.password = password;
        this.rights = rights;
    }

    /*
     * Persistent contract
     */

    @Override
    public Long getId() {
        return id;
    }

    /*
     * getter/setter
     */

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreationTime() {
        return creationTime;
    }
}
