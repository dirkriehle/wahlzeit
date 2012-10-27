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

import org.wahlzeit.main.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.webparts.*;

import junit.framework.*;

/**
 * Acceptance tests for the TellFriend feature.
 * 
 * @author dirkriehle
 *
 */
public class TellFriendTest extends TestCase {
	
	/**
	 * 
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TellFriendTest.class);
	}

	/**
	 * 
	 */
	public TellFriendTest(String name) {
		super(name);
	}
	
	static HandlerTestSetup hts;
	
	public static Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TellFriendTest.class);
		hts = new HandlerTestSetup(suite);
		return hts;
	}
	
	/**
	 * 
	 */
	public void testTellFriendMakeWebPart() {
		WebPart part = hts.handler.makeWebPart(hts.session);
		// no failure is good behavior
		
		EmailServer.setNullInstance(); // no emails please
		EmailAddress to = EmailAddress.getFromString("engel@himmel.de");
		Map<String, String> args = new HashMap<String, String>();
		args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
		args.put(TellFriendFormHandler.EMAIL_SUBJECT, "Oh well...");
		hts.handler.handlePost(hts.session, args);
		
		part = hts.handler.makeWebPart(hts.session);
		assertEquals(part.getValue(TellFriendFormHandler.EMAIL_TO), to.asString());
		assertEquals(part.getValue(TellFriendFormHandler.EMAIL_SUBJECT), "Oh well...");
	}

	/**
	 * 
	 */
	public void testTellFriendPost() {
		EmailAddress from = EmailAddress.getFromString("info@wahlzeit.org");
		EmailAddress to = EmailAddress.getFromString("fan@yahoo.com");
		EmailAddress bcc = hts.session.cfg().getAuditEmailAddress();
		String subject = "Coolest website ever!";
		String body = "You've got to check this out!";
		EmailServer.setInstance(new MockEmailServer(from, to, bcc, subject, body));

		Map<String, String> args = new HashMap<String, String>();
		args.put(TellFriendFormHandler.EMAIL_FROM, from.asString());
		args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
		args.put(TellFriendFormHandler.EMAIL_SUBJECT, subject);
		args.put(TellFriendFormHandler.EMAIL_BODY, body);

		hts.handler.handlePost(hts.session, args);
		
		EmailServer.setInstance(new MockEmailServer(from, to, bcc, subject, body));
		hts.handler.handlePost(hts.session, Collections.EMPTY_MAP); // will fail if email is sent		
	}	

}
