package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.repository.DataObject;

/**
 * A photo represents a user-provided (uploaded) photo.
 */
public class Photo extends DataObject {

    /*
     * global constraints
     */

    public static final int MAX_PHOTO_WIDTH = 420;
    public static final int MAX_PHOTO_HEIGHT = 600;
    public static final int MAX_THUMB_PHOTO_WIDTH = 105;
    public static final int MAX_THUMB_PHOTO_HEIGHT = 150;

    /*
     * coupled classes
     */

    protected PhotoStatus status;
    protected Long ownerId;

    /*
     * members
     */

    protected int width;
    protected int height;
    protected PhotoSize maxPhotoSize = PhotoSize.MEDIUM; // derived
    protected long creationTime = System.currentTimeMillis();

    /*
     * constructor
     */

    Photo(PhotoStatus status, int with, int height) {
        this.status = status;
        this.width = with;
        this.height = height;
    }

    Photo(long ownerId, PhotoStatus status, int with, int height) {
        this.ownerId = ownerId;
        this.status = status;
        this.width = with;
        this.height = height;
    }

    Photo(long id, long creationTime, long ownerId, PhotoStatus status, int width, int height) {
        this.id = id;
        this.creationTime = creationTime;
        this.ownerId = ownerId;
        this.status = status;
        this.width = width;
        this.height = height;
    }

    /*
     * getter/setter
     */

    /**
     * @methodtype get
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * @methodtype boolean-query
     */
    public boolean hasSameOwner(Photo photo) {
        return getOwnerId().equals(photo.getOwnerId());
    }

    /**
     * @methodtype boolean-query
     */
    public boolean isWiderThanHigher() {
        return (height * MAX_PHOTO_WIDTH) < (width * MAX_PHOTO_HEIGHT);
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
    public int getThumbWidth() {
        return isWiderThanHigher() ? MAX_THUMB_PHOTO_WIDTH : (width * MAX_THUMB_PHOTO_HEIGHT / height);
    }

    /**
     * @methodtype get
     */
    public int getThumbHeight() {
        return isWiderThanHigher() ? (height * MAX_THUMB_PHOTO_WIDTH / width) : MAX_THUMB_PHOTO_HEIGHT;
    }

    /**
     * @methodtype set
     */
    public void setWidthAndHeight(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;

        maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);
    }

    /**
     * Can this photo satisfy provided photo size?
     *
     * @methodtype boolean-query
     */
    public boolean hasPhotoSize(PhotoSize size) {
        return maxPhotoSize.asInt() >= size.asInt();
    }

    /**
     * @methodtype get
     */
    public PhotoSize getMaxPhotoSize() {
        return maxPhotoSize;
    }

    /**
     * @methodtype boolean-query
     */
    public boolean isVisible() {
        return status.isDisplayable();
    }

    /**
     * @methodtype get
     */
    public PhotoStatus getStatus() {
        return status;
    }

    /**
     * @methodtype set
     */
    public void setStatus(PhotoStatus newStatus) {
        status = newStatus;
    }

    /**
     * @methodtype get
     */
    public long getCreationTime() {
        return creationTime;
    }

}
