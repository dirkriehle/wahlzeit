package org.wahlzeit_revisited.model;

public class PhotoFactory {

    public Photo createPhoto() {
        int with = Photo.MAX_PHOTO_WIDTH;
        int height = Photo.MAX_PHOTO_HEIGHT;
        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(status, with, height);
    }

    public Photo createPhoto(long ownerId) {
        int with = Photo.MAX_PHOTO_WIDTH;
        int height = Photo.MAX_PHOTO_HEIGHT;
        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(ownerId, status, with, height);
    }

    public Photo createPhoto(long id, long creationTime, long ownerId, PhotoStatus status, int width, int height) {
        return new Photo(id, creationTime, ownerId, status, width, height);
    }

}
