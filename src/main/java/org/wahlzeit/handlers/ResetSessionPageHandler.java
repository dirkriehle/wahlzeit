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
 * A handler class for a specific web page.
 */
public class ResetSessionPageHandler extends AbstractWebPageHandler {
	
	/**
	 *
	 */
	public ResetSessionPageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		us.clear();
		return link;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		page.addString("noteHeading", us.cfg().getThankYou());
		String msg1 = us.cfg().getResetSession();
		String msg2 = us.cfg().getContinueWithShowPhoto();
		page.addString("note", HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2));
	}

}
