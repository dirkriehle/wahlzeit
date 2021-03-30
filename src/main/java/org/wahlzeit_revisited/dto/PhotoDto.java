package org.wahlzeit_revisited.dto;
public class PhotoDto {

    private final Long id;
    private final Long userId;
    private final String path;

    private final int width;
    private final int height;

    public PhotoDto(Long id, Long userId, String path, int width, int height) {
        this.id = id;
        this.userId = userId;
        this.path = path;
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
}
