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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import junit.framework.*;
import org.wahlzeit.services.*;


/**
 * The MockEmailServer compares calls with test data.
 * 
 * @author dirkriehle
 *
 */
public class MockEmailServer extends AbstractEmailServer {

	/**
	 * 
	 */
	protected EmailAddress fromEA;
	protected EmailAddress toEA;
	protected EmailAddress bccEA;
	protected String emailSubject;
	protected String emailBody;

	/**
	 * 
	 */
	public MockEmailServer(EmailAddress from, EmailAddress to,
			EmailAddress bcc, String subject, String body) {
		super();

		fromEA = from;
		toEA = to;
		bccEA = bcc;
		emailSubject = subject;
		emailBody = body;
	}

	/**
	 * 
	 */
	public synchronized void sendEmail(EmailAddress from, EmailAddress to,
			EmailAddress bcc, String subject, String body) {
		if (!fromEA.equals(from) || !toEA.equals(to) || !bccEA.equals(bcc)
				|| !emailSubject.equals(subject) || !emailBody.equals(body)) {
			Assert.fail("unexpected parameters passed to MockEmailServer.sendEmail");
		}
	}

	/**
	 * 
	 * @methodtype factory
	 * @methodproperties composed
	 */
	protected Message createMessage(EmailAddress from, EmailAddress to,
			EmailAddress bcc, String subject, String body)
			throws MessagingException, AddressException {
		return null;
	}

	/**
	 * 
	 * @methodproperties primitive, hook
	 */
	protected void doSendEmail(Message msg) throws Exception {
		SysLog.logInfo("pretending to send email...");
	}

}
