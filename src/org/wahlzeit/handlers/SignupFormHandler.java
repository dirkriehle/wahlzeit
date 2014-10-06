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
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author dirkriehle
 *
 */
public class SignupFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public SignupFormHandler() {
		initialize(PartUtil.SIGNUP_FORM_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);
		
//		part.addString(WebContext.MESSAGE, ctx.getMessage());
		
		part.addStringFromArgs(args, User.PASSWORD);
		part.addStringFromArgs(args, User.PASSWORD_AGAIN);

		part.maskAndAddStringFromArgs(args, User.NAME);
		part.maskAndAddStringFromArgsWithDefault(args, User.EMAIL_ADDRESS, us.getEmailAddressAsString());
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String userName = us.getAndSaveAsString(args, User.NAME);
		String password = us.getAndSaveAsString(args, User.PASSWORD);
		String passwordAgain = us.getAndSaveAsString(args, User.PASSWORD_AGAIN);
		String emailAddress = us.getAndSaveAsString(args, User.EMAIL_ADDRESS);
		String terms = us.getAndSaveAsString(args, User.TERMS);
		
		UserManager userManager = UserManager.getInstance();
		
		if (StringUtil.isNullOrEmptyString(userName)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(password)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(emailAddress)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (userManager.hasUserByName(userName)) {
			us.setMessage(us.cfg().getUserAlreadyExists());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!StringUtil.isLegalUserName(userName)) {
			us.setMessage(us.cfg().getInputIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!password.equals(passwordAgain)) {
			us.setMessage(us.cfg().getPasswordsDontMatch());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!StringUtil.isLegalPassword(password)) {
			us.setMessage(us.cfg().getInputIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(emailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if ((terms == null) || !terms.equals("on")) {
			us.setMessage(us.cfg().getDidntCheckTerms());
			return PartUtil.SIGNUP_PAGE_NAME;
		}

		long confirmationCode = userManager.createConfirmationCode();
		User user = new User(userName, password, emailAddress, confirmationCode);
		userManager.addUser(user);
		
		userManager.emailWelcomeMessage(us, user);
		us.setClient(user);
		
		userManager.saveUser(user);
		
		StringBuffer sb = UserLog.createActionEntry("Signup");
		UserLog.addCreatedObject(sb, "User", userName);
		UserLog.log(sb);
		
		us.setTwoLineMessage(us.cfg().getConfirmationEmailWasSent(), us.cfg().getContinueWithShowUserHome());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
