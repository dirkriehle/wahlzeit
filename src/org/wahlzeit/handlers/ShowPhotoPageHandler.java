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
 * @author dirkriehle
 *
 */
public class ShowPhotoPageHandler extends AbstractWebPageHandler implements WebFormHandler {
	
	/**
	 * 
	 */
	public ShowPhotoPageHandler() {
		initialize(PartUtil.SHOW_PHOTO_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession ctx, String link, Map args) {
		Photo photo = null;
		
		String arg = ctx.getAsString(args, "prior");
		if (!StringUtil.isNullOrEmptyString(arg)) {
			ctx.setPriorPhoto(PhotoManager.getPhoto(arg));
		}
		
		if (!link.equals(PartUtil.SHOW_PHOTO_PAGE_NAME)) {
			photo = PhotoManager.getPhoto(link);
		}
		
		if (photo == null) {
			PhotoManager photoManager = PhotoManager.getInstance();
			PhotoFilter filter = ctx.getPhotoFilter();
			photo = photoManager.getVisiblePhoto(filter);
			if (photo != null) {
				link = photo.getId().asString();
			}
		}

		ctx.setPhoto(photo);
		
		return link;
	}
	
	/**
	 * 
	 */
	protected boolean isToShowAds(UserSession ctx) {
		return ctx.getPriorPhoto() != null;
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession ctx, WebPart page) {
		Photo photo = ctx.getPhoto();

		makeLeftSidebar(ctx, page);

		makePhoto(ctx, page);
		
		if (photo != null && photo.isVisible()) {
			makePhotoCaption(ctx, page);
			makeEngageGuest(ctx, page);

			String photoId = photo.getId().asString();
			page.addString(Photo.ID, photoId);

			Tags tags = photo.getTags();
			page.addString(Photo.DESCRIPTION, getPhotoSummary(ctx, photo));
			page.addString(Photo.KEYWORDS, tags.asString(false, ','));

			ctx.addDisplayedPhoto(photo);
		}
		
		makeRightSidebar(ctx, page);
	}
	
	/**
	 * 
	 */
	protected void makeLeftSidebar(UserSession ctx, WebPart page) {
		WritableList parts = new WritableList();
		
		Photo photo = ctx.getPriorPhoto();
		if (photo != null) {
			parts.append(makePriorPhotoInfo(ctx));
		} else {
			parts.append(createWebPart(ctx, PartUtil.BLURP_INFO_FILE));
		}

		WebFormHandler handler = getFormHandler(PartUtil.FILTER_PHOTOS_FORM_NAME);
		Writable filterPhotos = handler.makeWebPart(ctx);
		parts.append(filterPhotos);

		parts.append(createWebPart(ctx, PartUtil.LINKS_INFO_FILE));
		
		page.addWritable("sidebar", parts);
	}
	
	/**
	 * 
	 */
	protected void makePhoto(UserSession ctx, WebPart page) {
		PhotoSize pagePhotoSize = ctx.getPhotoSize();

		Photo photo = ctx.getPhoto();
		if (photo == null) {
			page.addString("mainWidth", String.valueOf(pagePhotoSize.getMaxPhotoWidth()));
			WebPart done = createWebPart(ctx, PartUtil.DONE_INFO_FILE);
			page.addWritable(Photo.IMAGE, done);
			return;
		}
		
		Client client = ctx.getClient();
		if (!photo.isVisible() && !client.hasModeratorRights() && !ctx.isPhotoOwner(photo)) {
			page.addString("mainWidth", String.valueOf(pagePhotoSize.getMaxPhotoWidth()));
			WebPart done = createWebPart(ctx, PartUtil.HIDDEN_INFO_FILE);
			page.addWritable(Photo.IMAGE, done);
			return;
		}
		
		PhotoSize maxPhotoSize = photo.getMaxPhotoSize();
		PhotoSize photoSize = (maxPhotoSize.isSmaller(pagePhotoSize)) ? maxPhotoSize : pagePhotoSize;
		String imageLink = getPhotoLink(photo, photoSize);
		page.addString(Photo.IMAGE, HtmlUtil.asImg(imageLink));
	}
	
	/**
	 * 
	 */
	protected void makePhotoCaption(UserSession ctx, WebPart page) {
		Photo photo = ctx.getPhoto();
		// String photoId = photo.getId().asString();
			
		WebPart caption = createWebPart(ctx, PartUtil.CAPTION_INFO_FILE);
		caption.addString(Photo.CAPTION, getPhotoCaption(ctx, photo));
		page.addWritable(Photo.CAPTION, caption);
	}

	/**
	 * 
	 */
	protected void makeEngageGuest(UserSession ctx, WebPart page) {
		Photo photo = ctx.getPhoto();
		String photoId = photo.getId().asString();

		WebPart engageGuest = createWebPart(ctx, PartUtil.ENGAGE_GUEST_FORM_FILE);
		engageGuest.addString(Photo.LINK, HtmlUtil.asHref(SysConfig.getLinkAsUrlString(photoId)));
		engageGuest.addString(Photo.ID, photoId);

		page.addWritable("engageGuest", engageGuest);
	}
	
	/**
	 * 
	 */
	protected void makeRightSidebar(UserSession ctx, WebPart page) {
		String handlerName = PartUtil.NULL_FORM_NAME;
		Photo photo = ctx.getPhoto();
		if (photo != null) {
			handlerName = PartUtil.PRAISE_PHOTO_FORM_NAME;
		}

		WebFormHandler handler = getFormHandler(handlerName);
		Writable praisePhotoForm = handler.makeWebPart(ctx);
		page.addWritable("praisePhoto", praisePhotoForm);
	}

	
	/**
	 * 
	 */
	protected WebPart makePriorPhotoInfo(UserSession ctx) {
		WebPart result = createWebPart(ctx, PartUtil.PHOTO_INFO_FILE);

		Photo photo = ctx.getPriorPhoto();
		// String id = photo.getId().asString();

		result.addString(Photo.PRAISE, photo.getPraiseAsString(ctx.cfg()));
		result.addString(Photo.THUMB, getPhotoThumb(ctx, photo));
		result.addString(Photo.CAPTION, getPhotoCaption(ctx, photo));
			
		ctx.setPriorPhoto(null); // reset so you don't get repeats

		return result;
	}

	/**
	 * 
	 */
	public String handlePost(UserSession ctx, Map args) {
		String result = PartUtil.DEFAULT_PAGE_NAME;
		
		String id = ctx.getAndSaveAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);
		if (photo != null) {
			if (ctx.isFormType(args, "flagPhotoLink")) {
				result = PartUtil.FLAG_PHOTO_PAGE_NAME;
			} else if (ctx.isFormType(args, "tellFriendLink")) {
				result = PartUtil.TELL_FRIEND_PAGE_NAME;
			} else if (ctx.isFormType(args, "sendEmailLink")) {
				result = PartUtil.SEND_EMAIL_PAGE_NAME;
			}
		}

		return result;
	}
	
}
