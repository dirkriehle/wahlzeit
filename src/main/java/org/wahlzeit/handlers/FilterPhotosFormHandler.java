/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * A handler class for a specific web form.
 */
public class FilterPhotosFormHandler extends AbstractWebFormHandler {
	
	/**
	 * 
	 */
	public FilterPhotosFormHandler() {
		initialize(PartUtil.FILTER_PHOTOS_FORM_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected void doMakeWebPart(UserSession us, WebPart part) {
		PhotoFilter filter = us.getPhotoFilter();
			
		part.maskAndAddString(PhotoFilter.USER_NAME, filter.getUserName());
		part.maskAndAddString(PhotoFilter.TAGS, filter.getTags().asString());
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		PhotoFilter filter = us.getPhotoFilter();

		String un = us.getAsString(args, PhotoFilter.USER_NAME);
		if (StringUtil.isLegalUserName(un)) {
			filter.setUserName(un);
		}
		
		String tags = us.getAsString(args, PhotoFilter.TAGS);
		if (StringUtil.isLegalTagsString(tags)) {
			filter.setTags(new Tags(tags));
		}
		
		UserLog.logPerformedAction("FilterPhotos");
		
		return PartUtil.SHOW_PHOTO_PAGE_NAME;
	}
	
}
