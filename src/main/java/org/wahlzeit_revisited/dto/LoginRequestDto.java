package org.wahlzeit_revisited.dto;

/**
 * LoginRequestDto so outer world can log in
 */
public class LoginRequestDto {

    /*
     * members
     */

    private String email;
    private String password;

    public LoginRequestDto() {
        // default constructor
    }

    /*
     * getter/setter
     */

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
