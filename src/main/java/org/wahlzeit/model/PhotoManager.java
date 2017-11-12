/*
 *  Copyright
 *
 *  Classname: PhotoManager
 *  Author: Tango1266
 *  Version: 12.11.17 23:51
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

import com.google.appengine.api.images.Image;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.wahlzeit.model.persistence.ImageStorage;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * A photo manager provides access to and manages photos.
 */
public class PhotoManager extends ObjectManager {

    private static final Logger log = Logger.getLogger(PhotoManager.class.getName());
    /**
     *
     */
    protected static final PhotoManager instance = new PhotoManager();
    /**
     * In-memory cache for photos
     */
    protected Map<PhotoId, Photo> photoCache = new HashMap<>();

    /**
     *
     */
    protected PhotoTagCollector photoTagCollector = null;

    /**
     * TODO: Check whether the constructor should be private because of singleton
     */
    public PhotoManager() {
        photoTagCollector = PhotoFactory.getInstance().createPhotoTagCollector();
    }

    /**
     * @methodtype get
     * @methodproperties primitive
     */
    protected Photo doGetPhotoFromId(PhotoId id) {
        return photoCache.get(id);
    }

    /**
     * @methodtype command
     * @methodproperties primitive
     */
    protected void doAddPhoto(Photo myPhoto) {
        photoCache.put(myPhoto.getId(), myPhoto);
    }

    /**
     * @methodtype boolean-query
     * @methodproperty primitive
     */
    protected boolean doHasPhoto(PhotoId id) {
        return photoCache.containsKey(id);
    }

    /**
     * @methodtype command
     * Loads all scaled Images of this Photo from Google Cloud Storage
     */
    protected void loadScaledImages(Photo photo) {
        String photoIdAsString = photo.getId().asString();
        ImageStorage imageStorage = ImageStorage.getInstance();

        for (PhotoSize photoSize : PhotoSize.values()) {
            getLogger().config(LogBuilder.createSystemMessage().
                    addAction("loading image").
                    addParameter("image size", photoSize.asString()).
                    addParameter("photo ID", photoIdAsString).toString());
            if (imageStorage.doesImageExist(photoIdAsString, photoSize.asInt())) {
                try {
                    Serializable rawImage = imageStorage.readImage(photoIdAsString, photoSize.asInt());
                    if (rawImage != null && rawImage instanceof Image) {
                        photo.setImage(photoSize, (Image) rawImage);
                    }
                } catch (IOException e) {
                    getLogger().warning(LogBuilder.createSystemMessage().
                            addParameter("size", photoSize.asString()).
                            addParameter("photo ID", photoIdAsString).
                            addException("Could not load image although it exists", e).toString());
                }
            } else {
                getLogger().config(LogBuilder.createSystemMessage().
                        addParameter("Size does not exist", photoSize.asString()).toString());
            }
        }
    }

    @Override
    protected void updateDependents(Persistent obj) {
        if (obj instanceof Photo) {
            Photo photo = (Photo) obj;
            saveScaledImages(photo);
            updateTags(photo);
            UserManager userManager = UserManager.getInstance();
            Client owner = userManager.getClientById(photo.getOwnerId());
            userManager.saveClient(owner);
        }
    }

    /**
     * @methodtype command
     * Persists all available sizes of the Photo. If one size exceeds the limit of the persistence layer, e.g. > 1MB for
     * the Datastore, it is simply not persisted.
     */
    protected void saveScaledImages(Photo photo) {
        String photoIdAsString = photo.getId().asString();
        ImageStorage imageStorage = ImageStorage.getInstance();
        PhotoSize photoSize;
        int it = 0;
        boolean moreSizesExist = true;
        do {
            photoSize = PhotoSize.values()[it];
            it++;
            Image image = photo.getImage(photoSize);
            if (image != null) {
                try {
                    if (!imageStorage.doesImageExist(photoIdAsString, photoSize.asInt())) {
                        imageStorage.writeImage(image, photoIdAsString, photoSize.asInt());
                    }
                } catch (Exception e) {
                    getLogger().warning(LogBuilder.createSystemMessage().
                            addException("Problem when storing image", e).toString());
                    moreSizesExist = false;
                }
            } else {
                getLogger().config(LogBuilder.createSystemMessage().
                        addParameter("No image for size", photoSize.asString()).toString());
                moreSizesExist = false;
            }
        } while (it < PhotoSize.values().length && moreSizesExist);
    }

