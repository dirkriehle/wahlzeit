/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services.mailing;

import javax.mail.Message;

import org.wahlzeit.services.EmailAddress;

/**
 * Abstract superclass for non-trivial EmailServer implementations.
 * 
 */
public class MockEmailService extends AbstractEmailService {

	/**
	 * 
	 */
	protected Message doCreateEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
		return null;
	}

	/**
	 * 
	 */	
	protected void doSendEmail(Message msg) throws MailingException {
		// do nothing
	}
	
}
