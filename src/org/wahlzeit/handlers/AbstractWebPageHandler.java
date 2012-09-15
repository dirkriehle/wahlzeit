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

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.HtmlUtil;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author dirkriehle
 *
 */
public abstract class AbstractWebPageHandler extends AbstractWebPartHandler implements WebPageHandler {
	
	/**
	 * 
	 */
	public WebPart makeWebPart(UserSession ctx) {
		return makeWebPage(ctx);
	}
	
	/**
	 * 
	 */
	public WebPart makeWebPage(UserSession ctx) {
		WebPart result = createWebPart(ctx);
		
		String siteUrl = SysConfig.getSiteUrlAsString();
		ConfigDir staticDir = SysConfig.getStaticDir();
		String stylesheetUrl = siteUrl + staticDir.getFullConfigFileName("wahlzeit.css");
		result.addString("stylesheet", stylesheetUrl);
		String javascriptUrl = siteUrl + staticDir.getFullConfigFileName("wahlzeit.js");
		result.addString("javascript", javascriptUrl);

		makeWebPageFrame(ctx, result);
		makeWebPageMenu(ctx, result);		
		makeWebPageBody(ctx, result);
		
		return result;
	}

	/**
	 * 
	 */
	protected void makeWebPageFrame(UserSession ctx, WebPart page) {
		page.addString("title", ctx.cfg().getPageTitle());
		
		makeWebPageHeading(ctx, page);
		
		page.addString("footer", ctx.cfg().getPageFooter(ctx.getPhotoSize()));
		page.addString("mission", ctx.cfg().getPageMission());
	}
	
	/**
	 * 
	 */
	protected void makeWebPageHeading(UserSession ctx, WebPart page) {
		Language langValue = ctx.cfg().getLanguage();
		if (isToShowAds(ctx)) {
			WebPart heading = createWebPart(ctx, PartUtil.BANNER_INFO_FILE);
			String imgTag = HtmlUtil.asImg(SysConfig.getLogoImageAsUrlString(langValue));
			heading.addString("logo", HtmlUtil.asHref(SysConfig.getSiteUrlAsString(), imgTag));
			page.addWritable("heading", heading);
		} else {
			String heading = HtmlUtil.asImg(SysConfig.getHeadingImageAsUrlString(langValue));
			heading = HtmlUtil.asHref(SysConfig.getSiteUrlAsString(), heading);
			page.addString("heading", heading);
		}
	}
	
	/**
	 * 
	 */
	protected boolean isToShowAds(UserSession ctx) {
		return false;
	}
	
	/**
	 * 
	 */
	protected void makeWebPageMenu(UserSession ctx, WebPart page) {
		Client client = ctx.getClient();
		String menu = "";
		
		if (client.hasAdministratorRights()) {
			menu = ctx.cfg().getAdministratorMenu();
		} else if (client.hasModeratorRights()) {
			menu = ctx.cfg().getModeratorMenu();
		} else if (client.hasUserRights()) {
			menu = ctx.cfg().getUserMenu();
		} else {
			menu = ctx.cfg().getGuestMenu();
		}
		
		page.addString("menu", menu.toString());
	}
	
	/**
	 * 
	 */
	protected void makeWebPageBody(UserSession ctx, WebPart page) {
		// do nothing by default
	}
	
}
