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
