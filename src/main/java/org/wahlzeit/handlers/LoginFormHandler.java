/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle https://dirkriehle.com
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
 */
public class LoginFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public LoginFormHandler() {
		initialize(PartUtil.LOGIN_FORM_FILE, AccessRights.GUEST);
	}

	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);
		
//		part.addString(WebContext.MESSAGE, ctx.getMessage());

		part.maskAndAddStringFromArgs(args, User.NAME);
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String userName = us.getAndSaveAsString(args, User.NAME);
		String password = us.getAndSaveAsString(args, User.PASSWORD);
		
		UserManager userManager = UserManager.getInstance();
		
		if (StringUtil.isNullOrEmptyString(userName)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (!StringUtil.isLegalUserName(userName)) {
			us.setMessage(us.cfg().getLoginIsIncorrect());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(password)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (!userManager.hasUserByName(userName)) {
			us.setMessage(us.cfg().getLoginIsIncorrect());
			return PartUtil.LOGIN_PAGE_NAME;
		}
		
		User user = userManager.getUserByName(userName);
		if (!user.hasPassword(password)) {
			us.setMessage(us.cfg().getLoginIsIncorrect());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (user.getStatus().isDisabled()) {
			us.setMessage(us.cfg().getUserIsDisabled());
			return PartUtil.LOGIN_PAGE_NAME;
		}

		us.setClient(user);
		if (!user.isConfirmed() && user.needsConfirmation()) {
			if (us.hasConfirmationCode()) {
				if (user.getConfirmationCode() == us.getConfirmationCode()) {
					user.setConfirmed();
					us.setTwoLineMessage(us.cfg().getConfirmAccountSucceeded(), us.cfg().getContinueWithShowUserHome());
				} else {
					UserManager.getInstance().emailConfirmationRequest(us, user);
					us.setTwoLineMessage(us.cfg().getConfirmAccountFailed(), us.cfg().getConfirmationEmailWasSent());
				}
				us.clearConfirmationCode();
			} else {
				UserManager.getInstance().emailConfirmationRequest(us, user);
				us.setTwoLineMessage(us.cfg().getConfirmationEmailWasSent(), us.cfg().getContinueWithShowUserHome());
			}
			return PartUtil.SHOW_NOTE_PAGE_NAME;
		}
		
		UserLog.logPerformedAction("Login");
		
		return PartUtil.SHOW_USER_HOME_PAGE_NAME;
	}
	
}
