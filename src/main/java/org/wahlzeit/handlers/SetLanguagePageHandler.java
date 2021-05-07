/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.ModelConfig;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.Language;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.WebPart;



/**
 * A handler class for a specific web page.
 */
public class SetLanguagePageHandler extends AbstractWebPageHandler {
	
	/**
	 * 
	 */
	public SetLanguagePageHandler() {
		initialize(PartUtil.SHOW_NOTE_PAGE_FILE, AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		ModelConfig result = LanguageConfigs.get(Language.ENGLISH);
		
		if (link.equals(PartUtil.SET_GERMAN_LANGUAGE_PAGE_NAME)) {
			result = LanguageConfigs.get(Language.GERMAN);
		} else if (link.equals(PartUtil.SET_SPANISH_LANGUAGE_PAGE_NAME)) {
			result = LanguageConfigs.get(Language.ENGLISH);
		} else if (link.equals(PartUtil.SET_JAPANESE_LANGUAGE_PAGE_NAME)) {
			result = LanguageConfigs.get(Language.JAPANESE);
		}
		
		us.setConfiguration(result);
		
		return link;
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		page.addString("noteHeading", us.cfg().getInformation());
		String msg1 = us.cfg().getNewLanguageSet();
		String msg2 = us.cfg().getContinueWithShowPhoto();
		page.addString("note", HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2));
	}

}
