/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.AccessRights;
import org.wahlzeit.model.PhotoCase;
import org.wahlzeit.model.PhotoCaseManager;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.Writable;
import org.wahlzeit.webparts.WebPart;
import org.wahlzeit.webparts.WritableList;



/**
 * A handler class for a specific web page.
 */
public class ShowPhotoCasesPageHandler extends AbstractWebPageHandler {
	
	/**
	 *
	 */
	public ShowPhotoCasesPageHandler() {
		initialize(PartUtil.SHOW_PHOTO_CASES_PAGE_FILE, AccessRights.MODERATOR);
	}

	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		Map args = us.getSavedArgs();
		page.addStringFromArgs(args, UserSession.MESSAGE);
		
		PhotoCaseManager pcm = PhotoCaseManager.getInstance();
		PhotoCase[] flaggedCases = pcm.getOpenPhotoCasesByAscendingAge();
		if (flaggedCases.length != 0) {
			WritableList openCases = new WritableList();
			for (int i = 0; i < flaggedCases.length; i++) {
				openCases.append(makePhotoCaseForm(us, flaggedCases[i]));
			}
			page.addWritable("openCases", openCases);
		} else {
			page.addString("openCases", HtmlUtil.asP(us.cfg().getNoFlaggedPhotoCases()));
		}
	}
	
	/**
	 * 
	 */
	protected Writable makePhotoCaseForm(UserSession us, PhotoCase photoCase) {
		us.setPhotoCase(photoCase);
		WebFormHandler handler = getFormHandler(PartUtil.EDIT_PHOTO_CASE_FORM_NAME);
		return handler.makeWebPart(us);
	}
	
}
