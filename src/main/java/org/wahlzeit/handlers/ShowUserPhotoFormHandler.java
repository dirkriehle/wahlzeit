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
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class ShowUserPhotoFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(ShowUserPhotoFormHandler.class.getName());


    /**
     *
     */
    public ShowUserPhotoFormHandler() {
        initialize(PartUtil.SHOW_USER_PHOTO_FORM_FILE, AccessRights.USER);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        PhotoId photoId = us.getPhotoId();
        Photo photo = PhotoManager.getPhoto(photoId);
        String id = photo.getId().asString();
        part.addString(Photo.ID, id);
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));

        part.addString(Photo.PRAISE, photo.getPraiseAsString(us.getConfiguration()));

        String tags = photo.getTags().asString();
        tags = !StringUtil.isNullOrEmptyString(tags) ? tags : us.getConfiguration().getNoTags();
        part.maskAndAddString(Photo.TAGS, tags);

        String photoStatus = us.getConfiguration().asValueString(photo.getStatus());
        part.addString(Photo.STATUS, photoStatus);

        part.addString(Photo.UPLOADED_ON, us.getConfiguration().asDateString(photo.getCreationTime()));
        part.addString(Photo.LINK, HtmlUtil.asHref(getResourceAsRelativeHtmlPathString(id)));
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
        String result = PartUtil.SHOW_USER_HOME_PAGE_NAME;

        String id = us.getAndSaveAsString(args, Photo.ID);
        Photo photo = PhotoManager.getPhoto(id);

        UserManager userManager = UserManager.getInstance();
        User user = userManager.getUserById(photo.getOwnerId());
        if (us.isFormType(args, "edit")) {
            us.setPhotoId(photo.getId());
            result = PartUtil.EDIT_USER_PHOTO_PAGE_NAME;
        } else if (us.isFormType(args, "tell")) {
            us.setPhotoId(photo.getId());
            result = PartUtil.TELL_FRIEND_PAGE_NAME;
        } else if (us.isFormType(args, "select")) {
            user.setUserPhoto(photo);
            us.setClient(user);
            userManager.saveClient(user);
            log.info(LogBuilder.createUserMessage().
                    addAction("Select user photo").
                    addParameter("Photo", id).toString());
        } else if (us.isFormType(args, "delete")) {
            photo.setStatus(photo.getStatus().asDeleted(true));
            PhotoManager.getInstance().savePhoto(photo);
            if (user.getUserPhoto() == photo) {
                user.setUserPhoto(null);
                userManager.saveClient(user);
            }
            log.info(LogBuilder.createUserMessage().
                    addAction("Deselect user photo").toString());
        }

        return result;
    }

}
