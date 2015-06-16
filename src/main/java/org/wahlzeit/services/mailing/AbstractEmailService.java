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
import org.wahlzeit.utils.StringUtil;

import javax.mail.Message;
import java.util.logging.Logger;

/**
 * Abstract superclass for non-trivial EmailServer implementations.
 */
public abstract class AbstractEmailService implements EmailService {

    private static final Logger log = Logger.getLogger(AbstractEmailService.class.getName());

    /**
     *
     */
    @Override
    public void sendEmail(EmailAddress to, String subject, String body) throws MailingException {
        sendEmail(to, EmailAddress.EMPTY, subject, body);
    }

    /**
     *
     */
    @Override
    public boolean sendEmailIgnoreException(EmailAddress to, String subject, String body) {
        return sendEmailIgnoreException(to, EmailAddress.EMPTY, subject, body);
    }

    /**
     *
     */
    @Override
    public void sendEmail(EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
        assertIsValidEmailAddress(to, "to");
        assertIsValidString(subject, "subject");
        assertIsValidString(body, "body");

        Message msg = doCreateEmail(to, bcc, subject, body);
        doSendEmail(msg);
    }

    /**
     *
     */
    @Override
    public boolean sendEmailIgnoreException(EmailAddress to, EmailAddress bcc, String subject, String body) {
        try {
            sendEmail(to, bcc, subject, body);
            return true;
        } catch (Exception ex) {
            log.warning(LogBuilder.createSystemMessage().
                    addException("Problem sending email", ex).toString());
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
    protected abstract Message doCreateEmail(EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException;

    /**
     *
     */
    protected abstract void doSendEmail(Message msg) throws MailingException;

}
