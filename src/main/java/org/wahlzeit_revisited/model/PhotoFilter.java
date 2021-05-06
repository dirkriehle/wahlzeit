package org.wahlzeit_revisited.model;


import com.sun.istack.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * Inclusive filter of a PhotoSet query
 */
public class PhotoFilter {

    private final User user;
    private final Tags tags;

    public PhotoFilter(@Nullable User user, Tags tags) {
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
        return Optional.ofNullable(user.getId());
    }

    /**
     * @methodtype get
     */
    public Set<String> getTags() {
        return tags.getTags();
    }
}
