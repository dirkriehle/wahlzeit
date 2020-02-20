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

import java.util.*;

import org.wahlzeit.utils.StringUtil;

/**
 * 
 * @author dirkriehle
 *
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
	protected List<PhotoId> displayablePhotoIds = Collections.EMPTY_LIST;
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
		displayablePhotoIds = Collections.EMPTY_LIST;
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
