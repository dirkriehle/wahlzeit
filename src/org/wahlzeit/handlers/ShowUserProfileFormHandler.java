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

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author driehle
 *
 */
public class ShowUserProfileFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public ShowUserProfileFormHandler() {
		initialize(PartUtil.SHOW_USER_PROFILE_FORM_FILE, AccessRights.USER);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		User user = (User) ctx.getClient();

		Photo photo = user.getUserPhoto();
		part.addString(Photo.THUMB, getPhotoThumb(ctx, photo));
		
		part.maskAndAddString(User.NAME, user.getName());
		part.addString(User.STATUS, ctx.cfg().asValueString(user.getStatus()));
		part.maskAndAddString(User.EMAIL_ADDRESS, user.getEmailAddress().asString());
		part.addString(User.MEMBER_SINCE, ctx.cfg().asDateString(user.getCreationTime()));
		part.addString(User.NOTIFY_ABOUT_PRAISE, ctx.cfg().asYesOrNoString(user.getNotifyAboutPraise()));
		part.addString(User.HOME_PAGE, HtmlUtil.asHref(user.getHomePage().toString()));
		part.addString(User.NO_PHOTOS, String.valueOf(user.getNoPhotos()));
		part.addString(User.GENDER, ctx.cfg().asValueString(user.getGender()));
		part.addString(User.LANGUAGE, ctx.cfg().asValueString(user.getLanguage()));
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		return PartUtil.EDIT_USER_PROFILE_PAGE_NAME;
	}
	
}
