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
import org.wahlzeit.services.LogBuilder;

import java.util.logging.Logger;

/**
 * A logging mailing service logs email send attempts before sending emails.
 * This is a decorator pattern application.
 */
public class LoggingEmailService implements EmailService {

    private static final Logger log = Logger.getLogger(LoggingEmailService.class.getName());

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
    public void sendEmail(EmailAddress to, String subject, String body) throws MailingException {
        String toString = (to == null) ? "null" : to.asString();
        String subjectString = (subject == null) ? "null" : subject;

        log.config(LogBuilder.createSystemMessage().
                addAction("Send E-Mail").
                addParameter("to", toString).
                addParameter("subject", subjectString).toString());

        decorated.sendEmail(to, subject, body);
    }

    /**
     *
     */
    @Override
    public boolean sendEmailIgnoreException(EmailAddress to, String subject, String body) {
        try {
            sendEmail(to, subject, body);
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
    public void sendEmail(EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
        String toString = (to == null) ? "null" : to.asString();
        String bccString = (bcc == null) ? "null" : bcc.asString();
        String subjectString = (subject == null) ? "null" : subject;

        log.config(LogBuilder.createSystemMessage().
                addAction("Send E-Mail").
                addParameter("to", toString).
                addParameter("bcc", bccString).
                addParameter("subject", subjectString).toString());

        decorated.sendEmail(to, bcc, subject, body);
    }

    /**
     *
     */
    @Override
    public boolean sendEmailIgnoreException(EmailAddress to, EmailAddress bcc, String subject, String body) {
        try {
            sendEmail(to, bcc, subject, body);
            return true;
        } catch (MailingException ex) {
            // sendEmail failed
            return false;
        }
    }

}
