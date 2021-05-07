/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;



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
		ModelConfig cfg = us.cfg();
		
		if (StringUtil.isNullOrEmptyString(userName)) {
			us.setMessage(cfg.getFieldIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(password)) {
			us.setMessage(cfg.getFieldIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(emailAddress)) {
			us.setMessage(cfg.getFieldIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (userManager.hasUserByName(userName)) {
			us.setMessage(cfg.getUserAlreadyExists());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (userManager.isReservedUserName(userName)) {
			us.setMessage(cfg.getUserNameIsReserved());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!StringUtil.isLegalUserName(userName)) {
			us.setMessage(cfg.getInputIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!password.equals(passwordAgain)) {
			us.setMessage(cfg.getPasswordsDontMatch());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!StringUtil.isLegalPassword(password)) {
			us.setMessage(cfg.getInputIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(emailAddress)) {
			us.setMessage(cfg.getEmailAddressIsMissing());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
			us.setMessage(cfg.getEmailAddressIsInvalid());
			return PartUtil.SIGNUP_PAGE_NAME;
		} else if ((terms == null) || !terms.equals("on")) {
			us.setMessage(cfg.getDidntCheckTerms());
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
