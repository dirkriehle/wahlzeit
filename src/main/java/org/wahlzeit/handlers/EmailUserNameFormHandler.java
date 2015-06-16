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

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.mailing.EmailService;
import org.wahlzeit.services.mailing.EmailServiceManager;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class EmailUserNameFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(EmailUserNameFormHandler.class.getName());


    /**
     *
     */
    public EmailUserNameFormHandler() {
        initialize(PartUtil.EMAIL_USER_NAME_FORM_FILE, AccessRights.GUEST);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map<String, Object> savedArgs = us.getSavedArgs();
        part.addStringFromArgs(savedArgs, UserSession.MESSAGE);
        part.maskAndAddStringFromArgs(savedArgs, User.EMAIL_ADDRESS);
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        String emailAddress = us.getAndSaveAsString(args, User.EMAIL_ADDRESS);
        if (StringUtil.isNullOrEmptyString(emailAddress)) {
            us.setMessage(us.getConfiguration().getFieldIsMissing());
            return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
        } else if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
            us.setMessage(us.getConfiguration().getEmailAddressIsInvalid());
            return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
        }

        UserManager userManager = UserManager.getInstance();
        User user = userManager.getUserByEmailAddress(emailAddress);
        if (user == null) {
            us.setMessage(us.getConfiguration().getUnknownEmailAddress());
            return PartUtil.EMAIL_PASSWORD_PAGE_NAME;
        }

        EmailService emailService = EmailServiceManager.getDefaultService();

        EmailAddress to = user.getEmailAddress();
        emailService.sendEmailIgnoreException(to, us.getConfiguration().getAuditEmailAddress(), us.getConfiguration().getSendUserNameEmailSubject(), user.getId());

        log.info(LogBuilder.createUserMessage().
                addAction("Username send per E-Mail").
                addParameter("Target address", to.asString()).toString());

        us.setTwoLineMessage(us.getConfiguration().getUserNameWasEmailed(), us.getConfiguration().getContinueWithShowPhoto());

        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}
