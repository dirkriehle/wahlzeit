/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * A handler class for a specific web form.
 */
public class ShowUserPhotoFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public ShowUserPhotoFormHandler() {
		initialize(PartUtil.SHOW_USER_PHOTO_FORM_FILE, AccessRights.USER);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Photo photo = us.getPhoto();
		String id = photo.getId().asString();
		part.addString(Photo.ID, id);
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));
		
		part.addString(Photo.PRAISE, photo.getPraiseAsString(us.cfg()));
		
		String tags = photo.getTags().asString();
		tags = !StringUtil.isNullOrEmptyString(tags) ? tags : us.cfg().getNoTags();
		part.maskAndAddString(Photo.TAGS, tags);
		
		String photoStatus = us.cfg().asValueString(photo.getStatus());
		part.addString(Photo.STATUS, photoStatus);

		part.addString(Photo.UPLOADED_ON, us.cfg().asDateString(photo.getCreationTime()));
		part.addString(Photo.LINK, HtmlUtil.asHref(getResourceAsRelativeHtmlPathString(id)));
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedPost(UserSession us, Map args) {
		String id = us.getAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);
		return (photo != null) && us.isPhotoOwner(photo);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String result = PartUtil.SHOW_USER_HOME_PAGE_NAME;
		
		String id = us.getAndSaveAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);

		UserManager userManager = UserManager.getInstance();
		User user = userManager.getUserByName(photo.getOwnerName());
		if (us.isFormType(args, "edit")) {
			us.setPhoto(photo);
			result = PartUtil.EDIT_USER_PHOTO_PAGE_NAME;
		} else if (us.isFormType(args, "tell")) {
			us.setPhoto(photo);
			result = PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (us.isFormType(args, "select")) {
			user.setUserPhoto(photo);
			userManager.saveUser(user);
			UserLog.logPerformedAction("SelectUserPhoto");
		} else if (us.isFormType(args, "delete")) {
			photo.setStatus(photo.getStatus().asDeleted(true));
			PhotoManager.getInstance().savePhoto(photo);
			if (user.getUserPhoto() == photo) {
				user.setUserPhoto(null);
			}
			userManager.saveUser(user);
			UserLog.logPerformedAction("DeleteUserPhoto");
		}
		
		return result;
	}
	
}
