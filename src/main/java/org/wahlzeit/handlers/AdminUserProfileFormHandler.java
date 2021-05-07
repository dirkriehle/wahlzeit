/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * A handler class for a specific web form.
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
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> args = us.getSavedArgs();

		String userId = us.getAndSaveAsString(args, "userId");
		User user = UserManager.getInstance().getUserByName(userId);
	
		Photo photo = user.getUserPhoto();
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));

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
	protected String doHandlePost(UserSession us, Map args) {
		UserManager um = UserManager.getInstance();
		String userId = us.getAndSaveAsString(args, "userId");
		User user = um.getUserByName(userId);
		
		String status = us.getAndSaveAsString(args, User.STATUS);
		String rights = us.getAndSaveAsString(args, User.RIGHTS);
		String gender = us.getAndSaveAsString(args, User.GENDER);
		String language = us.getAndSaveAsString(args, User.LANGUAGE);
		String emailAddress = us.getAndSaveAsString(args, User.EMAIL_ADDRESS);
		String homePage = us.getAndSaveAsString(args, User.HOME_PAGE);
		String notifyAboutPraise = us.getAndSaveAsString(args, User.NOTIFY_ABOUT_PRAISE);
		
		if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.SHOW_ADMIN_PAGE_NAME;
		} else if (!StringUtil.isValidURL(homePage)) {
			us.setMessage(us.cfg().getUrlIsInvalid());
			return PartUtil.SHOW_ADMIN_PAGE_NAME;
		}
		
		user.setStatus(UserStatus.getFromString(status));
		user.setRights(AccessRights.getFromString(rights));
		user.setGender(Gender.getFromString(gender));
		user.setLanguage(Language.getFromString(language));
		user.setEmailAddress(EmailAddress.getFromString(emailAddress));
		user.setHomePage(StringUtil.asUrl(homePage));
		user.setNotifyAboutPraise((notifyAboutPraise != null) && notifyAboutPraise.equals("on"));

		um.removeUser(user);
		user = um.getUserByName(userId);
		us.setSavedArg("userId", userId);

		StringBuffer sb = UserLog.createActionEntry("AdminUserProfile");
		UserLog.addUpdatedObject(sb, "User", user.getName());
		UserLog.log(sb);
		
		us.setMessage(us.cfg().getProfileUpdateSucceeded());

		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}
	
}
