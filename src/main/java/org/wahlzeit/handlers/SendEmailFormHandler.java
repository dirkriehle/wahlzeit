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
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.mailing.EmailService;
import org.wahlzeit.services.mailing.EmailServiceManager;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class SendEmailFormHandler extends AbstractWebFormHandler {

    /**
     *
     */
    public static final String USER = "user";
    public static final String USER_LANGUAGE = "userLanguage";
    public static final String EMAIL_SUBJECT = "emailSubject";
    public static final String EMAIL_BODY = "emailBody";

    private static final Logger log = Logger.getLogger(SendEmailFormHandler.class.getName());

    /**
     *
     */
    public SendEmailFormHandler() {
        initialize(PartUtil.SEND_EMAIL_FORM_FILE, AccessRights.GUEST);
    }

    /**
     *
     */
    public boolean isWellFormedGet(UserSession us, String link, Map args) {
        return hasSavedPhotoId(us);
    }

    /**
     *
     */
    protected String doHandleGet(UserSession us, String link, Map args) {
        if (!(us.getClient() instanceof User)) {
            us.setHeading(us.getConfiguration().getInformation());
            us.setMessage(us.getConfiguration().getNeedToSignupFirst());
            return PartUtil.SHOW_NOTE_PAGE_NAME;
        }

        return super.doHandleGet(us, link, args);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map args = us.getSavedArgs();
        part.addStringFromArgs(args, UserSession.MESSAGE);

        String id = us.getAndSaveAsString(args, Photo.ID);
        part.addString(Photo.ID, id);
        Photo photo = PhotoManager.getPhoto(id);
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));

        part.maskAndAddString(USER, photo.getOwnerId());

        User user = (User) us.getClient();
        part.addString(USER_LANGUAGE, us.getConfiguration().asValueString(user.getLanguage()));

        part.maskAndAddStringFromArgs(args, EMAIL_SUBJECT);
        part.maskAndAddStringFromArgs(args, EMAIL_BODY);
    }

    /**
     *
     */
    protected boolean isWellFormedPost(UserSession us, Map args) {
        return PhotoManager.getPhoto(us.getAsString(args, Photo.ID)) != null;
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        String id = us.getAndSaveAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(id);

        String emailSubject = us.getAndSaveAsString(args, EMAIL_SUBJECT);
        String emailBody = us.getAndSaveAsString(args, EMAIL_BODY);
        if ((emailSubject.length() > 128) || (emailBody.length() > 1024)) {
            us.setMessage(us.getConfiguration().getInputIsTooLong());
            return PartUtil.SEND_EMAIL_PAGE_NAME;
        }

        UserManager userManager = UserManager.getInstance();
        User toUser = userManager.getUserById(photo.getOwnerId());

        emailSubject = us.getConfiguration().getSendEmailSubjectPrefix() + emailSubject;
        emailBody = us.getConfiguration().getSendEmailBodyPrefix() + emailBody + us.getConfiguration().getSendEmailBodyPostfix();

        EmailService emailService = EmailServiceManager.getDefaultService();
        emailService.sendEmailIgnoreException(toUser.getEmailAddress(), us.getConfiguration().getAuditEmailAddress(), emailSubject, emailBody);

        log.info(LogBuilder.createUserMessage().
                addAction("Send E-Mail").
                addParameter("Recipient", toUser.getNickName()).toString());

        us.setMessage(us.getConfiguration().getEmailWasSent() + toUser.getNickName() + "!");

        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}
