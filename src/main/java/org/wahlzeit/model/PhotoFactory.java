/*
 *  Copyright
 *
 *  Classname: PhotoFactory
 *  Author: Tango1266
 *  Version: 16.11.17 15:19
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

package org.wahlzeit.model;

import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.Assert;

import java.util.logging.Logger;

/**
 * An Abstract Factory for creating photos and related objects.
 */
public abstract class PhotoFactory {

    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    private static PhotoFactory instance;
    private static final Logger log = Logger.getLogger(getInstance() != null ? getInstance().getClass().getName() : PhotoFactory.class.getName());

    /**
     *
     */
    protected PhotoFactory() {
        // do nothing
    }

    /**
     * Public singleton access method.
     */
    public static synchronized PhotoFactory getInstance() {
        return instance;
    }

    /**
     * @methodtype factory
     */
    public Photo createPhoto() {
        return new Photo();
    }

    /**
     * Creates a new photo with the specified id
     */
    public Photo createPhoto(PhotoId id) {
        return new Photo(id);
    }

    /**
     * Loads a photo. The Java object is loaded from the Google Datastore, the Images in all sizes are loaded from the
     * Google Cloud storage.
     */
    public Photo loadPhoto(PhotoId id) {
       /* Photo result =
                OfyService.ofy().load().type(Photo.class).ancestor(KeyFactory.createKey("Application", "Wahlzeit")).filter(Photo.ID, id).first().now();
        for (PhotoSize size : PhotoSize.values()) {
            GcsFilename gcsFilename = new GcsFilename("picturebucket", filename);



        }*/
        return null;
    }

    /**
     *
     */
    public PhotoFilter createPhotoFilter() {
        return new PhotoFilter();
    }

    /**
     *
     */
    public PhotoTagCollector createPhotoTagCollector() {
        return new PhotoTagCollector();
    }

    /**
     * Method to set the singleton instance of GurkenPhotoFactory.
     */
    public static synchronized void setInstance(PhotoFactory photoFactory) {
        Assert.isNull(instance, "PhotoFactory");
        log.config(LogBuilder.createSystemMessage().addAction("setting generic GurkenPhotoFactory").toString());
        instance = photoFactory;
    }

}
