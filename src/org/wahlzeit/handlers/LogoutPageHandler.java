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
import org.wahlzeit.model.Client;
import org.wahlzeit.model.Guest;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * 
 * @author dirkriehle
 *
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
	protected String doHandleGet(UserSession ctx, String link, Map args) {
		EmailAddress ea = ctx.getClient().getEmailAddress();
		ctx.setClient(Client.createClient(Guest.class));
		ctx.getClient().setEmailAddress(ea);
		ctx.clearSavedArgs();
		return link;
	}
	
	/**
	 * @methodtype command
	 */
	protected void makeWebPageBody(UserSession ctx, WebPart page) {
		page.addString("noteHeading", ctx.cfg().getThankYou());
		String msg1 = ctx.cfg().getLogoutSucceeded();
		String msg2 = ctx.cfg().getContinueWithTellFriends();
		page.addString("note", HtmlUtil.asPara(msg1) + HtmlUtil.asPara(msg2));
	}

}
