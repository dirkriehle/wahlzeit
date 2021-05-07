/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services.mailing;

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.SysLog;

/**
 * A logging mailing service logs email send attempts before sending emails.
 * This is a decorator pattern application.
 * 
 */
public class LoggingEmailService implements EmailService {
	
	/**
	 * 
	 */
	protected EmailService decorated = null;
	
	/**
	 * 
	 */
	public LoggingEmailService(EmailService myDecorated) {
		decorated = myDecorated;
	}
	
	/**
	 * 
	 */
	@Override
	public void sendEmail(EmailAddress from, EmailAddress to, String subject, String body) throws MailingException {
		String fromString = (from == null) ? "null" : from.asString();
		String toString = (to == null) ? "null" : to.asString();
		String subjectString = (subject == null) ? "null" : subject;
		
		SysLog.logSysInfo("Called sendEmail from: " + fromString + " to: " + toString + " with subject: " + subjectString);
		
		decorated.sendEmail(from, to, subject, body);
	}

	/**
	 * 
	 */
	@Override
	public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, String subject, String body) {
		try {
			sendEmail(from, to, subject, body);
			return true;
		} catch (MailingException ex) {
			// sendEmail failed
			return false;
		}
	}

	/**
	 * 
	 */
	@Override
	public void sendEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
		String fromString = (from == null) ? "null" : from.asString();
		String toString = (to == null) ? "null" : to.asString();
		String bccString = (bcc == null) ? "null" : bcc.asString();
		String subjectString = (subject == null) ? "null" : subject;
		
		SysLog.logSysInfo("Called sendEmail from: " + fromString + " to: " + toString + " bcc: " + bccString + " with subject: " + subjectString);
		
		decorated.sendEmail(from, to, bcc, subject, body);
	}

	/**
	 * 
	 */
	@Override
	public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
		try {
			sendEmail(from, to, bcc, subject, body);
			return true;
		} catch (MailingException ex) {
			// sendEmail failed
			return false;
		}
	}

}