    /**
     * Removes all tags of the Photo (obj) in the datastore that have been removed by the user and adds all new tags of
     * the photo to the datastore.
     */
    protected void updateTags(Photo photo) {
        // delete all existing tags, for the case that some have been removed
        deleteObjects(Tag.class, Tag.PHOTO_ID, photo.getId().asString());

        // add all current tags to the datastore
        Set<String> tags = new HashSet<>();
        photoTagCollector.collect(tags, photo);
        for (Iterator<String> i = tags.iterator(); i.hasNext(); ) {
            Tag tag = new Tag(i.next(), photo.getId().asString());
            getLogger().config(LogBuilder.createSystemMessage().addParameter("Writing Tag", tag.asString()).toString());
            writeObject(tag);
        }
    }

    /**
     * @methodtype assertion
     */
    protected void assertIsNewPhoto(PhotoId id) {
        if (hasPhoto(id)) {
            throw new IllegalStateException("Photo already exists!");
        }
    }

    protected Logger getLogger() {
        return log;
    }

    /**
     *
     */
    public static PhotoManager getInstance() {
        return instance;
    }

    /**
     *
     */
    public final boolean hasPhoto(String id) {
        return hasPhoto(PhotoId.getIdFromString(id));
    }

    /**
     *
     */
    public final boolean hasPhoto(PhotoId id) {
        return getPhoto(id) != null;
    }

    /**
     *
     */
    public Photo getPhoto(PhotoId id) {
        return getInstance().getPhotoFromId(id);
    }

    /**
     *
     */
    public Photo getPhotoFromId(PhotoId id) {
        if (id == null) {
            return null;
        }

        Photo result = doGetPhotoFromId(id);

        if (result == null) {
            result = PhotoFactory.getInstance().loadPhoto(id);
            if (result != null) {
                doAddPhoto(result);
            }
        }

        return result;
    }

    /**
     * @methodtype get
     */
    public final Photo getPhoto(String id) {
        return getPhoto(PhotoId.getIdFromString(id));
    }

    /**
     * @methodtype init Loads all Photos from the Datastore and holds them in the cache
     */
    public void init() {
        loadPhotos();
    }

    /**
     * @methodtype command
     * Load all persisted photos. Executed when Wahlzeit is restarted.
     */
    public void loadPhotos() {
        Collection<Photo> existingPhotos = ObjectifyService.run(new Work<Collection<Photo>>() {
            @Override
            public Collection<Photo> run() {
                Collection<Photo> existingPhotos = new ArrayList<>();
                readObjects(existingPhotos, Photo.class);
                return existingPhotos;
            }
        });

        for (Photo photo : existingPhotos) {
            if (!doHasPhoto(photo.getId())) {
                getLogger().config(LogBuilder.createSystemMessage().
                        addParameter("Load Photo with ID", photo.getIdAsString()).toString());
                loadScaledImages(photo);
                doAddPhoto(photo);
            } else {
                getLogger().config(LogBuilder.createSystemMessage().
                        addParameter("Already loaded Photo", photo.getIdAsString()).toString());
            }
        }

        getLogger().info(LogBuilder.createSystemMessage().addMessage("All photos loaded.").toString());
    }

    /**
     *
     */
    public void savePhoto(Photo photo) {
        updateObject(photo);
    }

    /**
     * @methodtype helper
     */
    public List<Tag> addTagsThatMatchCondition(List<Tag> tags, String condition) {
        readObjects(tags, Tag.class, Tag.TEXT, condition);
        return tags;
    }

    /**
     *
     */
    public void savePhotos() throws IOException {
        updateObjects(photoCache.values());
    }

    /**
     * @methodtype get
     */
    public Map<PhotoId, Photo> getPhotoCache() {
        return photoCache;
    }

    /**
     *
     */
    public Set<Photo> findPhotosByOwner(String ownerName) {
        Set<Photo> result = new HashSet<>();
        readObjects(result, Photo.class, Photo.OWNER_ID, ownerName);

        for (Iterator<Photo> i = result.iterator(); i.hasNext(); ) {
            doAddPhoto(i.next());
        }

        return result;
    }

    /**
     *
     */
    public Photo getVisiblePhoto(PhotoFilter filter) {
        filter.generateDisplayablePhotoIds();
        return getPhotoFromId(filter.getRandomDisplayablePhotoId());
    }

    /**
     *
     */
    public Photo createPhoto(String filename, Image uploadedImage) throws Exception {
        PhotoId id = PhotoId.getNextId();
        Photo result = PhotoUtil.createPhoto(filename, id, uploadedImage);
        addPhoto(result);
        return result;
    }

    /**
     * @methodtype command
     */
    public void addPhoto(Photo photo) throws IOException {
        PhotoId id = photo.getId();
        assertIsNewPhoto(id);
        doAddPhoto(photo);

        GlobalsManager.getInstance().saveGlobals();
    }

    /**
     * @methodtype command
     */
    public void removePhoto(Photo photo) throws IOException {
        photoCache.remove(photo.id);
    }

}
