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
 * 
 * @author dirkriehle
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
	protected boolean isWellFormedGet(UserSession us, String link, Map args) {
		return (args != null) && (args.get("code") != null);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		Client client = us.getClient();
		long confirmationCode = -1;
		
		try {
			String arg = us.getAsString(args, "code");
			confirmationCode = Long.valueOf(arg).longValue();
			us.setConfirmationCode(confirmationCode);
		} catch (Exception ex) {
			// NumberFormatException
		}

		if (client instanceof User) {
			User user = (User) client;
			if (user.getConfirmationCode() == confirmationCode) {
				user.setConfirmed();
			} else {
				UserManager.getInstance().emailConfirmationRequest(us, user);
			}
			us.clearConfirmationCode();
		}

		return link;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		String heading, msg1, msg2 = "";
		
		Client client = us.getClient();
		if (client instanceof User) {
			User user = (User) client;
			if (user.isConfirmed()) {
				heading = us.cfg().getThankYou();
				msg1 = us.cfg().getConfirmAccountSucceeded();
				msg2 = us.cfg().getContinueWithShowUserHome();
			} else {
				heading = us.cfg().getInformation();
				msg1 = us.cfg().getConfirmAccountFailed();
				msg2 = us.cfg().getConfirmationEmailWasSent();
			}
			page.addString("note", HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2));
		} else {
			heading = us.cfg().getInformation();
			page.addString("note", us.cfg().getNeedToLoginFirst());
		}
			
		page.addString("noteHeading", heading);
	}

}
