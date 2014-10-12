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
