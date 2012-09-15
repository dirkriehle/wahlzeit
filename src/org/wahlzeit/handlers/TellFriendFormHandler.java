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
import org.wahlzeit.model.UserLog;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.EmailServer;
import org.wahlzeit.services.SysConfig;
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
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Map args = ctx.getSavedArgs();
		part.addStringFromArgs(args, UserSession.MESSAGE);

		part.maskAndAddStringFromArgsWithDefault(args, EMAIL_FROM, ctx.getEmailAddressAsString());
		part.maskAndAddStringFromArgs(args, EMAIL_TO);
		part.maskAndAddStringFromArgsWithDefault(args, EMAIL_SUBJECT, ctx.cfg().getTellFriendEmailSubject());
		
		String emailText = ctx.cfg().getTellFriendEmailWebsite() + "\n\n" + SysConfig.getSiteUrlAsString() + "\n\n";

		String id = ctx.getAsString(args, Photo.ID);
		if (!StringUtil.isNullOrEmptyString(id) && PhotoManager.hasPhoto(id)) {
			emailText += (ctx.cfg().getTellFriendEmailPhoto() + "\n\n" + SysConfig.getSiteUrlAsString() + id + ".html" + "\n\n");
		}
		
		part.addString(Photo.ID, id);
		Photo photo = PhotoManager.getPhoto(id);
		part.addString(Photo.THUMB, getPhotoThumb(ctx, photo));

		part.maskAndAddStringFromArgsWithDefault(args, EMAIL_BODY, emailText);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		String yourEmailAddress = ctx.getAndSaveAsString(args, EMAIL_FROM);
		String friendsEmailAddress = ctx.getAndSaveAsString(args, EMAIL_TO);
		String emailSubject = ctx.getAndSaveAsString(args, EMAIL_SUBJECT);
		String emailBody = ctx.getAndSaveAsString(args, EMAIL_BODY);
		
		if (StringUtil.isNullOrEmptyString(yourEmailAddress)) {
			ctx.setMessage(ctx.cfg().getEmailAddressIsMissing());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (!StringUtil.isValidEmailAddress(yourEmailAddress)) {
			ctx.setMessage(ctx.cfg().getEmailAddressIsInvalid());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (StringUtil.isNullOrEmptyString(friendsEmailAddress)) {
			ctx.setMessage(ctx.cfg().getEmailAddressIsMissing());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (!StringUtil.isValidEmailAddress(friendsEmailAddress)) {
			ctx.setMessage(ctx.cfg().getEmailAddressIsInvalid());
			return PartUtil.TELL_FRIEND_PAGE_NAME;
		} if ((emailSubject.length() > 128) || (emailBody.length() > 1024)) {
			ctx.setMessage(ctx.cfg().getInputIsTooLong());
			return PartUtil.TELL_FRIEND_PAGE_NAME;			
		}

		EmailAddress from = EmailAddress.getFromString(yourEmailAddress);
		EmailAddress to = EmailAddress.getFromString(friendsEmailAddress);

		EmailServer emailServer = EmailServer.getInstance();
		emailServer.sendEmail(from, to, ctx.cfg().getAuditEmailAddress(), emailSubject, emailBody);

		ctx.setEmailAddress(from);

		UserLog.logPerformedAction("TellFriend");

		ctx.setTwoLineMessage(ctx.cfg().getEmailWasSent() + friendsEmailAddress + "! ", ctx.cfg().getKeepGoing());
		
		return PartUtil.TELL_FRIEND_PAGE_NAME;
	}
	
}
