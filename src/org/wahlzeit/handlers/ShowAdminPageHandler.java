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

import java.util.Map;

import org.wahlzeit.main.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author dirkriehle
 *
 */
public class ShowAdminPageHandler extends AbstractWebPageHandler implements WebFormHandler {
	
	/**
	 * 
	 */
	public ShowAdminPageHandler() {
		initialize(PartUtil.SHOW_ADMIN_PAGE_FILE, AccessRights.ADMINISTRATOR);
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession ctx, WebPart page) {
		Map args = ctx.getSavedArgs();
		page.addStringFromArgs(args, UserSession.MESSAGE);
		
		Object userId = ctx.getSavedArg("userId");
		if(!StringUtil.isNullOrEmptyString(userId)) {
			page.addStringFromArgs(args, "userId");
			page.addWritable("object", makeAdminUserProfile(ctx));
		}

		Object photoId = ctx.getSavedArg("photoId");
		if(!StringUtil.isNullOrEmptyString(photoId)) {
			page.addStringFromArgs(args, "photoId");
			page.addWritable("object", makeAdminUserPhoto(ctx));
		}
	}
	
	/**
	 * 
	 */
	protected Writable makeAdminUserProfile(UserSession ctx) {
		WebFormHandler handler = getFormHandler(PartUtil.NULL_FORM_NAME);

		String userId = ctx.getSavedArg("userId").toString();
		User user = UserManager.getInstance().getUserByName(userId);
		if (user != null) {
			handler = getFormHandler(PartUtil.ADMIN_USER_PROFILE_FORM_NAME);
		}
		
		return handler.makeWebPart(ctx);
	}

	/**
	 * 
	 */
	protected Writable makeAdminUserPhoto(UserSession ctx) {
		WebFormHandler handler = getFormHandler(PartUtil.NULL_FORM_NAME);

		String photoId = ctx.getSavedArg("photoId").toString();
		Photo photo = PhotoManager.getPhoto(photoId);
		if (photo != null) {
			handler = getFormHandler(PartUtil.ADMIN_USER_PHOTO_FORM_NAME);
		}
		
		return handler.makeWebPart(ctx);
	}

	/**
	 * 
	 */
	public String handlePost(UserSession ctx, Map args) {
		if (!hasAccessRights(ctx, args)) {
			SysLog.logInfo("insufficient rights for POST from: " + ctx.getEmailAddressAsString());
			return getIllegalAccessErrorPage(ctx);
		}
				
		String result = PartUtil.SHOW_ADMIN_PAGE_NAME;
		
		if (ctx.isFormType(args, "adminUser")) {
			result = performAdminUserProfileRequest(ctx, args);
		} else if (ctx.isFormType(args, "adminPhoto")) {
			result = performAdminUserPhotoRequest(ctx, args);
		} else if (ctx.isFormType(args, "saveAll")) {
			result = performSaveAllRequest(ctx);
		} else if (ctx.isFormType(args, "shutdown")) {
			result = performShutdownRequest(ctx);
		}

		return result;
	}
	
	/**
	 * 
	 */
	protected String performAdminUserProfileRequest(UserSession ctx, Map args) {
		String userId = ctx.getAndSaveAsString(args, "userId");
		User user = UserManager.getInstance().getUserByName(userId);
		if (user == null) {
			ctx.setMessage(ctx.cfg().getUserNameIsUnknown());
		}
		
		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}

	/**
	 * 
	 */
	protected String performAdminUserPhotoRequest(UserSession ctx, Map args) {
		String photoId = ctx.getAndSaveAsString(args, "photoId");
		Photo photo = PhotoManager.getPhoto(photoId);
		if (photo == null) {
			ctx.setMessage(ctx.cfg().getPhotoIsUnknown());
		}
		
		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}

	/**
	 * 
	 */
	protected String performShutdownRequest(UserSession ctx) {
		SysLog.logInfo("shutting down");
		
		try {
			Wahlzeit.requestStop();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
		
		ctx.setMessage("Shutting down...");
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
	/**
	 * 
	 */
	protected String performSaveAllRequest(UserSession ctx) {
		SysLog.logInfo("saving objects");

		try {
			Wahlzeit.saveAll();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
		
		ctx.setMessage("Saved objects...");
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}	

}