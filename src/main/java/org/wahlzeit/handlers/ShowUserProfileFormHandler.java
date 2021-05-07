/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
 * A handler class for a specific web form.
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
	protected void doMakeWebPart(UserSession us, WebPart part) {
		User user = (User) us.getClient();

		Photo photo = user.getUserPhoto();
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));
		
		part.maskAndAddString(User.NAME, user.getName());
		part.addString(User.STATUS, us.cfg().asValueString(user.getStatus()));
		part.maskAndAddString(User.EMAIL_ADDRESS, user.getEmailAddress().asString());
		part.addString(User.MEMBER_SINCE, us.cfg().asDateString(user.getCreationTime()));
		part.addString(User.NOTIFY_ABOUT_PRAISE, us.cfg().asYesOrNoString(user.getNotifyAboutPraise()));
		part.addString(User.HOME_PAGE, HtmlUtil.asHref(user.getHomePage().toString()));
		part.addString(User.NO_PHOTOS, String.valueOf(user.getNoPhotos()));
		part.addString(User.GENDER, us.cfg().asValueString(user.getGender()));
		part.addString(User.LANGUAGE, us.cfg().asValueString(user.getLanguage()));
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		return PartUtil.EDIT_USER_PROFILE_PAGE_NAME;
	}
	
}
