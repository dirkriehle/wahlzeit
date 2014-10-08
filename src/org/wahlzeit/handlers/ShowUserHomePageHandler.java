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

package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author dirkriehle
 *
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
			for (int i = 0; i < photos.length; i++) {
				Photo photo = photos[i];
				if (!photo.getStatus().isDeleted()) {
					part = makeUserPhotoForm(us, photos[i]);
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
