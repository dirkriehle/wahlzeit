package org.wahlzeit_revisited.model;

import java.util.Optional;
import java.util.Set;

public class PhotoFilter {

    private final Long userId;
    private final Tags tags;

    public PhotoFilter(Long userId, Tags tags) {
        this.userId = userId;
        this.tags = tags;
    }

    public boolean filtersByUser() {
        return this.getUserId().isPresent();
    }

    public boolean filtersByTags() {
        return !getTags().isEmpty();
    }

    public boolean hasFilters() {
        return filtersByTags() || filtersByUser();
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Set<String> getTags() {
        return tags.getTags();
    }
}
