/*
 *  Copyright
 *
 *  Classname: PhotoFilter
 *  Author: Tango1266
 *  Version: 08.11.17 22:26
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

import org.wahlzeit.model.gurkenDomain.GurkenPhotoManager;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.StringUtil;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * A class to specify a photo filter.
 * A photo filter captures selection ("filtering") criteria for photos.
 */
public class PhotoFilter implements Serializable {

    private static Logger log = Logger.getLogger(PhotoFilter.class.getName());
    /**
     *
     */
    protected String userName = "";
    protected Tags tags = Tags.EMPTY_TAGS;
    /**
     *
     */
    protected List<PhotoId> displayablePhotoIds;
    protected List<PhotoId> processedPhotoIds = new LinkedList<>();
    protected List<PhotoId> skippedPhotoIds = new LinkedList<>();
    /**
     *
     */
    protected Random randomNumber = new Random(System.currentTimeMillis());
    /**
     *
     */
    public static final String USER_NAME = "userName";
    public static final String TAGS = "tags";

    /**
     *
     */
    public PhotoFilter() {
        resetDisplayablePhotoIds();
    }

    /**
     *
     */
    protected void collectFilterConditions(List<String> filterConditions) {
        String un = getUserName();
        if (!StringUtil.isNullOrEmptyString(un)) {
            filterConditions.add("un:" + Tags.asTag(un));
        }

        String[] tags = getTags().asArray();
        for (int i = 0; i < tags.length; i++) {
            filterConditions.add("tg:" + tags[i]);
        }
    }

    /**
     *
     */
    protected List<PhotoId> getFilteredPhotoIds() {
        // get all tags that match the filter conditions
        List<PhotoId> result = new LinkedList<>();
        int noFilterConditions = getFilterConditions().size();
        log.config(LogBuilder.createSystemMessage().
                addParameter("Number of filter conditions", String.valueOf(noFilterConditions)).toString());

        Collection<PhotoId> candidates;
        if (noFilterConditions == 0) {
            candidates = GurkenPhotoManager.getInstance().getPhotoCache().keySet();
        } else {
            List<Tag> tags = new LinkedList<>();
            candidates = new LinkedList<>();
            for (String condition : getFilterConditions()) {
                GurkenPhotoManager.getInstance().addTagsThatMatchCondition(tags, condition);
            }
            // get the list of all photo ids that correspond to the tags
            for (Tag tag : tags) {
                candidates.add(PhotoId.getIdFromString(tag.getPhotoId()));
            }
        }

        int newPhotos = 0;
        for (PhotoId candidateId : candidates) {
            Photo photoCandidate = GurkenPhotoManager.getInstance().getPhoto(candidateId);
            if (!processedPhotoIds.contains(candidateId) && !skippedPhotoIds.contains(candidateId) &&
                    photoCandidate.isVisible()) {
                result.add(candidateId);
                ++newPhotos;
            }
        }
        int skippedPhotos = skippedPhotoIds.size();
        if (newPhotos == 0 && skippedPhotos > 0) {
            result.addAll(skippedPhotoIds);
            newPhotos = skippedPhotos;
        }

        log.config(LogBuilder.createSystemMessage().addParameter("Number of photos to show", newPhotos)
                .toString());

        return result;
    }

    /**
     *
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     */
    public void clear() {
        setUserName("");
        setTags(Tags.EMPTY_TAGS);
        displayablePhotoIds.clear();
        processedPhotoIds.clear();
    }

    /**
     *
     */
    public void setUserName(String newUserName) {
        userName = newUserName;
        resetDisplayablePhotoIds();
    }

    /**
     *
     */
    public Tags getTags() {
        return tags;
    }

    /**
     *
     */
    public void setTags(Tags newTags) {
        tags = newTags;
        resetDisplayablePhotoIds();
    }

    /**
     *
     */
    public List<String> getFilterConditions() {
        List<String> filterConditions = new ArrayList<>();

        collectFilterConditions(filterConditions);

        return filterConditions;
    }

    /**
     * @methodtype command
     */
    public void generateDisplayablePhotoIds() {
        displayablePhotoIds = getFilteredPhotoIds();
    }

    /**
     * Get a random photo that has not been rated. If possible avoid skipped photos.
     */
    public PhotoId getRandomDisplayablePhotoId() {
        if (!displayablePhotoIds.isEmpty()) {
            int size = displayablePhotoIds.size();
            int index = ((randomNumber.nextInt() % size) + size) / 2;
            return displayablePhotoIds.get(index);
        } else {
            return PhotoId.NULL_ID;
        }
    }

    /**
     *
     */
    public List<PhotoId> getDisplayablePhotoIds() {
        return displayablePhotoIds;
    }

    /**
     *
     */
    public void setDisplayablePhotoIds(List<PhotoId> newPhotoIds) {
        displayablePhotoIds = newPhotoIds;
    }

    /**
     *
     */
    public void resetDisplayablePhotoIds() {
        displayablePhotoIds = new ArrayList<>();
    }

    /**
     *
     */
    public List<PhotoId> getProcessedPhotoIds() {
        return processedPhotoIds;
    }

    /**
     *
     */
    public boolean isProcessedPhotoId(PhotoId photoId) {
        log.info("photoId: " + photoId.asString());
        for (PhotoId id : processedPhotoIds) {
            log.info("Processed id: " + id.asString());
        }
        return processedPhotoIds.contains(photoId);
    }

    /**
     *
     */
    public void addProcessedPhoto(Photo photo) {
        PhotoId photoId = photo.getId();
        processedPhotoIds.add(photoId);
        skippedPhotoIds.remove(photoId);
        if (displayablePhotoIds != null) {
            displayablePhotoIds.remove(photoId);
        }
    }

    /**
     * @methodtype get
     */
    public List<PhotoId> getSkippedPhotoIds() {
        return skippedPhotoIds;
    }

    /**
     * @methodtype set
     */
    public void setSkippedPhotoIds(List<PhotoId> skippedPhotoIds) {
        this.skippedPhotoIds = skippedPhotoIds;
    }

    /**
     * @methodtype set
     */
    public void addSkippedPhotoId(PhotoId skippedPhotoId) {
        if (!skippedPhotoIds.contains(skippedPhotoId)) {
            skippedPhotoIds.add(skippedPhotoId);
        }
    }
}
