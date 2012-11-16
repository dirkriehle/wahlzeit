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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.EmailServer;
import org.wahlzeit.webparts.WebPart;

import junit.framework.Test;


/**
 * Acceptance tests for the TellFriend feature.
 * 
 * @author dirkriehle
 *
 */
public class TellFriendTest extends HandlerTestCase {

	/**
	 * 
	 */
	public static void main(String[] args) {
		Test test = new HandlerTestSetup(new HandlerTestSuite(TellFriendTest.class));
		junit.textui.TestRunner.run(test);
	}

	/**
	 * 
	 */
	public TellFriendTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void setUp() {
		handler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}

	/**
	 * 
	 */
	public void testTellFriendMakeWebPart() {
		WebPart part = handler.makeWebPart(session);
		// no failure is good behavior

		EmailServer.setNullInstance(); // no emails please
		EmailAddress to = EmailAddress.getFromString("engel@himmel.de");
		Map<String, String> args = new HashMap<String, String>();
		args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
		args.put(TellFriendFormHandler.EMAIL_SUBJECT, "Oh well...");
		handler.handlePost(session, args);

		part = handler.makeWebPart(session);
		assertEquals(part.getValue(TellFriendFormHandler.EMAIL_TO), to.asString());
		assertEquals(part.getValue(TellFriendFormHandler.EMAIL_SUBJECT), "Oh well...");
	}

	/**
	 * 
	 */
	public void testTellFriendPost() {
		EmailAddress from = EmailAddress.getFromString("info@wahlzeit.org");
		EmailAddress to = EmailAddress.getFromString("fan@yahoo.com");
		EmailAddress bcc = session.cfg().getAuditEmailAddress();
		String subject = "Coolest website ever!";
		String body = "You've got to check this out!";
		EmailServer.setInstance(new MockEmailServer(from, to, bcc, subject, body));

		Map<String, String> args = new HashMap<String, String>();
		args.put(TellFriendFormHandler.EMAIL_FROM, from.asString());
		args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
		args.put(TellFriendFormHandler.EMAIL_SUBJECT, subject);
		args.put(TellFriendFormHandler.EMAIL_BODY, body);

		handler.handlePost(session, args);

		EmailServer.setInstance(new MockEmailServer(from, to, bcc, subject, body));
		handler.handlePost(session, Collections.EMPTY_MAP); // will fail if email is sent		
	}	

}