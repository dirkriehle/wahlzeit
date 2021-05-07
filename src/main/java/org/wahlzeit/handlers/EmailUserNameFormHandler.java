/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.mailing.*;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

/**
 * A handler class for a specific web form.
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
