package org.wahlzeit_revisited.dto;

/**
 * UserDto represents the User-entity for the outer world
 */
public class UserDto {

    /*
     * members
     */

    private final Long id;
    private final String name;
    private final String email;

    /*
     * constructor
     */

    UserDto(Long id, String name, String email) {
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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /*
     * Builder
     */

    public static class Builder {
        private Long id;
        private String name;
        private String email;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, name, email);
        }

    }

}
