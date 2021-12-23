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

package org.wahlzeit.service.mailing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wahlzeit.model.EmailAddress;

/**
 * @author dirk
 */
public class NullEmailService implements EmailService {

    protected static final Logger LOG = LogManager.getLogger(NullEmailService.class);

    @Override
    public void sendEmail(EmailAddress from, EmailAddress to, String subject, String body) {
        LOG.info("Called sendEmail (no bcc) on null implementation");
    }

    @Override
    public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, String subject, String body) {
        LOG.info("Called sendEmailIgnoreException (no bcc) on null implementation");
        return true;
    }

    @Override
    public void sendEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
        LOG.info("Called sendEmail (with bcc) on null implementation");
    }

    @Override
    public boolean sendEmailIgnoreException(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
        LOG.info("Called sendEmailIgnoreException (with bcc) on null implementation");
        return true;
    }

}
