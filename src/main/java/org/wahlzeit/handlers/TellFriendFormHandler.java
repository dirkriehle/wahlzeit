/*
 *  Copyright
 *
 *  Classname: TellFriendFormHandler
 *  Author: Tango1266
 *  Version: 08.11.17 22:26
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.mailing.EmailService;
import org.wahlzeit.services.mailing.EmailServiceManager;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * A handler class for a specific web form.
 */
public class TellFriendFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(TellFriendFormHandler.class.getName());
    /**
     *
     */
    public static final String EMAIL_FROM = "emailFrom";
    public static final String EMAIL_TO = "emailTo";
    public static final String EMAIL_SUBJECT = "emailSubject";
    public static final String EMAIL_BODY = "emailBody";

    /**
     *
     */
    public TellFriendFormHandler() {
        initialize(PartUtil.TELL_FRIEND_FORM_FILE, AccessRights.GUEST);
    }

    /**
     * @methodtype command
     */
    @Override
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map args = us.getSavedArgs();
        ModelConfig config = us.getClient().getLanguageConfiguration();
        part.addStringFromArgs(args, UserSession.MESSAGE);

        part.maskAndAddStringFromArgs(args, EMAIL_TO);
        part.maskAndAddStringFromArgsWithDefault(args, EMAIL_SUBJECT, config.getTellFriendEmailSubject());

        String emailText = config.getTellFriendEmailWebsite() + "\n\n" + us.getSiteUrl() + "\n\n";

        String id = us.getAsString(args, Photo.ID);
        if (!StringUtil.isNullOrEmptyString(id) && PhotoManager.getInstance().hasPhoto(id)) {
            emailText += (config.getTellFriendEmailPhoto() + "\n\n" + us.getSiteUrl() + id + ".html" + "\n\n");
        }

        part.addString(Photo.ID, id);
        Photo photo = PhotoManager.getInstance().getPhoto(id);
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));

        part.maskAndAddStringFromArgsWithDefault(args, EMAIL_BODY, emailText);
    }

    /**
     *
     */
    @Override
    protected String doHandlePost(UserSession us, Map args) {
        String friendsEmailAddress = us.getAndSaveAsString(args, EMAIL_TO);
        String emailSubject = us.getAndSaveAsString(args, EMAIL_SUBJECT);
        String emailBody = us.getAndSaveAsString(args, EMAIL_BODY);
        ModelConfig config = us.getClient().getLanguageConfiguration();

        if (StringUtil.isNullOrEmptyString(friendsEmailAddress)) {
            us.setMessage(config.getEmailAddressIsMissing());
            return PartUtil.TELL_FRIEND_PAGE_NAME;
        } else if (!StringUtil.isValidStrictEmailAddress(friendsEmailAddress)) {
            us.setMessage(config.getEmailAddressIsInvalid());
            return PartUtil.TELL_FRIEND_PAGE_NAME;
        }
        if ((emailSubject.length() > 128) || (emailBody.length() > 1024)) {
            us.setMessage(config.getInputIsTooLong());
            return PartUtil.TELL_FRIEND_PAGE_NAME;
        }

        EmailAddress to = EmailAddress.getFromString(friendsEmailAddress);

        EmailService emailService = EmailServiceManager.getDefaultService();
        emailService.sendEmailIgnoreException(to, config.getAuditEmailAddress(), emailSubject, emailBody);

        log.info(LogBuilder.createUserMessage().
                addAction("TellFriend").
                addParameter("recipient", to.asString()).toString());

        us.setTwoLineMessage(config.getEmailWasSent() + friendsEmailAddress + "! ", config.getKeepGoing());

        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}
