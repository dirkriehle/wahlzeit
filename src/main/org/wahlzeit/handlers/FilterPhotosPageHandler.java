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
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * This pages handles incoming get requests for a page.
 * Typically, this means an external link points to the site.
 * Example: www.wahlzeit.com/filter?userName=laura
 * 
 * @author dirkriehle
 *
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
