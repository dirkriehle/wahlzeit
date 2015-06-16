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
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.wahlzeit.model.persistance.ImageStorage;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A photo manager provides access to and manages photos.
 *
 * @author dirkriehle
 */
public class PhotoManager extends ObjectManager {

    /**
     *
     */
    protected static final PhotoManager instance = new PhotoManager();

    private static final Logger log = Logger.getLogger(PhotoManager.class.getName());

    /**
     * In-memory cache for photos
     */
    protected Map<PhotoId, Photo> photoCache = new HashMap<PhotoId, Photo>();

    /**
     *
     */
    protected PhotoTagCollector photoTagCollector = null;

    /**
     *
     */
    public PhotoManager() {
        photoTagCollector = PhotoFactory.getInstance().createPhotoTagCollector();
    }

    /**
     *
     */
    public static final PhotoManager getInstance() {
        return instance;
    }

    /**
     *
     */
    public static final boolean hasPhoto(String id) {
        return hasPhoto(PhotoId.getIdFromString(id));
    }

    /**
     *
     */
    public static final boolean hasPhoto(PhotoId id) {
        return getPhoto(id) != null;
    }

    /**
     *
     */
    public static final Photo getPhoto(PhotoId id) {
        return instance.getPhotoFromId(id);
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
     *
     */
    public static final Photo getPhoto(String id) {
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
     */
    public void loadPhotos() {
        Collection<Photo> existingPhotos = ObjectifyService.run(new Work<Collection<Photo>>() {
            @Override
            public Collection<Photo> run() {
                Collection<Photo> existingPhotos = new ArrayList<Photo>();
                readObjects(existingPhotos, Photo.class);
                return existingPhotos;
            }
        });

        for (Photo photo : existingPhotos) {
            if (!doHasPhoto(photo.getId())) {
                log.config(LogBuilder.createSystemMessage().
                        addParameter("Load Photo with ID", photo.getIdAsString()).toString());
                loadScaledImages(photo);
                doAddPhoto(photo);
                try {
                    String ownerName = photo.getOwnerId();
                    User user = UserManager.getInstance().getUserById(ownerName);
                    if (user != null) {
                        user.addPhoto(photo);
                        log.config(LogBuilder.createSystemMessage().
                                addParameter("Found owner", user.getId()).toString());
                    } else {
                        log.warning(LogBuilder.createSystemMessage().
                                addParameter("missing owner", ownerName).toString());
                    }
                } catch (Exception e) {
                    log.warning(LogBuilder.createSystemMessage().
                            addException("Problem when loading owner", e).toString());
                }
            } else {
                log.config(LogBuilder.createSystemMessage().
                        addParameter("Already loaded Photo", photo.getIdAsString()).toString());
            }
        }

        log.info(LogBuilder.createSystemMessage().addMessage("All photos loaded.").toString());
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
     * <p/>
     * Loads all scaled Images of this Photo from Google Cloud Storage
     */
    protected void loadScaledImages(Photo photo) {
        String photoIdAsString = photo.getId().asString();
        ImageStorage imageStorage = ImageStorage.getInstance();

        for (PhotoSize photoSize : PhotoSize.values()) {
            log.config(LogBuilder.createSystemMessage().
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
                    log.warning(LogBuilder.createSystemMessage().
                            addParameter("size", photoSize.asString()).
                            addParameter("photo ID", photoIdAsString).
                            addException("Could not load image although it exists", e).toString());
                }
            } else {
                log.config(LogBuilder.createSystemMessage().
                        addParameter("Size does not exist", photoSize.asString()).toString());
            }
        }
    }

    /**
     *
     */
    public void savePhoto(Photo photo) {
        updateObject(photo);
    }

    @Override
    protected void updateDependents(Persistent obj) {
        if (obj instanceof Photo) {
            Photo photo = (Photo) obj;
            saveScaledImages(photo);
            updateTags(photo);
        }
    }

    /**
     * @methodtype command
     * <p/>
     * Writes all Images of the different sizes to Google Cloud Storage.
     */
    protected void saveScaledImages(Photo photo) {
        String photoIdAsString = photo.getId().asString();
        ImageStorage imageStorage = ImageStorage.getInstance();
        for (PhotoSize photoSize : PhotoSize.values()) {
            Image image = photo.getImage(photoSize);
            if (image != null) {
                try {
                    if (!imageStorage.doesImageExist(photoIdAsString, photoSize.asInt())) {
                        imageStorage.writeImage(image, photoIdAsString, photoSize.asInt());
                    }
                } catch (IllegalArgumentException e) {
                    log.warning(LogBuilder.createSystemMessage().
                            addException("Invalid parameter to store Image", e).toString());
                } catch (IOException e) {
                    log.warning(LogBuilder.createSystemMessage().
                            addException("Problem storing image", e).toString());
                }
            } else {
                log.config(LogBuilder.createSystemMessage().
                        addParameter("No image for size", photoSize.asString()).toString());
            }
        }
    }

    /**
     * Removes all tags of the Photo (obj) in the datastore that have been removed by the user and adds all new tags of
     * the photo to the datastore.
     */
    protected void updateTags(Photo photo) {
        // delete all existing tags, for the case that some have been removed
        deleteObjects(Tag.class, Tag.PHOTO_ID, photo.getId().asString());

        // add all current tags to the datastore
        Set<String> tags = new HashSet<String>();
        photoTagCollector.collect(tags, photo);
        for (Iterator<String> i = tags.iterator(); i.hasNext(); ) {
            Tag tag = new Tag(i.next(), photo.getId().asString());
            log.config(LogBuilder.createSystemMessage().addParameter("Writing Tag", tag.asString()).toString());
            writeObject(tag);
        }
    }

    /**
     *
     */
    public void savePhotos() {
        updateObjects(photoCache.values());
    }

    /**
     *
     */
    public Set<Photo> findPhotosByOwner(String ownerName) {
        Set<Photo> result = new HashSet<Photo>();
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
        Photo result = getPhotoFromFilter(filter);

        if (result == null) {
            List<PhotoId> list = getFilteredPhotoIds(filter);
            filter.setDisplayablePhotoIds(list);
            result = getPhotoFromFilter(filter);
        }

        return result;
    }

    /**
     *
     */
    protected Photo getPhotoFromFilter(PhotoFilter filter) {
        PhotoId id = filter.getRandomDisplayablePhotoId();
        Photo result = getPhotoFromId(id);
        while ((result != null) && !result.isVisible()) {
            id = filter.getRandomDisplayablePhotoId();
            result = getPhotoFromId(id);
            if ((result != null) && !result.isVisible()) {
                log.config(LogBuilder.createSystemMessage().
                        addParameter("add processed photo", result.getId().asString()).toString());
                filter.addProcessedPhoto(result);
            }
        }

        return result;
    }

    /**
     *
     */
    protected List<PhotoId> getFilteredPhotoIds(PhotoFilter filter) {
        // get all tags that match the filter conditions
        List<PhotoId> result = new LinkedList<PhotoId>();
        int noFilterConditions = filter.getFilterConditions().size();
        log.config(LogBuilder.createSystemMessage().
                addParameter("Number of filter conditions", String.valueOf(noFilterConditions)).toString());

        if (noFilterConditions == 0) {
            Collection<PhotoId> candidates = photoCache.keySet();
            int newPhotos = 0;
            for (PhotoId candidate : candidates) {
                if (!filter.processedPhotoIds.contains(candidate)) {
                    result.add(candidate);
                    ++newPhotos;
                }
            }

            log.config(LogBuilder.createSystemMessage().addParameter("Number of photos to show", newPhotos).toString());
        } else {
            List<Tag> tags = new LinkedList<Tag>();
            for (String condition : filter.getFilterConditions()) {
                readObjects(tags, Tag.class, Tag.TEXT, condition);
            }

            // get the list of all photo ids that correspond to the tags
            for (Tag tag : tags) {
                PhotoId photoId = PhotoId.getIdFromString(tag.getPhotoId());
                if (!filter.isProcessedPhotoId(photoId)) {
                    result.add(PhotoId.getIdFromString(tag.getPhotoId()));
                    log.config(LogBuilder.createSystemMessage().
                            addParameter("Add photo to filter, ID", tag.getPhotoId()).toString());
                }
            }
        }

        return result;
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
    public void addPhoto(Photo photo) {
        PhotoId id = photo.getId();
        assertIsNewPhoto(id);
        doAddPhoto(photo);

        GlobalsManager.getInstance().saveGlobals();
    }

    /**
     * @methodtype assertion
     */
    protected void assertIsNewPhoto(PhotoId id) {
        if (hasPhoto(id)) {
            throw new IllegalStateException("Photo already exists!");
        }
    }

}
