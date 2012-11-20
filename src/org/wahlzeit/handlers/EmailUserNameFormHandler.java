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
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.EmailServer;
import org.wahlzeit.services.AbstractEmailServer;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author dirkriehle
 *
 */
public class EmailUserNameFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public EmailUserNameFormHandler() {
		initialize(PartUtil.EMAIL_USER_NAME_FORM_FILE, AccessRights.GUEST);
	}

	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Map<String, Object> savedArgs = ctx.getSavedArgs();
		part.addStringFromArgs(savedArgs, UserSession.MESSAGE);
		part.maskAndAddStringFromArgs(savedArgs, User.EMAIL_ADDRESS);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		String emailAddress = ctx.getAndSaveAsString(args, User.EMAIL_ADDRESS);
		if (StringUtil.isNullOrEmptyString(emailAddress)) {
			ctx.setMessage(ctx.cfg().getFieldIsMissing());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		} else if (!StringUtil.isValidEmailAddress(emailAddress)) {
			ctx.setMessage(ctx.cfg().getEmailAddressIsInvalid());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		}

		UserManager userManager = UserManager.getInstance();	
		User user = userManager.getUserByEmailAddress(emailAddress);
		if (user == null) {
			ctx.setMessage(ctx.cfg().getUnknownEmailAddress());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		}

		EmailServer emailServer = AbstractEmailServer.getInstance();
		EmailAddress from = ctx.cfg().getModeratorEmailAddress();
		EmailAddress to = user.getEmailAddress();
		emailServer.sendEmail(from, to, ctx.cfg().getAuditEmailAddress(), ctx.cfg().getSendUserNameEmailSubject(), user.getName());

		UserLog.logPerformedAction("EmailUserName");

		ctx.setTwoLineMessage(ctx.cfg().getUserNameWasEmailed(), ctx.cfg().getContinueWithShowPhoto());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
