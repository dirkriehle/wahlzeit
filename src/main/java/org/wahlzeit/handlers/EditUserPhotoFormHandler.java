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
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class EditUserPhotoFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(EditUserPhotoFormHandler.class.getName());


    /**
     *
     */
    public EditUserPhotoFormHandler() {
        initialize(PartUtil.EDIT_USER_PHOTO_FORM_FILE, AccessRights.USER);
    }

    /**
     *
     */
    protected boolean isWellFormedGet(UserSession us, String link, Map args) {
        return hasSavedPhotoId(us);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map<String, Object> args = us.getSavedArgs();
        part.addStringFromArgs(args, UserSession.MESSAGE);

        String id = us.getAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(id);

        part.addString(Photo.ID, id);
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));

        part.addString(Photo.PRAISE, photo.getPraiseAsString(us.getConfiguration()));
        part.maskAndAddString(Photo.TAGS, photo.getTags().asString());

        part.addString(Photo.IS_INVISIBLE, HtmlUtil.asCheckboxCheck(photo.getStatus().isInvisible()));
        part.addString(Photo.STATUS, us.getConfiguration().asValueString(photo.getStatus()));
        part.addString(Photo.UPLOADED_ON, us.getConfiguration().asDateString(photo.getCreationTime()));
    }

    /**
     *
     */
    protected boolean isWellFormedPost(UserSession us, Map args) {
        String id = us.getAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(id);
        return (photo != null) && us.isPhotoOwner(photo);
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        String id = us.getAndSaveAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(id);

        String tags = us.getAndSaveAsString(args, Photo.TAGS);
        photo.setTags(new Tags(tags));

        String status = us.getAndSaveAsString(args, Photo.IS_INVISIBLE);
        boolean isInvisible = (status != null) && status.equals("on");
        PhotoStatus ps = photo.getStatus().asInvisible(isInvisible);
        photo.setStatus(ps);

        AsyncTaskExecutor.savePhotoAsync(id);

        log.info(LogBuilder.createUserMessage().
                addAction("EditUserPhoto").
                addParameter("Photo", photo.getId().asString()).toString());

        us.setTwoLineMessage(us.getConfiguration().getPhotoUpdateSucceeded(), us.getConfiguration().getContinueWithShowUserHome());

        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}
