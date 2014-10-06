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
import org.wahlzeit.services.*;
import org.wahlzeit.services.mailing.*;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

/**
 * 
 * @author dirkriehle
 *
 */
public class TellFriendFormHandler extends AbstractWebFormHandler {
	
	/**
	 * 
	 */
	public static final String EMAIL_FROM = "emailFrom";
	public static final String EMAIL_TO = "emailTo";
	public static final String EMAIL_SUBJECT = "emailSubject";
	public static final String EMAIL_BODY = "emailBody";
	
	/**
	 *
	 */
	public TellFriendFormHandler() {
		initialize(PartUtil.TELL_FRIEND_FORM_FILE, AccessRights.GUEST);
	}

	/**
	 * @methodtype command
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);

		part.maskAndAddStringFromArgsWithDefault(args, EMAIL_FROM, us.getEmailAddressAsString());
		part.maskAndAddStringFromArgs(args, EMAIL_TO);
		part.maskAndAddStringFromArgsWithDefault(args, EMAIL_SUBJECT, us.cfg().getTellFriendEmailSubject());
		
		String emailText = us.cfg().getTellFriendEmailWebsite() + "\n\n" + us.getSiteUrl() + "\n\n";

		String id = us.getAsString(args, Photo.ID);
		if (!StringUtil.isNullOrEmptyString(id) && PhotoManager.hasPhoto(id)) {
			emailText += (us.cfg().getTellFriendEmailPhoto() + "\n\n" + us.getSiteUrl() + id + ".html" + "\n\n");
		}
		
		part.addString(Photo.ID, id);
		Photo photo = PhotoManager.getPhoto(id);
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));

		part.maskAndAddStringFromArgsWithDefault(args, EMAIL_BODY, emailText);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String yourEmailAddress = us.getAndSaveAsString(args, EMAIL_FROM);
		String friendsEmailAddress = us.getAndSaveAsString(args, EMAIL_TO);
		String emailSubject = us.getAndSaveAsString(args, EMAIL_SUBJECT);
		String emailBody = us.getAndSaveAsString(args, EMAIL_BODY);
		
		if (StringUtil.isNullOrEmptyString(yourEmailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsMissing());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (!StringUtil.isValidStrictEmailAddress(yourEmailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(friendsEmailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsMissing());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (!StringUtil.isValidStrictEmailAddress(friendsEmailAddress)) {
			us.setMessage(us.cfg().getEmailAddressIsInvalid());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} if ((emailSubject.length() > 128) || (emailBody.length() > 1024)) {
			us.setMessage(us.cfg().getInputIsTooLong());
			return PartUtil.TELL_FRIEND_PAGE_NAME;			
		}

		EmailAddress from = EmailAddress.getFromString(yourEmailAddress);
		EmailAddress to = EmailAddress.getFromString(friendsEmailAddress);

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, us.cfg().getAuditEmailAddress(), emailSubject, emailBody);

		us.setEmailAddress(from);

		UserLog.logPerformedAction("TellFriend");

		us.setTwoLineMessage(us.cfg().getEmailWasSent() + friendsEmailAddress + "! ", us.cfg().getKeepGoing());
		
		return PartUtil.TELL_FRIEND_PAGE_NAME;
	}
	
}
