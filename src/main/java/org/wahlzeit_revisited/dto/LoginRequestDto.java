package org.wahlzeit_revisited.dto;

/**
 * LoginRequestDto so outer world can log in
 */
public class LoginRequestDto {

    private String email;
    private String password;

    public LoginRequestDto() {
        // default constructor
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
