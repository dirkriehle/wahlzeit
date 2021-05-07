/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services.mailing;

import javax.mail.Message;

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
