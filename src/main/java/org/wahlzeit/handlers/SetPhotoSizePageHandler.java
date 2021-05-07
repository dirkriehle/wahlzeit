/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.PhotoSize;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * A handler class for a specific web page.
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
