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
	protected void makeWebPageBody(UserSession us, WebPart page) {
		Map args = us.getSavedArgs();
		page.addStringFromArgs(args, UserSession.MESSAGE);
		
		Object userId = us.getSavedArg("userId");
		if(!StringUtil.isNullOrEmptyString(userId)) {
			page.addStringFromArgs(args, "userId");
			page.addWritable("object", makeAdminUserProfile(us));
		}

		Object photoId = us.getSavedArg("photoId");
		if(!StringUtil.isNullOrEmptyString(photoId)) {
			page.addStringFromArgs(args, "photoId");
			page.addWritable("object", makeAdminUserPhoto(us));
		}
	}
	
	/**
	 * 
	 */
	protected Writable makeAdminUserProfile(UserSession us) {
		WebFormHandler handler = getFormHandler(PartUtil.NULL_FORM_NAME);

		String userId = us.getSavedArg("userId").toString();
		User user = UserManager.getInstance().getUserByName(userId);
		if (user != null) {
			handler = getFormHandler(PartUtil.ADMIN_USER_PROFILE_FORM_NAME);
		}
		
		return handler.makeWebPart(us);
	}

	/**
	 * 
	 */
	protected Writable makeAdminUserPhoto(UserSession us) {
		WebFormHandler handler = getFormHandler(PartUtil.NULL_FORM_NAME);

		String photoId = us.getSavedArg("photoId").toString();
		Photo photo = PhotoManager.getPhoto(photoId);
		if (photo != null) {
			handler = getFormHandler(PartUtil.ADMIN_USER_PHOTO_FORM_NAME);
		}
		
		return handler.makeWebPart(us);
	}

	/**
	 * 
	 */
	public String handlePost(UserSession us, Map args) {
		if (!hasAccessRights(us, args)) {
			SysLog.logSysInfo("insufficient rights for POST from: " + us.getEmailAddressAsString());
			return getIllegalAccessErrorPage(us);
		}
				
		String result = PartUtil.SHOW_ADMIN_PAGE_NAME;
		
		if (us.isFormType(args, "adminUser")) {
			result = performAdminUserProfileRequest(us, args);
		} else if (us.isFormType(args, "adminPhoto")) {
			result = performAdminUserPhotoRequest(us, args);
		} else if (us.isFormType(args, "saveAll")) {
			result = performSaveAllRequest(us);
		} else if (us.isFormType(args, "shutdown")) {
			result = performShutdownRequest(us);
		}

		return result;
	}
	
	/**
	 * 
	 */
	protected String performAdminUserProfileRequest(UserSession us, Map args) {
		String userId = us.getAndSaveAsString(args, "userId");
		User user = UserManager.getInstance().getUserByName(userId);
		if (user == null) {
			us.setMessage(us.cfg().getUserNameIsUnknown());
		}
		
		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}

	/**
	 * 
	 */
	protected String performAdminUserPhotoRequest(UserSession us, Map args) {
		String photoId = us.getAndSaveAsString(args, "photoId");
		Photo photo = PhotoManager.getPhoto(photoId);
		if (photo == null) {
			us.setMessage(us.cfg().getPhotoIsUnknown());
		}
		
		return PartUtil.SHOW_ADMIN_PAGE_NAME;
	}

	/**
	 * 
	 */
	protected String performShutdownRequest(UserSession us) {
		SysLog.logSysInfo("shutting down");
		
		try {
			ServiceMain.getInstance().requestStop();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
		
		us.setMessage("Shutting down...");
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
	/**
	 * 
	 */
	protected String performSaveAllRequest(UserSession us) {
		SysLog.logSysInfo("saving objects");

		try {
			ServiceMain.getInstance().saveAll();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
		
		us.setMessage("Saved objects...");
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}	

}