/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.services.mailing.*;
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
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map<String, Object> savedArgs = us.getSavedArgs();
		part.addStringFromArgs(savedArgs, UserSession.MESSAGE);
		part.maskAndAddStringFromArgs(savedArgs, User.NAME);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		UserManager userManager = UserManager.getInstance();	

		String userName = us.getAndSaveAsString(args, User.NAME);
		if (StringUtil.isNullOrEmptyString(userName)) {
			us.setMessage(us.cfg().getFieldIsMissing());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		} else if (!userManager.hasUserByName(userName)) {
			us.setMessage(us.cfg().getUserNameIsUnknown());
			return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
		}
		
		User user = userManager.getUserByName(userName);
		
		EmailAddress from = us.cfg().getModeratorEmailAddress();
		EmailAddress to = user.getEmailAddress();

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, us.cfg().getAuditEmailAddress(), us.cfg().getSendPasswordEmailSubject(), user.getPassword());

		UserLog.logPerformedAction("EmailPassword");
		
		us.setTwoLineMessage(us.cfg().getPasswordWasEmailed(), us.cfg().getContinueWithShowPhoto());

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}

}
