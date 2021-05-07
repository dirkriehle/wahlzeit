/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserLog;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author dirkriehle
 *
 */
public class ChangePasswordFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public ChangePasswordFormHandler() {
		initialize(PartUtil.CHANGE_PASSWORD_FORM_FILE, AccessRights.USER);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);

		User user = (User) us.getClient();
		part.addStringFromArgsWithDefault(args, User.PASSWORD, user.getPassword());
		part.addStringFromArgsWithDefault(args, User.PASSWORD_AGAIN, user.getPassword());
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String password = us.getAndSaveAsString(args, User.PASSWORD);
		String passwordAgain = us.getAndSaveAsString(args, User.PASSWORD_AGAIN);
		
		if (StringUtil.isNullOrEmptyString(password)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.CHANGE_PASSWORD_PAGE_NAME;
		} else if (!password.equals(passwordAgain)) {
			us.setMessage(us.cfg().getPasswordsDontMatch());
			return PartUtil.CHANGE_PASSWORD_PAGE_NAME;
		} else if (!StringUtil.isLegalPassword(password)) {
			us.setMessage(us.cfg().getInputIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		}

		User user = (User) us.getClient();
		user.setPassword(password);
		
		UserLog.logPerformedAction("ChangePassword");
		
		us.setTwoLineMessage(us.cfg().getPasswordChangeSucceeded(), us.cfg().getContinueWithShowUserHome());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
