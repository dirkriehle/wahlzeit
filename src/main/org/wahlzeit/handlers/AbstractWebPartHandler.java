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

import java.io.File;
import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author dirkriehle
 *
 */
public abstract class AbstractWebPartHandler implements WebPartHandler {
	
	/**
	 * 
	 */
	protected String tmplName;
	
	/**
	 * 
	 */
	protected AccessRights neededRights;
	
	/**
	 * 
	 */
	protected void initialize(String myTmplName, AccessRights myRights) {
		tmplName = myTmplName;
		neededRights = myRights;
	}
	
	/**
	 * @methodtype factory
	 */
	protected final WebPart createWebPart(UserSession us) {
		return createWebPart(us, tmplName);
	}
	
	/**
	 * @methodtype factory
	 */
	protected final WebPart createWebPart(UserSession us, String name) {
		WebPartTemplateService wpts = WebPartTemplateService.getInstance();
		WebPartTemplate tmpl = wpts.getTemplate(us.cfg().getLanguageCode(), name);
		return new WebPart(tmpl);
	}
		
	/**
	 * 
	 */
	protected String getPhotoThumb(UserSession us, Photo photo) {
		String result = null;
		if (photo != null) {
			String imageLink = getPhotoAsRelativeResourcePathString(photo, PhotoSize.THUMB);
			result = HtmlUtil.asImg(HtmlUtil.asPath(imageLink), photo.getThumbWidth(), photo.getThumbHeight());
		} else {
			Language langValue = us.cfg().getLanguage();
			result = HtmlUtil.asImg(getEmptyImageAsRelativeResourcePathString(langValue));
		}
		return result;
	}
	
	/**
	 * 
	 */
	protected String getPhotoSummary(UserSession us, Photo photo) {
		return photo.getSummary(us.cfg());
	}
	
	/**
	 * 
	 */
	protected String getPhotoCaption(UserSession us, Photo photo) {
		return photo.getCaption(us.cfg());
	}
	
	/**
	 * 
	 */
	protected final WebFormHandler getFormHandler(String name) {
		return WebPartHandlerManager.getWebFormHandler(name);
	}
	
	/**
	 * 
	 */
	public final AccessRights getNeededRights() {
		return neededRights;
	}
	
	/**
	 * 
	 */
	protected boolean hasAccessRights(UserSession us, Map args) {
		return us.getClient().hasRights(getNeededRights());
	}
	
	/**
	 * 
	 */
	protected boolean isWellFormedGet(UserSession us, String link, Map args) {
		return true;
	}
	
	/**
	 * 
	 */
	protected boolean hasSavedPhotoId(UserSession us) {
		String id = us.getAsString(us.getSavedArgs(), Photo.ID);
		return !StringUtil.isNullOrEmptyString(id);
	}
	
	/**
	 * 
	 */
	protected boolean isSavedPhotoVisible(UserSession us) {
		String id = us.getAsString(us.getSavedArgs(), Photo.ID);
		Photo photo = PhotoManager.getPhoto(id);
		return photo.isVisible();
	}
	
	/**
	 * 
	 */
	protected boolean hasSavedMessage(UserSession us) {
		return !StringUtil.isNullOrEmptyString(us.getMessage());
	}

	/**
	 * 
	 */
	public final String handleGet(UserSession us, String link, Map args) {
		if (!hasAccessRights(us, args)) {
			SysLog.logSysInfo("insufficient rights for GET from: " + us.getEmailAddressAsString());
			return getIllegalAccessErrorPage(us);
		}
		
		if (!isWellFormedGet(us, link, args)) {
			SysLog.logSysInfo("received ill-formed GET from: " + us.getEmailAddressAsString());
			return getIllegalArgumentErrorPage(us);
		}
		
		try {
			// may throw Exception
			return doHandleGet(us, link, args);
		} catch (Throwable t) {
			SysLog.logThrowable(t);
			return getInternalProcessingErrorPage(us);
		}
	}
		
	/**
	 * @param args TODO
	 * 
	 */
	protected String doHandleGet(UserSession us, String link, Map args) {
		return link;
	}
	
	/**
	 * 
	 */
	protected String getIllegalAccessErrorPage(UserSession us) {
		us.setHeading(us.cfg().getInformation());
		
		String msg1 = us.cfg().getIllegalAccessError();
		us.setMessage(msg1);
		
		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}

	/**
	 * 
	 */
	protected String getIllegalArgumentErrorPage(UserSession us) {
		us.setHeading(us.cfg().getInformation());

		String msg1 = us.cfg().getIllegalArgumentError();
		String msg2 = us.cfg().getContinueWithShowPhoto();
		if (us.getClient() instanceof User) {
			msg2 = us.cfg().getContinueWithShowUserHome();
		}
		
		us.setTwoLineMessage(msg1, msg2);

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}

	/**
	 * 
	 */
	protected String getInternalProcessingErrorPage(UserSession us) {
		us.setHeading(us.cfg().getInformation());

		String msg1 = us.cfg().getInternalProcessingError();
		String msg2 = us.cfg().getContinueWithShowPhoto();
		if (us.getClient() instanceof User) {
			msg2 = us.cfg().getContinueWithShowUserHome();
		}
		
		us.setTwoLineMessage(msg1, msg2);

		return PartUtil.SHOW_NOTE_PAGE_NAME;
	}
	
	/**
	 * 
	 */
	protected String getEmptyImageAsRelativeResourcePathString(Language lang) {
		String resName = lang.asIsoCode() + File.separator + "empty.png";
		return HtmlUtil.asPath(SysConfig.getStaticDir().getRelativeConfigFileName(resName));
	}

	/**
	 * 
	 */
	protected String getHeadingImageAsRelativeResourcePathString(Language lang) {
		String resName = lang.asIsoCode() + File.separator + "heading.png";
		return HtmlUtil.asPath(SysConfig.getStaticDir().getRelativeConfigFileName(resName));
	}
	
	/**
	 * 
	 */
	protected String getResourceAsRelativeHtmlPathString(String resource) {
		return resource + ".html";
	}
	
	/**
	 * 
	 */
	protected String getPhotoAsRelativeResourcePathString(Photo photo, PhotoSize size) {
		String resName = photo.getId().asString() + size.asInt() + ".jpg";
		return SysConfig.getPhotosDir().getRelativeDir() + '/' + resName;
	}

}
