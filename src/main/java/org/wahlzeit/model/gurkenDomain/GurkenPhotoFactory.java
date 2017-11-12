/*
 *  Copyright
 *
 *  Classname: GurkenPhotoFactory
 *  Author: Tango1266
 *  Version: 13.11.17 00:21
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

package org.wahlzeit.model.gurkenDomain;

import org.wahlzeit.model.Location;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoId;
import org.wahlzeit.services.LogBuilder;

import java.util.logging.Logger;

/**
 * An Abstract Factory for creating GurkenPhotos and related objects.
 */
public class GurkenPhotoFactory extends PhotoFactory {

    private static final Logger log = Logger.getLogger(GurkenPhotoFactory.class.getName());
    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    private static GurkenPhotoFactory instance = null;

    /**
     *
     */
    protected GurkenPhotoFactory() {
        // do nothing
    }

    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    public static void initialize() {
        getInstance(); // drops result due to getInstance() side-effects
    }

    /**
     * Public singleton access method.
     */
    public static synchronized GurkenPhotoFactory getInstance() {
        if (instance == null) {
            getLogger().config(LogBuilder.createSystemMessage().addAction("setting generic GurkenPhotoFactory").toString());
            setInstance(new GurkenPhotoFactory());
        }

        return instance;
    }

    /**
     * @methodtype factory
     */
    @Override
    public Photo createPhoto() {
        return new GurkenPhoto();
    }

    /**
     * Creates a new photo with the specified id
     */
    @Override
    public Photo createPhoto(PhotoId id) {
        return new GurkenPhoto(id);
    }

    public GurkenPhoto createGurkenPhoto(PhotoId photoId, String cucumberType, int sizeInMillimeter, Taste taste, Location location) {
        return new GurkenPhoto(photoId, cucumberType, sizeInMillimeter, taste, location);
    }

    /**
     * Method to set the singleton instance of GurkenPhotoFactory.
     */
    protected static synchronized void setInstance(GurkenPhotoFactory gurkenPhotoFactory) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initalize GurkenPhotoFactory twice");
        }

        instance = gurkenPhotoFactory;
    }

    protected static Logger getLogger() {
        return log;
    }
}
