package org.wahlzeit_revisited.dto;

public class PhotoDto {

    private final Long id;
    private final String path;
    private final int width;
    private final int height;

    public PhotoDto(Long id, String path, int width, int height) {
        this.id = id;
        this.path = path;
        this.width = width;
        this.height = height;
    }

    public Long getId() {
        return id;
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
}
