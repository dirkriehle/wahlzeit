/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.Map;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.webparts.Writable;
import org.wahlzeit.webparts.WebPart;



/**
 * A handler class for a generic web page.
 */
public class ShowPartPageHandler extends AbstractWebPageHandler {
	
	/**
	 * 
	 */
	protected WebPartHandler partHandler = null;

	/**
	 * 
	 */
	public ShowPartPageHandler(AccessRights myRights, WebPartHandler myPartHandler) {
		initialize(myRights, myPartHandler);
	}

	/**
	 * 
	 */
	protected void initialize(AccessRights myRights, WebPartHandler myPartHandler) {
		super.initialize(PartUtil.SHOW_PART_PAGE_FILE, myRights);
		partHandler = myPartHandler;
	}

	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		return partHandler.handleGet(us, link, null);
	}

	/**
	 * 
	 */
	public void makeWebPageBody(UserSession us, WebPart page) {
		Writable part = partHandler.makeWebPart(us);
		page.addWritable("part", part);
	}
	
}