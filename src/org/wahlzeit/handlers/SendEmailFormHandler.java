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
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserLog;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailServer;
import org.wahlzeit.services.AbstractEmailServer;
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
	public boolean isWellFormedGet(UserSession ctx, String link, Map args) {
		return hasSavedPhotoId(ctx);
	}

	/**
	 * 
	 */
	protected String doHandleGet(UserSession ctx, String link, Map args) {
		if(!(ctx.getClient() instanceof User)) {
			ctx.setHeading(ctx.cfg().getInformation());
			ctx.setMessage(ctx.cfg().getNeedToSignupFirst());
			return PartUtil.SHOW_NOTE_PAGE_NAME;
		}
		
		return super.doHandleGet(ctx, link, args);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Map args = ctx.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);
		
		String id = ctx.getAndSaveAsString(args, Photo.ID);
		part.addString(Photo.ID, id);
		Photo photo = PhotoManager.getPhoto(id);
		part.addString(Photo.THUMB, getPhotoThumb(ctx, photo));

		part.maskAndAddString(USER, photo.getOwnerName());
		
		User user = (User) ctx.getClient();
		part.addString(USER_LANGUAGE, ctx.cfg().asValueString(user.getLanguage()));
		
		part.maskAndAddStringFromArgs(args, EMAIL_SUBJECT);
		part.maskAndAddStringFromArgs(args, EMAIL_BODY);
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedPost(UserSession ctx, Map args) {
		return PhotoManager.getPhoto(ctx.getAsString(args, Photo.ID)) != null;
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		String id = ctx.getAndSaveAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);

		String emailSubject = ctx.getAndSaveAsString(args, EMAIL_SUBJECT);
		String emailBody = ctx.getAndSaveAsString(args, EMAIL_BODY);
		if ((emailSubject.length() > 128) || (emailBody.length() > 1024)) {
			ctx.setMessage(ctx.cfg().getInputIsTooLong());
			return PartUtil.SEND_EMAIL_PAGE_NAME;			
		}

		UserManager userManager = UserManager.getInstance();
		User toUser = userManager.getUserByName(photo.getOwnerName());
		User fromUser = (User) ctx.getClient();

		EmailServer emailServer = AbstractEmailServer.getInstance();
		emailSubject = ctx.cfg().getSendEmailSubjectPrefix() + emailSubject;
		emailBody = ctx.cfg().getSendEmailBodyPrefix() + emailBody + ctx.cfg().getSendEmailBodyPostfix();
		emailServer.sendEmail(fromUser.getEmailAddress(), toUser.getEmailAddress(), ctx.cfg().getAuditEmailAddress(), emailSubject, emailBody);

		UserLog.logPerformedAction("SendEmail");
		
		ctx.setMessage(ctx.cfg().getEmailWasSent() + toUser.getName() + "!");
		
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
