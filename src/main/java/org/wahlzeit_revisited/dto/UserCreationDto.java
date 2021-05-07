package org.wahlzeit_revisited.dto;

/**
 * UserCreationDto represents the creation of a new user for the outer world
 */
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


    /**
     * @methodtype get
     */
    public String getName() {
        return name;
    }

    /**
     * @methodtype set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @methodtype get
     */
    public String getEmail() {
        return email;
    }

    /**
     * @methodtype set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @methodtype get
     */
    public String getPassword() {
        return password;
    }

    /**
     * @methodtype set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
