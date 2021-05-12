/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
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

package org.wahlzeit.model;


import java.util.Optional;
import java.util.Set;

/**
 * Inclusive filter of a PhotoSet query
 */
public class PhotoFilter {

    private final User user;
    private final Tags tags;

    public PhotoFilter(User user, Tags tags) {
        this.user = user;
        this.tags = tags;
    }

    /**
     * @methodtype boolean-query
     */
    public boolean filtersByUser() {
        return this.getUserId().isPresent();
    }

    /**
     * @methodtype boolean-query
     */
    public boolean filtersByTags() {
        return !getTags().isEmpty();
    }

    /**
     * @methodtype boolean-query
     */
    public boolean hasFilters() {
        return filtersByTags() || filtersByUser();
    }

    /**
     * @methodtype get
     */
    public Optional<Long> getUserId() {
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user.getId());
    }

    /**
     * @methodtype get
     */
    public Set<String> getTags() {
        return tags.getTags();
    }
}
