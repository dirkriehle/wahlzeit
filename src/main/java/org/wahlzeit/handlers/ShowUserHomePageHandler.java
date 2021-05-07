/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * A handler class for a specific web page.
 */
public class ShowUserHomePageHandler extends AbstractWebPageHandler {
	
	/**
	 *
	 */
	public ShowUserHomePageHandler() {
		initialize(PartUtil.SHOW_USER_HOME_PAGE_FILE, AccessRights.USER);
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		Writable part = makeUserProfileForm(us);
		page.addWritable("profile", part);
		
		User user = (User) us.getClient();
		Photo[] photos = user.getPhotos();
		boolean wasEmpty = true;
		if (photos.length != 0) {
			WritableList list = new WritableList();
			for (Photo photo : photos) {
				// load it from the PhotoManager to make sure the same copy is used
				photo = PhotoManager.getInstance().getPhotoFromId(photo.getId());
				if (photo != null && !photo.getStatus().isDeleted()) {
					part = makeUserPhotoForm(us, photo);
					list.append(part);
					wasEmpty = false;
				}
			}
			page.addWritable("photos", list);
		}
		
		if (wasEmpty){
			page.addString("photos", HtmlUtil.asP(us.cfg().getNoPhotoUploaded()));
		}	
	}
	
	/**
	 * 
	 */
	protected Writable makeUserProfileForm(UserSession us) {
		WebFormHandler handler = getFormHandler(PartUtil.SHOW_USER_PROFILE_FORM_NAME);
		return handler.makeWebPart(us);
	}
	
	
	/**
	 * 
	 */
	protected Writable makeUserPhotoForm(UserSession us, Photo photo) {
		us.setPhoto(photo);
		WebFormHandler handler = getFormHandler(PartUtil.SHOW_USER_PHOTO_FORM_NAME);
		return handler.makeWebPart(us);
	}
	
}
