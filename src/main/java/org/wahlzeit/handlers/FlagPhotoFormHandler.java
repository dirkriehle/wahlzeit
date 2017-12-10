/*
 *  Copyright
 *
 *  Classname: FlagPhotoFormHandler
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

import org.wahlzeit.agents.AsyncTaskExecutor;
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
public class FlagPhotoFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(FlagPhotoFormHandler.class.getName());

    /**
     *
     */
    public FlagPhotoFormHandler() {
        initialize(PartUtil.FLAG_PHOTO_FORM_FILE, AccessRights.GUEST);
    }

    /**
     *
     */
    @Override
    protected boolean isWellFormedGet(UserSession us, String link, Map args) {
        return hasSavedPhotoId(us) && isSavedPhotoVisible(us);
    }

    /**
     *
     */
    @Override
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map args = us.getSavedArgs();
        part.addStringFromArgs(args, UserSession.MESSAGE);

        String id = us.getAsString(args, Photo.ID);
        Photo photo = PhotoManager.getInstance().getPhoto(id);
        part.addString(Photo.ID, id);
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));
        part.maskAndAddStringFromArgsWithDefault(args, PhotoCase.FLAGGER, us.getClient().getEmailAddress().asString());
        part.addSelect(PhotoCase.REASON, FlagReason.MISMATCH);
        part.maskAndAddStringFromArgs(args, PhotoCase.EXPLANATION);
    }

    /**
     *
     */
    @Override
    protected String doHandlePost(UserSession us, Map args) {
        String id = us.getAndSaveAsString(args, Photo.ID);
        String flagger = us.getAndSaveAsString(args, PhotoCase.FLAGGER);
        FlagReason reason = FlagReason.getFromString(us.getAndSaveAsString(args, PhotoCase.REASON));
        String explanation = us.getAndSaveAsString(args, PhotoCase.EXPLANATION);
        ModelConfig config = us.getClient().getLanguageConfiguration();

        if (StringUtil.isNullOrEmptyString(flagger)) {
            us.setMessage(config.getEmailAddressIsMissing());
            return PartUtil.FLAG_PHOTO_PAGE_NAME;
        } else if (!StringUtil.isValidStrictEmailAddress(flagger)) {
            us.setMessage(config.getEmailAddressIsInvalid());
            return PartUtil.FLAG_PHOTO_PAGE_NAME;
        } else if (explanation.length() > 1024) {
            us.setMessage(config.getInputIsTooLong());
            return PartUtil.FLAG_PHOTO_PAGE_NAME;
        }

        Photo photo = PhotoManager.getInstance().getPhoto(id);
        photo.setStatus(photo.getStatus().asFlagged(true));
        AsyncTaskExecutor.savePhotoAsync(id);

        PhotoCase photoCase = new PhotoCase(photo);
        photoCase.setFlagger(flagger);
        photoCase.setReason(reason);
        photoCase.setExplanation(explanation);
        PhotoCaseManager pcm = PhotoCaseManager.getInstance();
        pcm.addPhotoCase(photoCase);

        EmailService emailService = EmailServiceManager.getDefaultService();

        EmailAddress to = config.getModeratorEmailAddress();

        String emailSubject = "Photo: " + id + " of user: " + photo.getOwnerId() + " got flagged";
        String emailBody = "Photo: " + us.getSiteUrl() + id + ".html\n\n";
        emailBody += "Reason: " + reason + "\n\n";
        emailBody += "Explanation: " + explanation + "\n\n";

        emailService.sendEmailIgnoreException(to, config.getAuditEmailAddress(), emailSubject, emailBody);

        log.info(LogBuilder.createUserMessage()
                .addAction("Flag Photo")
                .addParameter("Photo", photo.getId().asString()).toString());

        us.setTwoLineMessage(config.getModeratorWasInformed(), config.getContinueWithShowPhoto());

        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}
