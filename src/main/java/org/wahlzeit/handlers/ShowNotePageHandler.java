/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * A handler class for a specific web page.
 */
public class ShowNotePageHandler extends AbstractWebPageHandler {
		
	/**
	 *
	 */
	public ShowNotePageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedGet(UserSession us, String link, Map args) {
		return hasSavedMessage(us);
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		String heading = us.getHeading();
		heading = StringUtil.isNullOrEmptyString(heading) ? us.cfg().getThankYou() : heading;
		page.addString("noteHeading", heading);
		
		page.addString("note", us.getMessage());
	}

}
