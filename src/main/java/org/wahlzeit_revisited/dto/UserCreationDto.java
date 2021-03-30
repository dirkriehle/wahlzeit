package org.wahlzeit_revisited.dto;

public class UserCreationDto {

    /*
     * members
     */

    String name;
    String email;
    String password;

    /*
     * constructor
     */

    public UserCreationDto() {
        // default constructor
    }

    /*
     * getter/setter
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
