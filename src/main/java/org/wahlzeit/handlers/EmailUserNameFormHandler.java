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

import org.wahlzeit.model.*;
import org.wahlzeit.services.mailing.*;
import org.wahlzeit.services.EmailAddress;
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
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> savedArgs = us.getSavedArgs();
		part.addStringFromArgs(savedArgs, UserSession.MESSAGE);
		part.maskAndAddStringFromArgs(savedArgs, User.EMAIL_ADDRESS);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String emailAddress = us.getAndSaveAsString(args, User.EMAIL_ADDRESS);
		if (StringUtil.isNullOrEmptyString(emailAddress)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		} else if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		}

		UserManager userManager = UserManager.getInstance();	
		User user = userManager.getUserByEmailAddress(emailAddress);
		if (user == null) {
			us.setMessage(us.cfg().getUnknownEmailAddress());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		}

		EmailService emailService = EmailServiceManager.getDefaultService();
		
		EmailAddress from = us.cfg().getModeratorEmailAddress();
		EmailAddress to = user.getEmailAddress();
		emailService.sendEmailIgnoreException(from, to, us.cfg().getAuditEmailAddress(), us.cfg().getSendUserNameEmailSubject(), user.getName());

		UserLog.logPerformedAction("EmailUserName");

		us.setTwoLineMessage(us.cfg().getUserNameWasEmailed(), us.cfg().getContinueWithShowPhoto());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
