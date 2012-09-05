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
import java.net.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Gender;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserLog;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.model.UserStatus;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;
import org.wahlzeit.webparts.WebValue;



/**
 * 
 * @author driehle
 *
 */
public class AdminUserProfileFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public AdminUserProfileFormHandler() {
		initialize(PartUtil.ADMIN_USER_PROFILE_FORM_FILE, AccessRights.ADMINISTRATOR);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Map<String, Object> args = ctx.getSavedArgs();

		String userId = ctx.getAndSaveAsString(args, "userId");
		User user = UserManager.getInstance().getUserByName(userId);
	
		Photo photo = user.getUserPhoto();
		part.addString(Photo.THUMB, getPhotoThumb(ctx, photo));

		part.maskAndAddString("userId", user.getName());
		part.maskAndAddString(User.NAME, user.getName());
		part.addSelect(User.STATUS, UserStatus.class, (String) args.get(User.STATUS));
		part.addSelect(User.RIGHTS, AccessRights.class, (String) args.get(User.RIGHTS));
		part.addSelect(User.GENDER, Gender.class, (String) args.get(User.GENDER));
		part.addSelect(User.LANGUAGE, Language.class, (String) args.get(User.LANGUAGE));
		part.maskAndAddStringFromArgsWithDefault(args, User.EMAIL_ADDRESS, user.getEmailAddress().asString());
		part.maskAndAddStringFromArgsWithDefault(args, User.HOME_PAGE, user.getHomePage().toString());
		
		if (user.getNotifyAboutPraise()) {
			part.addString(User.NOTIFY_ABOUT_PRAISE, HtmlUtil.CHECKBOX_CHECK);
		}
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		UserManager um = UserManager.getInstance();
		String userId = ctx.getAndSaveAsString(args, "userId");
		User user = um.getUserByName(userId);
		
		String status = ctx.getAndSaveAsString(args, User.STATUS);
		String rights = ctx.getAndSaveAsString(args, User.RIGHTS);
		String gender = ctx.getAndSaveAsString(args, User.GENDER);
		String language = ctx.getAndSaveAsString(args, User.LANGUAGE);
		String emailAddress = ctx.getAndSaveAsString(args, User.EMAIL_ADDRESS);
		String homePage = ctx.getAndSaveAsString(args, User.HOME_PAGE);
		String notifyAboutPraise = ctx.getAndSaveAsString(args, User.NOTIFY_ABOUT_PRAISE);
		
		if (!StringUtil.isValidEmailAddress(emailAddress)) {
			ctx.setMessage(ctx.cfg().getEmailAddressIsInvalid());
			return PartUtil.SHOW_ADMIN_PAGE_NAME;
		} else if (!StringUtil.isValidURL(homePage)) {
			ctx.setMessage(ctx.cfg().getUrlIsInvalid());
			return PartUtil.SHOW_ADMIN_PAGE_NAME;
		}
		
		user.setStatus(UserStatus.getFromString(status));
		user.setRights(AccessRights.getFromString(rights));
		user.setGender(Gender.getFromString(gender));
		user.setLanguage(Language.getFromString(language));
		user.setEmailAddress(EmailAddress.getFromString(emailAddress));
		user.setHomePage(StringUtil.asUrl(homePage));
		user.setNotifyAboutPraise((notifyAboutPraise != null) && notifyAboutPraise.equals("on"));

		um.dropUser(user);
		user = um.getUserByName(userId);
		ctx.setSavedArg("userId", userId);

		StringBuffer sb = UserLog.createActionEntry("AdminUserProfile");
		UserLog.addUpdatedObject(sb, "User", user.getName());
		UserLog.log(sb);
		
		ctx.setMessage(ctx.cfg().getProfileUpdateSucceeded());

		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}
	
}
