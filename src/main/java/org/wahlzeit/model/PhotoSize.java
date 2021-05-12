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

/**
 * The PhotoSize class defines the sizes in which a Photo can come.
 * The defined sizes are: thumb, extra small, small, medium, large, and extra large.
 * For each size, there is a defined pixel size to which photos are scaled.
 */
public enum PhotoSize {

    /**
     * medium: 440, 600
     */
    THUMB(0, Photo.MAX_THUMB_PHOTO_WIDTH, Photo.MAX_THUMB_PHOTO_HEIGHT), // = (105, 150)
    EXTRA_SMALL(1, Photo.MAX_PHOTO_WIDTH / 2, Photo.MAX_PHOTO_HEIGHT / 2), // (210, 300)
    SMALL(2, Photo.MAX_PHOTO_WIDTH * 2 / 3, Photo.MAX_PHOTO_HEIGHT * 2 / 3), // (280, 400)
    MEDIUM(3, Photo.MAX_PHOTO_WIDTH, Photo.MAX_PHOTO_HEIGHT), // (420, 600)
    LARGE(4, Photo.MAX_PHOTO_WIDTH * 3 / 2, Photo.MAX_PHOTO_HEIGHT * 3 / 2), // (630, 900)
    EXTRA_LARGE(5, Photo.MAX_PHOTO_WIDTH * 2, Photo.MAX_PHOTO_HEIGHT * 2); // (840, 1200)

    /**
     * All possible states of PhotoSize
     */
    private static final PhotoSize[] ALL_VALUES = {
            THUMB, EXTRA_SMALL, SMALL, MEDIUM, LARGE, EXTRA_LARGE
    };

    /**
     *
     */
    public static PhotoSize getFromInt(int myValue) throws IllegalArgumentException {
        assertIsValidPhotoSizeAsInt(myValue);
        return ALL_VALUES[myValue];
    }

    /**
     *
     */
    public static void assertIsValidPhotoSizeAsInt(int myValue) throws IllegalArgumentException {
        if ((myValue < 0) || (myValue > 5)) {
            throw new IllegalArgumentException("invalid PhotoSize int: " + myValue);
        }
    }

    /**
     *
     */
    public static PhotoSize getFromWidthHeight(int cw, int ch) {
        PhotoSize result = THUMB;

        for (PhotoSize size : PhotoSize.values()) {
            if (size.isWiderAndHigher(cw, ch)) {
                return result;
            } else {
                result = size;
            }
        }

        return EXTRA_LARGE;
    }

    /**
     *
     */
    private static final String[] VALUE_NAMES = {
            "thumb", "extra-small", "small", "medium", "large", "extra-large"
    };

    /**
     *
     */
    public static PhotoSize getFromString(String mySize) throws IllegalArgumentException {
        for (PhotoSize result : PhotoSize.values()) {
            if (VALUE_NAMES[result.asInt()].equals(mySize)) {
                return result;
            }
        }

        throw new IllegalArgumentException("invalid PhotoSize string: " + mySize);
    }

    /**
     *
     */
    private final int value;
    private final int maxPhotoWidth;
    private final int maxPhotoHeight;

    /**
     *
     */
    PhotoSize(int myValue, int myMaxWidth, int myMaxHeight) {
        value = myValue;
        maxPhotoWidth = myMaxWidth;
        maxPhotoHeight = myMaxHeight;
    }

    /**
     *
     */
    public int asInt() {
        return value;
    }

    /**
     *
     */
    public String asString() {
        return VALUE_NAMES[value];
    }

    /**
     *
     */
    public PhotoSize[] getAllValues() {
        return ALL_VALUES;
    }

    /**
     *
     */
    public String getTypeName() {
        return "PhotoSize";
    }

    /**
     * @methodtype boolean-query
     */
    public boolean isEqual(PhotoSize size) {
        return size == this;
    }

    /**
     *
     */
    public boolean isSmaller(PhotoSize size) {
        return value < size.asInt();
    }

    /**
     *
     */
    public int getMaxPhotoWidth() {
        return maxPhotoWidth;
    }

    /**
     *
     */
    public int getMaxPhotoHeight() {
        return maxPhotoHeight;
    }

    /**
     *
     */
    public boolean isEqual(int cw, int ch) {
        return (cw == maxPhotoWidth) && (ch == maxPhotoHeight);
    }

    /**
     *
     */
    public boolean isWiderAndHigher(int cw, int ch) {
        return (cw < maxPhotoWidth) && (ch < maxPhotoHeight);
    }

    /**
     *
     */
    public int calcAdjustedWidth(int cw, int ch) {
        if ((cw * maxPhotoHeight) > (ch * maxPhotoWidth)) {
            return maxPhotoWidth;
        } else {
            return cw * maxPhotoHeight / ch;
        }
    }

    /**
     *
     */
    public int calcAdjustedHeight(int cw, int ch) {
        if ((cw * maxPhotoHeight) > (ch * maxPhotoWidth)) {
            return ch * maxPhotoWidth / cw;
        } else {
            return maxPhotoHeight;
        }
    }

}
