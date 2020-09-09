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