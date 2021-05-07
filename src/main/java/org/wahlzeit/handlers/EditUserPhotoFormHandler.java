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
public class EditUserPhotoFormHandler extends AbstractWebFormHandler {

	/**
	 *
	 */
	public EditUserPhotoFormHandler() {
		initialize(PartUtil.EDIT_USER_PHOTO_FORM_FILE, AccessRights.USER);
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedGet(UserSession us, String link, Map args) {
		return hasSavedPhotoId(us);
	}

	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);

		String id = us.getAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);

		part.addString(Photo.ID, id);
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));
		
		part.addString(Photo.PRAISE, photo.getPraiseAsString(us.cfg()));
		part.maskAndAddString(Photo.TAGS, photo.getTags().asString());
		
		part.addString(Photo.IS_INVISIBLE, HtmlUtil.asCheckboxCheck(photo.getStatus().isInvisible()));
		part.addString(Photo.STATUS, us.cfg().asValueString(photo.getStatus()));
		part.addString(Photo.UPLOADED_ON, us.cfg().asDateString(photo.getCreationTime()));	
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
		String id = us.getAndSaveAsString(args, Photo.ID);
		PhotoManager pm = PhotoManager.getInstance();
		Photo photo = PhotoManager.getPhoto(id);

		String tags = us.getAndSaveAsString(args, Photo.TAGS);
		photo.setTags(new Tags(tags));

		String status = us.getAndSaveAsString(args, Photo.IS_INVISIBLE);
		boolean isInvisible = (status != null) && status.equals("on");
		PhotoStatus ps = photo.getStatus().asInvisible(isInvisible);
		photo.setStatus(ps);

		pm.savePhoto(photo);
		
		StringBuffer sb = UserLog.createActionEntry("EditUserPhoto");
		UserLog.addUpdatedObject(sb, "Photo", photo.getId().asString());
		UserLog.log(sb);
		
		us.setTwoLineMessage(us.cfg().getPhotoUpdateSucceeded(), us.cfg().getContinueWithShowUserHome());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
