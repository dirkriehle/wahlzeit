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
	protected String doHandleGet(UserSession us, String link, Map args) {
		Photo photo = null;
		
		String arg = us.getAsString(args, "prior");
		if (!StringUtil.isNullOrEmptyString(arg)) {
			us.setPriorPhoto(PhotoManager.getPhoto(arg));
		}
		
		if (!link.equals(PartUtil.SHOW_PHOTO_PAGE_NAME)) {
			photo = PhotoManager.getPhoto(link);
		}
		
		if (photo == null) {
			PhotoManager photoManager = PhotoManager.getInstance();
			PhotoFilter filter = us.getPhotoFilter();
			photo = photoManager.getVisiblePhoto(filter);
			if (photo != null) {
				link = photo.getId().asString();
			}
		}

		us.setPhoto(photo);
		
		return link;
	}
	
	/**
	 * 
	 */
	protected boolean isToShowAds(UserSession us) {
		return us.getPriorPhoto() != null;
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		Photo photo = us.getPhoto();

		makeLeftSidebar(us, page);

		makePhoto(us, page);
		
		if (photo != null && photo.isVisible()) {
			makePhotoCaption(us, page);
			makeEngageGuest(us, page);

			String photoId = photo.getId().asString();
			page.addString(Photo.ID, photoId);

			Tags tags = photo.getTags();
			page.addString(Photo.DESCRIPTION, getPhotoSummary(us, photo));
			page.addString(Photo.KEYWORDS, tags.asString(false, ','));

			us.addDisplayedPhoto(photo);
		}
		
		makeRightSidebar(us, page);
	}
	
	/**
	 * 
	 */
	protected void makeLeftSidebar(UserSession us, WebPart page) {
		WritableList parts = new WritableList();
		
		Photo photo = us.getPriorPhoto();
		if (photo != null) {
			parts.append(makePriorPhotoInfo(us));
		} else {
			parts.append(createWebPart(us, PartUtil.BLURP_INFO_FILE));
		}

		WebFormHandler handler = getFormHandler(PartUtil.FILTER_PHOTOS_FORM_NAME);
		Writable filterPhotos = handler.makeWebPart(us);
		parts.append(filterPhotos);

		parts.append(createWebPart(us, PartUtil.LINKS_INFO_FILE));
		
		page.addWritable("sidebar", parts);
	}
	
	/**
	 * 
	 */
	protected void makePhoto(UserSession us, WebPart page) {
		PhotoSize pagePhotoSize = us.getPhotoSize();

		Photo photo = us.getPhoto();
		if (photo == null) {
			page.addString("mainWidth", String.valueOf(pagePhotoSize.getMaxPhotoWidth()));
			WebPart done = createWebPart(us, PartUtil.DONE_INFO_FILE);
			page.addWritable(Photo.IMAGE, done);
			return;
		}
		
		Client client = us.getClient();
		if (!photo.isVisible() && !client.hasModeratorRights() && !us.isPhotoOwner(photo)) {
			page.addString("mainWidth", String.valueOf(pagePhotoSize.getMaxPhotoWidth()));
			WebPart done = createWebPart(us, PartUtil.HIDDEN_INFO_FILE);
			page.addWritable(Photo.IMAGE, done);
			return;
		}
		
		PhotoSize maxPhotoSize = photo.getMaxPhotoSize();
		PhotoSize photoSize = (maxPhotoSize.isSmaller(pagePhotoSize)) ? maxPhotoSize : pagePhotoSize;
		String imageLink = getPhotoAsRelativeResourcePathString(photo, photoSize);
		page.addString(Photo.IMAGE, HtmlUtil.asImg(HtmlUtil.asPath(imageLink)));
	}
	
	/**
	 * 
	 */
	protected void makePhotoCaption(UserSession us, WebPart page) {
		Photo photo = us.getPhoto();
		// String photoId = photo.getId().asString();
			
		WebPart caption = createWebPart(us, PartUtil.CAPTION_INFO_FILE);
		caption.addString(Photo.CAPTION, getPhotoCaption(us, photo));
		page.addWritable(Photo.CAPTION, caption);
	}

	/**
	 * 
	 */
	protected void makeEngageGuest(UserSession us, WebPart page) {
		Photo photo = us.getPhoto();
		String photoId = photo.getId().asString();

		WebPart engageGuest = createWebPart(us, PartUtil.ENGAGE_GUEST_FORM_FILE);
		engageGuest.addString(Photo.LINK, HtmlUtil.asHref(getResourceAsRelativeHtmlPathString(photoId)));
		engageGuest.addString(Photo.ID, photoId);

		page.addWritable("engageGuest", engageGuest);
	}
	
	/**
	 * 
	 */
	protected void makeRightSidebar(UserSession us, WebPart page) {
		String handlerName = PartUtil.NULL_FORM_NAME;
		Photo photo = us.getPhoto();
		if (photo != null) {
			handlerName = PartUtil.PRAISE_PHOTO_FORM_NAME;
		}

		WebFormHandler handler = getFormHandler(handlerName);
		Writable praisePhotoForm = handler.makeWebPart(us);
		page.addWritable("praisePhoto", praisePhotoForm);
	}

	
	/**
	 * 
	 */
	protected WebPart makePriorPhotoInfo(UserSession us) {
		WebPart result = createWebPart(us, PartUtil.PHOTO_INFO_FILE);

		Photo photo = us.getPriorPhoto();
		// String id = photo.getId().asString();

		result.addString(Photo.PRAISE, photo.getPraiseAsString(us.cfg()));
		result.addString(Photo.THUMB, getPhotoThumb(us, photo));
		result.addString(Photo.CAPTION, getPhotoCaption(us, photo));
			
		us.setPriorPhoto(null); // reset so you don't get repeats

		return result;
	}

	/**
	 * 
	 */
	public String handlePost(UserSession us, Map args) {
		String result = PartUtil.DEFAULT_PAGE_NAME;
		
		String id = us.getAndSaveAsString(args, Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);
		if (photo != null) {
			if (us.isFormType(args, "flagPhotoLink")) {
				result = PartUtil.FLAG_PHOTO_PAGE_NAME;
			} else if (us.isFormType(args, "tellFriendLink")) {
				result = PartUtil.TELL_FRIEND_PAGE_NAME;
			} else if (us.isFormType(args, "sendEmailLink")) {
				result = PartUtil.SEND_EMAIL_PAGE_NAME;
			}
		}

		return result;
	}
	
}
