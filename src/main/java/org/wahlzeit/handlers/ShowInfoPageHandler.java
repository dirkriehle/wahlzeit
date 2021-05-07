/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.webparts.WebPart;


/**
 * A handler class for a specific web page.
 */
public class ShowInfoPageHandler extends AbstractWebPageHandler {
	
	/**
	 * 
	 */
	protected String infoTmplName = null;
	
	/**
	 *
	 */
	public ShowInfoPageHandler(AccessRights myRights, String myInfoTmplName) {
		initialize(PartUtil.SHOW_INFO_PAGE_FILE, myRights);
		infoTmplName = myInfoTmplName;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		page.addWritable("info", createWebPart(us, infoTmplName));
	}

}
