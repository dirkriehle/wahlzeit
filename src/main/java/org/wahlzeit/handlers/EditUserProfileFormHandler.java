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
public class EditUserProfileFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public EditUserProfileFormHandler() {
		initialize(PartUtil.EDIT_USER_PROFILE_FORM_FILE, AccessRights.USER);
	}
	
	/**
	 * @methodtype command
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);

		User user = (User) us.getClient();
		part.maskAndAddString(User.NAME, user.getName());

		Photo photo = user.getUserPhoto();
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));
		part.addSelect(User.GENDER, Gender.class, (String) args.get(User.GENDER), user.getGender()); 
		part.addSelect(User.LANGUAGE, Language.class, (String) args.get(User.LANGUAGE), user.getLanguage());
		
		part.maskAndAddStringFromArgsWithDefault(args, User.EMAIL_ADDRESS, user.getEmailAddress().asString());
		
		part.addString(User.NOTIFY_ABOUT_PRAISE, HtmlUtil.asCheckboxCheck(user.getNotifyAboutPraise()));

		part.maskAndAddStringFromArgsWithDefault(args, User.HOME_PAGE, user.getHomePage().toString());
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String emailAddress = us.getAndSaveAsString(args, User.EMAIL_ADDRESS);
		String homePage = us.getAndSaveAsString(args, User.HOME_PAGE);
		String gender = us.getAndSaveAsString(args, User.GENDER);
		String language = us.getAndSaveAsString(args, User.LANGUAGE);
		
		if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.EDIT_USER_PROFILE_PAGE_NAME;
		} else if (!StringUtil.isValidURL(homePage)) {
			us.setMessage(us.cfg().getUrlIsInvalid());
			return PartUtil.EDIT_USER_PROFILE_PAGE_NAME;
		}
		
		User user = (User) us.getClient();
		
		user.setEmailAddress(EmailAddress.getFromString(emailAddress));
	
		String status = us.getAndSaveAsString(args, User.NOTIFY_ABOUT_PRAISE);
		boolean notify = (status != null) && status.equals("on");
		user.setNotifyAboutPraise(notify);

		user.setHomePage(StringUtil.asUrl(homePage));
		
		if (!StringUtil.isNullOrEmptyString(gender)) {
			user.setGender(Gender.getFromString(gender));
		}
		
		if (!StringUtil.isNullOrEmptyString(language)) {
			Language langValue = Language.getFromString(language);
			us.setConfiguration(LanguageConfigs.get(langValue));
			user.setLanguage(langValue);
		}
		
		StringBuffer sb = UserLog.createActionEntry("EditUserProfile");
		UserLog.addUpdatedObject(sb, "User", user.getName());
		UserLog.log(sb);
		
		us.setTwoLineMessage(us.cfg().getProfileUpdateSucceeded(), us.cfg().getContinueWithShowUserHome());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
