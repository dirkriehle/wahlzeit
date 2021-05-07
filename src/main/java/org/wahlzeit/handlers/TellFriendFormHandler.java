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
 * A handler class for a specific web form.
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
