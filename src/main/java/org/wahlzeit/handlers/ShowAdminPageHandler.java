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

import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;
import org.wahlzeit.webparts.Writable;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class ShowAdminPageHandler extends AbstractWebPageHandler implements WebFormHandler {

    private static Logger log = Logger.getLogger(ShowAdminPageHandler.class.getName());

    /**
     *
     */
    public ShowAdminPageHandler() {
        initialize(PartUtil.SHOW_ADMIN_PAGE_FILE, AccessRights.ADMINISTRATOR);
    }

    /**
     *
     */
    protected void makeWebPageBody(UserSession us, WebPart page) {
        Map args = us.getSavedArgs();
        page.addStringFromArgs(args, UserSession.MESSAGE);

        Object userId = us.getSavedArg("userId");
        if (!StringUtil.isNullOrEmptyString(userId)) {
            page.addStringFromArgs(args, "userId");
            page.addWritable("object", makeAdminUserProfile(us));
        }

        Object photoId = us.getSavedArg("photoId");
        if (!StringUtil.isNullOrEmptyString(photoId)) {
            page.addStringFromArgs(args, "photoId");
            page.addWritable("object", makeAdminUserPhoto(us));
        }
    }

    /**
     *
     */
    protected Writable makeAdminUserProfile(UserSession us) {
        WebFormHandler handler = getFormHandler(PartUtil.NULL_FORM_NAME);

        String userId = us.getSavedArg("userId").toString();
        User user = UserManager.getInstance().getUserById(userId);
        log.config("UserId: " + userId);
        if (user != null) {
            log.config("User = null");
            handler = getFormHandler(PartUtil.ADMIN_USER_PROFILE_FORM_NAME);
        }

        return handler.makeWebPart(us);
    }

    /**
     *
     */
    protected Writable makeAdminUserPhoto(UserSession us) {
        WebFormHandler handler = getFormHandler(PartUtil.NULL_FORM_NAME);

        String photoId = us.getSavedArg("photoId").toString();
        Photo photo = PhotoManager.getPhoto(photoId);
        if (photo != null) {
            handler = getFormHandler(PartUtil.ADMIN_USER_PHOTO_FORM_NAME);
        }

        return handler.makeWebPart(us);
    }

    /**
     *
     */
    public String handlePost(UserSession us, Map args) {
        if (!hasAccessRights(us, args)) {
            log.warning(LogBuilder.createSystemMessage().
                    addParameter("insufficient rights for POST from", us.getEmailAddressAsString()).toString());
            return getIllegalAccessErrorPage(us);
        }

        String result = PartUtil.SHOW_ADMIN_PAGE_NAME;

        if (us.isFormType(args, "adminUser")) {
            result = performAdminUserProfileRequest(us, args);
        } else if (us.isFormType(args, "adminPhoto")) {
            result = performAdminUserPhotoRequest(us, args);
        } else if (us.isFormType(args, "saveAll")) {
            result = performSaveAllRequest(us);
        } else if (us.isFormType(args, "shutdown")) {
            result = performShutdownRequest(us);
        }

        return result;
    }

    /**
     *
     */
    protected String performAdminUserProfileRequest(UserSession us, Map args) {
        String userId = us.getAndSaveAsString(args, "userId");
        log.config("UserId: " + userId);
        User user = UserManager.getInstance().getUserById(userId);
        if (user == null) {
            log.config("User = null");
            us.setMessage(us.getConfiguration().getUserNameIsUnknown());
        }

        return PartUtil.SHOW_ADMIN_PAGE_NAME;
    }

    /**
     *
     */
    protected String performAdminUserPhotoRequest(UserSession us, Map args) {
        String photoId = us.getAndSaveAsString(args, "photoId");
        Photo photo = PhotoManager.getPhoto(photoId);
        if (photo == null) {
            us.setMessage(us.getConfiguration().getPhotoIsUnknown());
        }

        return PartUtil.SHOW_ADMIN_PAGE_NAME;
    }

    /**
     *
     */
    protected String performSaveAllRequest(UserSession us) {
        log.info(LogBuilder.createSystemMessage().addAction("save all objects").toString());

        try {
            ServiceMain.getInstance().saveAll();
        } catch (Exception ex) {
            log.warning(LogBuilder.createSystemMessage().addException("saving all objects failed", ex).toString());
        }

        us.setMessage("Saved objects...");
        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

    /**
     *
     */
    protected String performShutdownRequest(UserSession us) {
        log.info(LogBuilder.createSystemMessage().addAction("shutting system down").toString());
        try {
            ServiceMain.getInstance().requestStop();
        } catch (Exception ex) {
            log.warning(LogBuilder.createSystemMessage().addException("requesting stop failed", ex).toString());
        }

        us.setMessage("Shutting down...");
        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}