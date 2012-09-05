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
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author driehle
 *
 */
public class ShowUserPhotoFormHandler extends AbstractWebFormHandler {
	
	/**
	 *
	 */
	public ShowUserPhotoFormHandler() {
		initialize(PartUtil.SHOW_USER_PHOTO_FORM_FILE, AccessRights.USER);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession ctx, WebPart part) {
		Photo photo = ctx.getPhoto();
		String id = photo.getId().asString();
		part.addString(Photo.ID, id);
		part.addString(Photo.THUMB, getPhotoThumb(ctx, photo));
		
		part.addString(Photo.PRAISE, photo.getPraiseAsString(ctx.cfg()));
		
		String tags = photo.getTags().asString();
		tags = !StringUtil.isNullOrEmptyString(tags) ? tags : ctx.cfg().getNoTags();
		part.maskAndAddString(Photo.TAGS, tags);
		
		String photoStatus = ctx.cfg().asValueString(photo.getStatus());
		part.addString(Photo.STATUS, photoStatus);

		part.addString(Photo.UPLOADED_ON, ctx.cfg().asDateString(photo.getCreationTime()));
		part.addString(Photo.LINK, HtmlUtil.asHref(SysConfig.getLinkAsUrlString(id)));
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedPost(UserSession ctx, Map args) {
		String id = ctx.getAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);
		return (photo != null) && ctx.isPhotoOwner(photo);
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		String result = PartUtil.SHOW_USER_HOME_PAGE_NAME;
		
		String id = ctx.getAndSaveAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);

		UserManager userManager = UserManager.getInstance();
		User user = userManager.getUserByName(photo.getOwnerName());
		if (ctx.isFormType(args, "edit")) {
			ctx.setPhoto(photo);
			result = PartUtil.EDIT_USER_PHOTO_PAGE_NAME;
		} else if (ctx.isFormType(args, "tell")) {
			ctx.setPhoto(photo);
			result = PartUtil.TELL_FRIEND_PAGE_NAME;
		} else if (ctx.isFormType(args, "select")) {
			user.setUserPhoto(photo);
			userManager.saveUser(user);
			UserLog.logPerformedAction("SelectUserPhoto");
		} else if (ctx.isFormType(args, "delete")) {
			photo.setStatus(photo.getStatus().asDeleted(true));
			if (user.getUserPhoto() == photo) {
				user.setUserPhoto(null);
			}
			userManager.saveUser(user);
			UserLog.logPerformedAction("DeleteUserPhoto");
		}
		
		return result;
	}
	
}
