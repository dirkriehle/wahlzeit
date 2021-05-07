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
 * This pages handles incoming get requests for a page. 
 * Typically, this means an external link points to the site.
 * Example: www.wahlzeit.com/filter?userName=laura
 */
public class FilterPhotosPageHandler extends AbstractWebPageHandler {
	
	/**
	 *
	 */
	public FilterPhotosPageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedGet(UserSession us, String link, Map args) {
		return args != null;
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		PhotoFilter filter = us.getPhotoFilter();

		String un = us.getAsString(args, PhotoFilter.USER_NAME);
		if (StringUtil.isLegalUserName(un)) {
			filter.setUserName(un);
		}

		String tags = us.getAsString(args, PhotoFilter.TAGS);
		if (StringUtil.isLegalTagsString(tags)) {
			filter.setTags(new Tags(tags));
		}

		return PartUtil.SHOW_PHOTO_PAGE_NAME;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		page.addString("noteHeading", us.cfg().getInformation());
		String msg1 = us.cfg().getContinueWithShowPhoto();
		page.addString("note", msg1);
	}

}
