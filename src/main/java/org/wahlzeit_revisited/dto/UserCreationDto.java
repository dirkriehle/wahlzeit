package org.wahlzeit_revisited.dto;

public class UserCreationDto {

    /*
     * members
     */

    String username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
