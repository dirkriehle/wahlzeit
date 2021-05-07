/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Guest;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * A handler class for a specific web page.
 */
public class LogoutPageHandler extends AbstractWebPageHandler {
	
	/**
	 *
	 */
	public LogoutPageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.USER);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		EmailAddress ea = us.getClient().getEmailAddress();
		us.setClient(new Guest());
		us.getClient().setEmailAddress(ea);
		us.clearSavedArgs();
		return link;
	}
	
	/**
	 * @methodtype command
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		page.addString("noteHeading", us.cfg().getThankYou());
		String msg1 = us.cfg().getLogoutSucceeded();
		String msg2 = us.cfg().getContinueWithTellFriends();
		page.addString("note", HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2));
	}

}
