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

package org.wahlzeit.services.mailing;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.StringUtil;

/**
 * Abstract superclass for non-trivial EmailServer implementations.
 * 
 */
public abstract class AbstractEmailService implements EmailService {

	/**
	 * 
	 */
	@Override
	public void sendEmail(EmailAddress from, EmailAddress to, String subject, String body) throws MailingException {
		sendEmail(from, to, EmailAddress.EMPTY, subject, body);
	}

	/**
	 * 
	 */
	@Override
	public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, String subject, String body) {
		return sendEmailIgnoreException(from, to, EmailAddress.EMPTY, subject, body);
	}

	/**
	 * 
	 */
	@Override
	public void sendEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
		assertIsValidEmailAddress(from, "from");
		assertIsValidEmailAddress(to, "to");		
		assertIsValidString(subject, "subject");
		assertIsValidString(body, "body");

		Message msg = doCreateEmail(from, to, bcc, subject, body);
		doSendEmail(msg);
	}

	/**
	 * 
	 */
	@Override
	public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
		try {
			sendEmail(from, to, bcc, subject, body);
			return true;
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
			return false;
		}
	}

	/**
	 * 
	 */
	protected void assertIsValidEmailAddress(EmailAddress address, String label) throws MailingException {
		if ((address == null) || !address.isValid()) {
			throw new MailingException(label + " must be a valid email address");
		}
	}

	/**
	 * 
	 */
	protected void assertIsValidString(String toBeChecked, String label) throws MailingException {
		if (StringUtil.isNullOrEmptyString(toBeChecked)) {
			throw new MailingException(label + " must neither be null nor empty");
		}
	}

	/**
	 * 
	 */
	protected abstract Message doCreateEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException;

	/**
	 * 
	 */	
	protected abstract void doSendEmail(Message msg) throws MailingException;
	
}
