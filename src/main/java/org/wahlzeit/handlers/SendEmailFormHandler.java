/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.mailing.*;
import org.wahlzeit.webparts.WebPart;

/**
 * A handler class for a specific web form.
 */
public class SendEmailFormHandler extends AbstractWebFormHandler {
	
	/**
	 * 
	 */
	public static final String USER = "user";
	public static final String USER_LANGUAGE = "userLanguage";
	public static final String EMAIL_SUBJECT = "emailSubject";
	public static final String EMAIL_BODY = "emailBody";
	
	/**
	 *
	 */
	public SendEmailFormHandler() {
		initialize(PartUtil.SEND_EMAIL_FORM_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	public boolean isWellFormedGet(UserSession us, String link, Map args) {
		return hasSavedPhotoId(us);
	}

	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		if(!(us.getClient() instanceof User)) {
			us.setHeading(us.cfg().getInformation());
			us.setMessage(us.cfg().getNeedToSignupFirst());
			return PartUtil.SHOW_NOTE_PAGE_NAME;
		}
		
		return super.doHandleGet(us, link, args);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		Map args = us.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);
		
		String id = us.getAndSaveAsString(args, Photo.ID);
		part.addString(Photo.ID, id);
		Photo photo = PhotoManager.getPhoto(id);
		part.addString(Photo.THUMB, getPhotoThumb(us, photo));

		part.maskAndAddString(USER, photo.getOwnerName());
		
		User user = (User) us.getClient();
		part.addString(USER_LANGUAGE, us.cfg().asValueString(user.getLanguage()));
		
		part.maskAndAddStringFromArgs(args, EMAIL_SUBJECT);
		part.maskAndAddStringFromArgs(args, EMAIL_BODY);
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedPost(UserSession us, Map args) {
		return PhotoManager.getPhoto(us.getAsString(args, Photo.ID)) != null;
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		String id = us.getAndSaveAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);

		String emailSubject = us.getAndSaveAsString(args, EMAIL_SUBJECT);
		String emailBody = us.getAndSaveAsString(args, EMAIL_BODY);
		if ((emailSubject.length() > 128) || (emailBody.length() > 1024)) {
			us.setMessage(us.cfg().getInputIsTooLong());
			return PartUtil.SEND_EMAIL_PAGE_NAME;			
		}

		UserManager userManager = UserManager.getInstance();
		User toUser = userManager.getUserByName(photo.getOwnerName());
		User fromUser = (User) us.getClient();

		emailSubject = us.cfg().getSendEmailSubjectPrefix() + emailSubject;
		emailBody = us.cfg().getSendEmailBodyPrefix() + emailBody + us.cfg().getSendEmailBodyPostfix();

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(fromUser.getEmailAddress(), toUser.getEmailAddress(), us.cfg().getAuditEmailAddress(), emailSubject, emailBody);

		UserLog.logPerformedAction("SendEmail");
		
		us.setMessage(us.cfg().getEmailWasSent() + toUser.getName() + "!");
		
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
