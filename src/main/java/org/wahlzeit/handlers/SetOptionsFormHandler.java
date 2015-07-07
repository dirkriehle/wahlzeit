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
import org.wahlzeit.model.Client;
import org.wahlzeit.model.ModelConfig;
import org.wahlzeit.model.PhotoSize;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.Language;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.webparts.WebPart;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public class SetOptionsFormHandler extends AbstractWebFormHandler {

    /**
     *
     */
    public static final String LANGUAGE = "language";
    public static final String PHOTO_SIZE = "photoSize";

    private static final Logger log = Logger.getLogger(SetOptionsFormHandler.class.getName());

    /**
     *
     */
    public SetOptionsFormHandler() {
        initialize(PartUtil.SET_OPTIONS_FORM_FILE, AccessRights.GUEST);
    }

    /**
     *
     */
    protected void doMakeWebPart(UserSession us, WebPart part) {
        Map args = us.getSavedArgs();
        part.addStringFromArgs(args, UserSession.MESSAGE);

        part.addString(UserSession.MESSAGE, us.getMessage());

        part.addSelect(LANGUAGE, Language.class, (String) args.get(LANGUAGE));
        part.addSelect(PHOTO_SIZE, PhotoSize.class, (String) args.get(PHOTO_SIZE));
    }

    /**
     *
     */
    protected String doHandlePost(UserSession us, Map args) {
        String language = us.getAndSaveAsString(args, LANGUAGE);
        Language langValue = Language.getFromString(language);
        Client client = us.getClient();
        client.setLanguage(langValue);

        String photoSize = (String) args.get(PHOTO_SIZE);
        PhotoSize photoValue = PhotoSize.getFromString(photoSize);
        client.setPhotoSize(photoValue);

        log.info(LogBuilder.createUserMessage().
                addAction("Set options").
                addParameter("language", language).
                addParameter("photo size", photoSize).toString());

        ModelConfig config = us.getClient().getLanguageConfiguration();
        String msg1 = config.getOptionsWereSet();
        String msg2 = config.getNoteMaximumPhotoSize();
        String msg3 = config.getContinueWithShowPhoto();
        us.setThreeLineMessage(msg1, msg2, msg3);

        return PartUtil.SHOW_NOTE_PAGE_NAME;
    }

}
