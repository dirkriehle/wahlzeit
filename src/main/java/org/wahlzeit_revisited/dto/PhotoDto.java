package org.wahlzeit_revisited.dto;

import java.util.Collection;
import java.util.Set;

public class PhotoDto {

    private final Long id;
    private final Long userId;
    private final String path;
    private final Set<String> tags;

    private final int width;
    private final int height;

    public PhotoDto(Long id, Long userId, String path, Set<String> tags, int width, int height) {
        this.id = id;
        this.userId = userId;
        this.path = path;
        this.tags = tags;
        this.width = width;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<String> getTags() {
        return tags;
    }
}
