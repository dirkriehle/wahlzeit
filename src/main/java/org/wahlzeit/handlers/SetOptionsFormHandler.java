/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.webparts.*;

/**
 * A handler class for a specific web form.
 */
public class SetOptionsFormHandler extends AbstractWebFormHandler {
	
	/**
	 * 
	 */
	public static final String LANGUAGE = "language";
	public static final String PHOTO_SIZE = "photoSize";
	
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
		us.setConfiguration(LanguageConfigs.get(langValue));

		String photoSize = us.getAndSaveAsString(args, PHOTO_SIZE);
		PhotoSize photoValue = PhotoSize.getFromString(photoSize);
		us.setPhotoSize(photoValue);
		
		StringBuffer sb = UserLog.createActionEntry("SetOptions");
		UserLog.addField(sb, "language", language);
		UserLog.addField(sb, "photoSize", photoSize);
		UserLog.log(sb);
		
		String msg1 = us.cfg().getOptionsWereSet();
		String msg2 = us.cfg().getNoteMaximumPhotoSize();
		String msg3 = us.cfg().getContinueWithShowPhoto();
		us.setThreeLineMessage(msg1, msg2, msg3);

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
}
