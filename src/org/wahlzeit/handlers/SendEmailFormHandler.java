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
import org.wahlzeit.webparts.WebPart;

/**
 * 
 * @author dirkriehle
 *
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
