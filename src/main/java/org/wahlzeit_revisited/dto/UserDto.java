package org.wahlzeit_revisited.dto;

public class UserDto {

    /*
     * members
     */

    private Long id;
    private String name;
    private String email;

    /*
     * constructor
     */

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /*
     * getters/setters
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}
