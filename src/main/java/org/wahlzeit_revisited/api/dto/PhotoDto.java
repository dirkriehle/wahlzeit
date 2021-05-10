/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit_revisited.api.dto;

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
