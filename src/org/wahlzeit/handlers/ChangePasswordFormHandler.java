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
