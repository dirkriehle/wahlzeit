/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.*;

/**
 * A superclass for handling web pages.
 */
public abstract class AbstractWebPageHandler extends AbstractWebPartHandler implements WebPageHandler {
	
	/**
	 * 
	 */
	public WebPart makeWebPart(UserSession us) {
		return makeWebPage(us);
	}
	
	/**
	 * 
	 */
	public WebPart makeWebPage(UserSession us) {
		WebPart result = createWebPart(us);
		
		ConfigDir staticDir = SysConfig.getStaticDir();
		String stylesheetUrl = HtmlUtil.asPath(staticDir.getRelativeConfigFileName("wahlzeit.css"));
		result.addString("stylesheet", stylesheetUrl);
		String javascriptUrl = HtmlUtil.asPath(staticDir.getRelativeConfigFileName("wahlzeit.js"));
		result.addString("javascript", javascriptUrl);

		makeWebPageFrame(us, result);
		makeWebPageMenu(us, result);		
		makeWebPageBody(us, result);
		
		return result;
	}

	/**
	 * 
	 */
	protected void makeWebPageFrame(UserSession us, WebPart page) {
		page.addString("title", us.cfg().getPageTitle());
		
		makeWebPageHeading(us, page);
		
		page.addString("footer", us.cfg().getPageFooter(us.getPhotoSize()));
		page.addString("mission", us.cfg().getPageMission());
	}
	
	/**
	 * 
	 */
	protected void makeWebPageHeading(UserSession us, WebPart page) {
		Language langValue = us.cfg().getLanguage();
		String heading = HtmlUtil.asImg(getHeadingImageAsRelativeResourcePathString(langValue));
		heading = HtmlUtil.asHref(us.getSiteUrl(), heading);
		page.addString("heading", heading);
	}
	
	/**
	 * @methodtype boolean-query
	 */
	protected boolean isToShowAds(UserSession us) {
		return false;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageMenu(UserSession us, WebPart page) {
		Client client = us.getClient();
		String menu = "";
		
		if (client.hasAdministratorRights()) {
			menu = us.cfg().getAdministratorMenu();
		} else if (client.hasModeratorRights()) {
			menu = us.cfg().getModeratorMenu();
		} else if (client.hasUserRights()) {
			menu = us.cfg().getUserMenu();
		} else {
			menu = us.cfg().getGuestMenu();
		}
		
		page.addString("menu", menu.toString());
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession us, WebPart page) {
		// do nothing by default
	}
	
}
