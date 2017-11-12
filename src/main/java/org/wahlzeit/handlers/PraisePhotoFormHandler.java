/*
 *  Copyright
 *
 *  Classname: PraisePhotoFormHandler
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
import org.wahlzeit.model.gurkenDomain.GurkenPhotoManager;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * A handler class for a specific web form.
 */
public class PraisePhotoFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(PraisePhotoFormHandler.class.getName());

    /**
     *
     */
    public PraisePhotoFormHandler() {
        initialize(PartUtil.PRAISE_PHOTO_FORM_FILE, AccessRights.GUEST);
    }

    /**
     *
     */
    @Override
    protected void doMakeWebPart(UserSession us, WebPart part) {
        PhotoId photoId = us.getPhotoId();
        if (photoId != null) {
            part.addString(Photo.ID, photoId.asString());
        }
    }

    /**
     *
     */
    @Override
    protected boolean isWellFormedPost(UserSession us, Map args) {
        String photoId = us.getAsString(args, Photo.ID);
        Photo photo = GurkenPhotoManager.getInstance().getPhoto(photoId);
        return photo != null;
    }

    /**
     *
     */
    @Override
    protected String doHandlePost(UserSession us, Map args) {
        String photoId = us.getAsString(args, Photo.ID);
        Photo photo = GurkenPhotoManager.getInstance().getPhoto(photoId);
        String praise = us.getAsString(args, Photo.PRAISE);
        Client client = us.getClient();

        boolean wasPraised = false;
        if (!StringUtil.isNullOrEmptyString(praise)) {
            if (!us.hasPraisedPhoto(photo)) {
                int value = Integer.parseInt(praise);
                photo.addToPraise(value);
                client.addPraisedPhotoId(photo.getId());
                us.addProcessedPhoto(photo);
                wasPraised = true;
            }
        }

        log.info(LogBuilder.createUserMessage().addAction(wasPraised ? "PraisePhoto" : "SkipPhoto").toString());

        return PartUtil.SHOW_PHOTO_PAGE_NAME;
    }

}
