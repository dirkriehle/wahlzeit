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
import org.wahlzeit.model.PhotoSize;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author dirkriehle
 *
 */
public class SetPhotoSizePageHandler extends AbstractWebPageHandler {
	
	/**
	 * 
	 */
	public SetPhotoSizePageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		PhotoSize result = PhotoSize.MEDIUM;
		
		if (link.equals(PartUtil.SET_EXTRA_SMALL_PHOTO_SIZE_PAGE_NAME)) {
			result = PhotoSize.EXTRA_SMALL;
		} else if (link.equals(PartUtil.SET_SMALL_PHOTO_SIZE_PAGE_NAME)) {
			result = PhotoSize.SMALL;
		} else if (link.equals(PartUtil.SET_LARGE_PHOTO_SIZE_PAGE_NAME)) {
			result = PhotoSize.LARGE;
		} else if (link.equals(PartUtil.SET_EXTRA_LARGE_PHOTO_SIZE_PAGE_NAME)) {
			result = PhotoSize.EXTRA_LARGE;
		}
		
		us.setPhotoSize(result);
		
		return link;
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		page.addString("noteHeading", us.cfg().getInformation());
		String msg1 = us.cfg().getNewPhotoSizeSet(us.getPhotoSize());
		String msg2 = us.cfg().getNoteMaximumPhotoSize();
		String msg3 = us.cfg().getContinueWithShowPhoto();
		page.addString("note", HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2) + HtmlUtil.asP(msg3));
	}

}
