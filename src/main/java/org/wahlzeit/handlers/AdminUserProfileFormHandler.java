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
import org.wahlzeit.model.Gender;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.model.UserStatus;
import org.wahlzeit.services.Language;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.utils.StringUtil;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class AdminUserProfileFormHandler extends AbstractWebFormHandler {

    private static final Logger log = Logger.getLogger(AdminUserProfileFormHandler.class.getName());

    /**
     *
     */
    public AdminUserProfileFormHandler() {
        initialize(PartUtil.ADMIN_USER_PROFILE_FORM_FILE, AccessRights.ADMINISTRATOR);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map<String, Object> args = us.getSavedArgs();

        String userId = us.getAndSaveAsString(args, "userId");
        User user = UserManager.getInstance().getUserById(userId);

        Photo photo = user.getUserPhoto();
        part.addString(Photo.THUMB, getPhotoThumb(us, photo));

        part.maskAndAddString(User.ID, user.getId());
        part.maskAndAddString(User.NICK_NAME, user.getId());
        part.addSelect(User.STATUS, UserStatus.class, (String) args.get(User.STATUS));
        part.addSelect(User.RIGHTS, AccessRights.class, (String) args.get(User.RIGHTS));
        part.addSelect(User.GENDER, Gender.class, (String) args.get(User.GENDER));
        part.addSelect(User.LANGUAGE, Language.class, (String) args.get(User.LANGUAGE));
        part.maskAndAddStringFromArgsWithDefault(args, User.EMAIL_ADDRESS, user.getEmailAddress().asString());

        if (user.getNotifyAboutPraise()) {
            part.addString(User.NOTIFY_ABOUT_PRAISE, HtmlUtil.CHECKBOX_CHECK);
        }
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        UserManager um = UserManager.getInstance();
        String userId = us.getAndSaveAsString(args, "userId");
        User user = um.getUserById(userId);

        String status = us.getAndSaveAsString(args, User.STATUS);
        String rights = us.getAndSaveAsString(args, User.RIGHTS);
        String gender = us.getAndSaveAsString(args, User.GENDER);
        String language = us.getAndSaveAsString(args, User.LANGUAGE);
        String emailAddress = us.getAndSaveAsString(args, User.EMAIL_ADDRESS);
        String notifyAboutPraise = us.getAndSaveAsString(args, User.NOTIFY_ABOUT_PRAISE);

        if (!StringUtil.isValidStrictEmailAddress(emailAddress)) {
            us.setMessage(us.getConfiguration().getEmailAddressIsInvalid());
            return PartUtil.SHOW_ADMIN_PAGE_NAME;
        }

        user.setStatus(UserStatus.getFromString(status));
        user.setAccessRights(AccessRights.getFromString(rights));
        user.setGender(Gender.getFromString(gender));
        user.setLanguage(Language.getFromString(language));
        // TODO user.setEmailAddress(EmailAddress.getFromString(emailAddress));
        user.setNotifyAboutPraise((notifyAboutPraise != null) && notifyAboutPraise.equals("on"));

        um.removeClient(user);
        user = um.getUserById(userId);
        us.setSavedArg("userId", userId);

        log.info(LogBuilder.createUserMessage().
                addAction("AdminUserProfile").
                addParameter("User ID", user.getId()).toString());

        us.setMessage(us.getConfiguration().getProfileUpdateSucceeded());

        return PartUtil.SHOW_ADMIN_PAGE_NAME;
    }

}
