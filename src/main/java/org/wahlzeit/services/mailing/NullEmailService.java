/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services.mailing;

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.SysLog;

/**
 * 
 * @author dirk
 *
 */
public class NullEmailService implements EmailService {

	@Override
	public void sendEmail(EmailAddress from, EmailAddress to, String subject, String body) throws MailingException {
		SysLog.logSysInfo("Called sendEmail (no bcc) on null implementation");
	}
	
	@Override
	public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, String subject, String body) {
		SysLog.logSysInfo("Called sendEmailIgnoreException (no bcc) on null implementation");
		return true;
	}
	
	@Override
	public void sendEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
		SysLog.logSysInfo("Called sendEmail (with bcc) on null implementation");
	}

	@Override
	public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
		SysLog.logSysInfo("Called sendEmailIgnoreException (with bcc) on null implementation");
		return true;
	}

}
