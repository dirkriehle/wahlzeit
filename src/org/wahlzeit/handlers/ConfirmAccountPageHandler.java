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
 * 
 * @author driehle
 *
 */
public class ConfirmAccountPageHandler extends AbstractWebPageHandler {
	
	/**
	 *
	 */
	public ConfirmAccountPageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedGet(UserSession ctx, String link, Map args) {
		return (args != null) && (args.get("code") != null);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession ctx, String link, Map args) {
		Client client = ctx.getClient();
		long confirmationCode = -1;
		
		try {
			String arg = ctx.getAsString(args, "code");
			confirmationCode = Long.valueOf(arg).longValue();
			ctx.setConfirmationCode(confirmationCode);
		} catch (Exception ex) {
			// NumberFormatException
		}

		if (client instanceof User) {
			User user = (User) client;
			if (user.getConfirmationCode() == confirmationCode) {
				user.setConfirmed();
			} else {
				UserManager.getInstance().emailConfirmationRequest(ctx, user);
			}
			ctx.clearConfirmationCode();
		}

		return link;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession ctx, WebPart page) {
		String heading, msg1, msg2 = "";
		
		Client client = ctx.getClient();
		if (client instanceof User) {
			User user = (User) client;
			if (user.isConfirmed()) {
				heading = ctx.cfg().getThankYou();
				msg1 = ctx.cfg().getConfirmAccountSucceeded();
				msg2 = ctx.cfg().getContinueWithShowUserHome();
			} else {
				heading = ctx.cfg().getInformation();
				msg1 = ctx.cfg().getConfirmAccountFailed();
				msg2 = ctx.cfg().getConfirmationEmailWasSent();
			}
			page.addString("note", HtmlUtil.asPara(msg1) + HtmlUtil.asPara(msg2));
		} else {
			heading = ctx.cfg().getInformation();
			page.addString("note", ctx.cfg().getNeedToLoginFirst());
		}
			
		page.addString("noteHeading", heading);
	}

}
