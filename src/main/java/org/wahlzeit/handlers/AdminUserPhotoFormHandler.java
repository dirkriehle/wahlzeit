/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.webparts.*;

/**
 * A specific web form class.
 */
public class AdminUserPhotoFormHandler extends AbstractWebFormHandler {

	/**
	 *
	 */
	public AdminUserPhotoFormHandler() {
		initialize(PartUtil.ADMIN_USER_PHOTO_FORM_FILE, AccessRights.ADMINISTRATOR);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> args = us.getSavedArgs();

		String photoId = us.getAndSaveAsString(args, "photoId");

		Photo photo = PhotoManager.getPhoto(photoId);
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));

		part.addString("photoId", photoId);
		part.addString(Photo.ID, photo.getId().asString());
		part.addSelect(Photo.STATUS, PhotoStatus.class, (String) args.get(Photo.STATUS));
		part.maskAndAddStringFromArgsWithDefault(args, Photo.TAGS, photo.getTags().asString());
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String id = us.getAndSaveAsString(args, "photoId");
		Photo photo = PhotoManager.getPhoto(id);
	
		String tags = us.getAndSaveAsString(args, Photo.TAGS);
		photo.setTags(new Tags(tags));
		String status = us.getAndSaveAsString(args, Photo.STATUS);
		photo.setStatus(PhotoStatus.getFromString(status));

		PhotoManager pm = PhotoManager.getInstance();
		pm.savePhoto(photo);
		
		StringBuffer sb = UserLog.createActionEntry("AdminUserPhoto");
		UserLog.addUpdatedObject(sb, "Photo", photo.getId().asString());
		UserLog.log(sb);
		
		us.setMessage(us.cfg().getPhotoUpdateSucceeded());

		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}
	
}
