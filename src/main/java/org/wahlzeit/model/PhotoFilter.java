/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.util.*;

import org.wahlzeit.utils.StringUtil;

/**
 * A class to specify a photo filter.
 * A photo filter captures selection ("filtering") criteria for photos.
 */
public class PhotoFilter {

	/**
	 * 
	 */
	public static final String USER_NAME = "userName";
	public static final String TAGS = "tags";
	
	/**
	 *
	 */
	protected String userName = "";
	protected Tags tags = Tags.EMPTY_TAGS;
	
	/**
	 * 
	 */
	protected List<PhotoId> displayablePhotoIds = new LinkedList<PhotoId>();
	protected List<PhotoId> processedPhotoIds = new LinkedList<PhotoId>();
	
	/**
	 * 
	 */
	protected Random randomNumber = new Random(System.currentTimeMillis());
			
	/**
	 * 
	 */
	public PhotoFilter() {
		// do nothing
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
	}
	
	/**
	 * 
	 */
	public List<String> getFilterConditions() {
		List<String> filterConditions = new ArrayList<String>();
		
		collectFilterConditions(filterConditions);

		return filterConditions;
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
		displayablePhotoIds = new LinkedList<PhotoId>();
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
		return processedPhotoIds.contains(photoId);
	}
	
	/**
	 * 
	 */
	public void addProcessedPhoto(Photo photo) {
		processedPhotoIds.add(photo.getId());
		if (displayablePhotoIds != null) {
			displayablePhotoIds.remove(photo.getId());
		}
	}
	
}
