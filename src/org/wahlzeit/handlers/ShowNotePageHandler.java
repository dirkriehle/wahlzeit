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
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author dirkriehle
 *
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
	protected boolean isWellFormedGet(UserSession ctx, String link, Map args) {
		return hasSavedMessage(ctx);
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession ctx, WebPart page) {
		String heading = ctx.getHeading();
		heading = StringUtil.isNullOrEmptyString(heading) ? ctx.cfg().getThankYou() : heading;
		page.addString("noteHeading", heading);
		
		page.addString("note", ctx.getMessage());
	}

}
