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

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.wahlzeit.services.LogBuilder;

import java.util.logging.Logger;

/**
 * PhotoUtil provides a set of utility functions to create defined images.
 * Images are created from a source in different sizes as needed by the app.
 *
 * @author dirkriehle
 */
public class PhotoUtil {

    private static final Logger log = Logger.getLogger(PhotoUtil.class.getName());

    /**
     * @methodtype creation
     */
    public static Photo createPhoto(String filename, PhotoId id, Image uploadedImage) throws Exception {
        Photo result = PhotoFactory.getInstance().createPhoto(id);
        result.setEnding(filename.substring(filename.lastIndexOf(".") + 1));

        createImageFiles(uploadedImage, result);

        int sourceWidth = uploadedImage.getWidth();
        int sourceHeight = uploadedImage.getHeight();
        result.setWidthAndHeight(sourceWidth, sourceHeight);

        return result;
    }

    /**
     *
     */
    public static void createImageFiles(Image source, Photo photo) throws Exception {
        assertIsValidImage(source);

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        assertHasValidSize(sourceWidth, sourceHeight);

        for (PhotoSize size : PhotoSize.values()) {
            if (!size.isWiderAndHigher(sourceWidth, sourceHeight)) {
                scaleImage(ImagesServiceFactory.makeImage(source.getImageData()), size, photo);
            }
        }
    }

    /**
     * @methodtype assertion
     */
    protected static void assertIsValidImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image = null!");
        }
    }

    /**
     *
     */
    protected static void assertHasValidSize(int cw, int ch) {
        if (PhotoSize.THUMB.isWiderAndHigher(cw, ch)) {
            throw new IllegalArgumentException("Photo too small!");
        }
    }

    /**
     * @methodtype command
     * Scale the source picture to the given size, store it in the datastore and reference it in the photo.
     */
    protected static void scaleImage(Image source, PhotoSize size, Photo photo) throws Exception {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        int targetWidth = size.calcAdjustedWidth(sourceWidth, sourceHeight);
        int targetHeight = size.calcAdjustedHeight(sourceWidth, sourceHeight);

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        Transform resize = ImagesServiceFactory.makeResize(targetWidth, targetHeight);
        Image newImage = imagesService.applyTransform(resize, source);

        photo.setImage(size, newImage);

        log.config(LogBuilder.createSystemMessage().addParameter("Scaled image to size", size.asString()).toString());
    }

}
