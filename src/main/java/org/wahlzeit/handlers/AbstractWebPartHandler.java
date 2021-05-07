/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.io.File;
import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.webparts.*;

/**
 * A superclass for handling parts of web pages.
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
