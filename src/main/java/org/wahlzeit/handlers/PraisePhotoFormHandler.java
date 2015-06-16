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
import org.wahlzeit.model.PhotoId;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;


/**
 * @author dirkriehle
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
    protected void doMakeWebPart(UserSession us, WebPart part) {
        PhotoId photoId = us.getPhotoId();
        if (photoId != null) {
            part.addString(Photo.ID, photoId.asString());
        }
    }

    /**
     *
     */
    protected boolean isWellFormedPost(UserSession us, Map args) {
        String photoId = us.getAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(photoId);
        return photo != null;
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        String photoId = us.getAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(photoId);
        String praise = us.getAsString(args, Photo.PRAISE);

        boolean wasPraised = false;
        if (!StringUtil.isNullOrEmptyString(praise)) {
            if (!us.hasPraisedPhoto(photo)) {
                int value = Integer.parseInt(praise);
                photo.addToPraise(value);
                us.addPraisedPhoto(photo);
                wasPraised = true;
            }
        }

        us.setPriorPhoto(photo);

        log.info(LogBuilder.createUserMessage().addAction(wasPraised ? "PraisePhoto" : "SkipPhoto").toString());

        return PartUtil.SHOW_PHOTO_PAGE_NAME;
    }

}
