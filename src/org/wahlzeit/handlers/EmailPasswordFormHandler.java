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
public class EmailPasswordFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public EmailPasswordFormHandler() {
		initialize(PartUtil.EMAIL_PASSWORD_FORM_FILE, AccessRights.GUEST);
	}

	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Map<String, Object> savedArgs = ctx.getSavedArgs();
		part.addStringFromArgs(savedArgs, UserSession.MESSAGE);
		part.maskAndAddStringFromArgs(savedArgs, User.NAME);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		UserManager userManager = UserManager.getInstance();	

		String userName = ctx.getAndSaveAsString(args, User.NAME);
		if (StringUtil.isNullOrEmptyString(userName)) {
			ctx.setMessage(ctx.cfg().getFieldIsMissing());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		} else if (!userManager.hasUserByName(userName)) {
			ctx.setMessage(ctx.cfg().getUserNameIsUnknown());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		}
		
		User user = userManager.getUserByName(userName);
		
		EmailServer emailServer = AbstractEmailServer.getInstance();
		EmailAddress from = ctx.cfg().getModeratorEmailAddress();
		EmailAddress to = user.getEmailAddress();
		emailServer.sendEmail(from, to, ctx.cfg().getAuditEmailAddress(), ctx.cfg().getSendPasswordEmailSubject(), user.getPassword());

		UserLog.logPerformedAction("EmailPassword");
		
		ctx.setTwoLineMessage(ctx.cfg().getPasswordWasEmailed(), ctx.cfg().getContinueWithShowPhoto());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}

}
