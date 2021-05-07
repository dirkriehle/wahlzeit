package org.wahlzeit_revisited.dto;

import java.util.Set;

/**
 * PhotoDto represents the Photo-entity for the outer world
 */
public class PhotoDto {

    private final Long id;
    private final Long userId;
    private final String path;
    private final Set<String> tags;
    private final int width;
    private final int height;
    private final double praise;

    public PhotoDto(Long id, Long userId, String path, Set<String> tags, int width, int height, double praise) {
        this.id = id;
        this.userId = userId;
        this.path = path;
        this.tags = tags;
        this.width = width;
        this.height = height;
        this.praise = praise;
    }

    /**
     * @methodtype get
     */
    public Long getId() {
        return id;
    }

    /**
     * @methodtype get
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @methodtype get
     */
    public String getPath() {
        return path;
    }

    /**
     * @methodtype get
     */
    public int getWidth() {
        return width;
    }

    /**
     * @methodtype get
     */
    public int getHeight() {
        return height;
    }

    /**
     * @methodtype get
     */
    public double getPraise() {
        return praise;
    }

    /**
     * @methodtype get
     */
    public Set<String> getTags() {
        return tags;
    }

    /*
     * Builder
     */

    public static class Builder {
        private Long id;
        private Long userId;
        private String path;
        private Set<String> tags;
        private int width;
        private int height;
        private double praise;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withTags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withWith(int with) {
            this.width = with;
            return this;
        }

        public Builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder withPraise(double praise) {
            this.praise = praise;
            return this;
        }

        /**
         * @methodtype transform
         */
        public PhotoDto build() {
            return new PhotoDto(id, userId, path, tags, width, height, praise);
        }
    }
}
