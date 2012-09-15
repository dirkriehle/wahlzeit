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
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Map args = ctx.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);
		
//		part.addString(WebContext.MESSAGE, ctx.getMessage());

		part.maskAndAddStringFromArgs(args, User.NAME);
	}

	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		String userName = ctx.getAndSaveAsString(args, User.NAME);
		String password = ctx.getAndSaveAsString(args, User.PASSWORD);
		
		UserManager userManager = UserManager.getInstance();
		
		if (StringUtil.isNullOrEmptyString(userName)) {
			ctx.setMessage(ctx.cfg().getFieldIsMissing());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (!StringUtil.isLegalUserName(userName)) {
			ctx.setMessage(ctx.cfg().getLoginIsIncorrect());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(password)) {
			ctx.setMessage(ctx.cfg().getFieldIsMissing());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (!userManager.hasUserByName(userName)) {
			ctx.setMessage(ctx.cfg().getLoginIsIncorrect());
			return PartUtil.LOGIN_PAGE_NAME;
		}
		
		User user = userManager.getUserByName(userName);
		if (!user.hasPassword(password)) {
			ctx.setMessage(ctx.cfg().getLoginIsIncorrect());
			return PartUtil.LOGIN_PAGE_NAME;
		} else if (user.getStatus().isDisabled()) {
			ctx.setMessage(ctx.cfg().getUserIsDisabled());
			return PartUtil.LOGIN_PAGE_NAME;
		}

		ctx.setClient(user);
		if (!user.isConfirmed() && user.needsConfirmation()) {
			if (ctx.hasConfirmationCode()) {
				if (user.getConfirmationCode() == ctx.getConfirmationCode()) {
					user.setConfirmed();
					ctx.setTwoLineMessage(ctx.cfg().getConfirmAccountSucceeded(), ctx.cfg().getContinueWithShowUserHome());
				} else {
					UserManager.getInstance().emailConfirmationRequest(ctx, user);
					ctx.setTwoLineMessage(ctx.cfg().getConfirmAccountFailed(), ctx.cfg().getConfirmationEmailWasSent());
				}
				ctx.clearConfirmationCode();
			} else {
				UserManager.getInstance().emailConfirmationRequest(ctx, user);
				ctx.setTwoLineMessage(ctx.cfg().getConfirmationEmailWasSent(), ctx.cfg().getContinueWithShowUserHome());
			}
			return PartUtil.SHOW_NOTE_PAGE_NAME;
		}
		
		UserLog.logPerformedAction("Login");
		
		return PartUtil.SHOW_USER_HOME_PAGE_NAME;
	}
	
}
