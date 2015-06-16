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

import org.wahlzeit.agents.AsyncTaskExecutor;
import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.PhotoStatus;
import org.wahlzeit.model.Tags;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class AdminUserPhotoFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(AdminUserPhotoFormHandler.class.getName());


    /**
     *
     */
    public AdminUserPhotoFormHandler() {
        initialize(PartUtil.ADMIN_USER_PHOTO_FORM_FILE, AccessRights.ADMINISTRATOR);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        String photoId = (String)us.getSavedArg("photoId");
        Photo photo = PhotoManager.getPhoto(photoId);
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));

        part.addString("photoId", photoId);
        part.addString(Photo.ID, photo.getId().asString());
        part.addSelect(Photo.STATUS, PhotoStatus.class, (String) us.getSavedArg(Photo.STATUS));
        part.maskAndAddStringFromArgsWithDefault(us.getSavedArgs(), Photo.TAGS, photo.getTags().asString());
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        String id = us.getAndSaveAsString(args, "photoId");
        Photo photo = PhotoManager.getPhoto(id);

        String tags = us.getAndSaveAsString(args, Photo.TAGS);
        photo.setTags(new Tags(tags));
        String status = us.getAndSaveAsString(args, Photo.STATUS);
        photo.setStatus(PhotoStatus.getFromString(status));

        AsyncTaskExecutor.savePhotoAsync(id);

        log.info(LogBuilder.createUserMessage().
                addAction("AdminUserPhoto").
                addParameter("Photo", photo.getId().asString()).toString());

        us.setMessage(us.getConfiguration().getPhotoUpdateSucceeded());

        return PartUtil.SHOW_ADMIN_PAGE_NAME;
    }

}
