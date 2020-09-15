/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle https://dirkriehle.com
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.util.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * Wrapper class for HttpSession to provide a readable interface for Wahlzeit.
 */
public class UserSession extends Session {

	/**
	 * 
	 */
	public static final String PHOTO = "photo";
	public static final String PRIOR_PHOTO = "priorPhoto";
	public static final String PHOTO_CASE = "photoCase";
	public static final String MESSAGE = "message";
	public static final String HEADING = "heading";
	public static final String USER = "user";
	
	/**
	 * Session state
	 */
	protected ModelConfig configuration = LanguageConfigs.get(Language.ENGLISH);
	
	protected String siteUrl = null; // @TODO Application

	protected Client client = new Guest();
	protected PhotoSize photoSize = PhotoSize.MEDIUM;
	protected long confirmationCode = -1; // -1 means not set
	protected PhotoFilter photoFilter = PhotoFactory.getInstance().createPhotoFilter();
	protected Set praisedPhotos = new HashSet<Photo>();

	/**
	 * Transaction state
	 */
	protected Map<String, Object> savedArgs = new HashMap<String, Object>();

	/**
	 * 
	 */
	public UserSession(String myName, String mySiteUrl) {
		initialize(myName);
		siteUrl = mySiteUrl;
	}
	
	/**
	 * 
	 */
	public void clear() {
		configuration = LanguageConfigs.get(Language.ENGLISH);
		photoSize = PhotoSize.MEDIUM;
		clearDisplayedPhotos();
		clearPraisedPhotos();
	}
	
	/**
	 * 
	 */
	public ModelConfig cfg() {
		return configuration;
	}
	
	/**
	 * 
	 */
	public void setConfiguration(ModelConfig cfg) {
		configuration = cfg;
	}
	
	/**
	 * 
	 */
	public String getSiteUrl() {
		return siteUrl;
	}
	
	/**
	 * 
	 */
	public Client getClient() {
		return client;
	}
	
	/**
	 * @methodtype set
	 */
	public void setClient(Client newClient) {
		client = newClient;
	}
	
	/**
	 * Returns some signifier of current user
	 */
	public String getClientName() {
		String result = "anon";
		if (!StringUtil.isNullOrEmptyString(getEmailAddressAsString())) {
			result = getEmailAddressAsString();
			if (client instanceof User) {
				User user = (User) client;
				result = user.getName();
			} 
		}
		return result;
	}
	
	/**
	 * 
	 */
	public String getEmailAddressAsString() {
		String result = null;
		if (client != null) {
			result = client.getEmailAddress().asString();
		}
		return result;
	}
	
	/**
	 * 
	 */
	public void setEmailAddress(EmailAddress emailAddress) {
		if (client != null) {
			client.setEmailAddress(emailAddress);
		} else {
			SysLog.logSysError("attempted to set email address to null client");
		}
	}
	
	/**
	 * 
	 */
	public PhotoSize getPhotoSize() {
		return photoSize;
	}
	
	/**
	 * 
	 */
	public void setPhotoSize(PhotoSize newPhotoSize) {
		photoSize = newPhotoSize;
	}
	
	/**
	 * 
	 */
	public boolean hasConfirmationCode() {
		return confirmationCode != -1;
	}
	
	/**
	 * 
	 */
	public long getConfirmationCode() {
		return confirmationCode;
	}
	
	/**
	 * 
	 */
	public void setConfirmationCode(long vc) {
		confirmationCode = vc;
	}
	
	/**
	 * 
	 */
	public void clearConfirmationCode() {
		confirmationCode = -1;
	}
	
	/**
	 * 
	 */
	public PhotoFilter getPhotoFilter() {
		return photoFilter;
	}
	
	/**
	 * 
	 */
	public boolean hasPraisedPhoto(Photo photo) {
		return praisedPhotos.contains(photo);
	}
	
	/**
	 * 
	 */
	public void addPraisedPhoto(Photo photo) {
		praisedPhotos.add(photo);
	}
	
	/**
	 * 
	 */
	public void clearPraisedPhotos() {
		praisedPhotos.clear();
	}
	
	/**
	 * 
	 */
	public void addDisplayedPhoto(Photo photo) {
		photoFilter.addProcessedPhoto(photo);
	}
	
	/**
	 * 
	 */
	public void clearDisplayedPhotos() {
		photoFilter.clear();
	}
	
	/**
	 * 
	 */
	public String getHeading() {
		return (String) savedArgs.get(HEADING);
	}
	
	/**
	 * 
	 */
	public void setHeading(String myHeading) {
		savedArgs.put(HEADING, myHeading);
	}
	
	/**
	 * 
	 */
	public String getMessage() {
		return (String) savedArgs.get(MESSAGE);
	}
	
	/**
	 * 
	 */
	public void setMessage(String myMessage) {
		savedArgs.put(MESSAGE, HtmlUtil.asP(myMessage));
	}
	
	/**
	 * 
	 */
	public void setTwoLineMessage(String msg1, String msg2) {
		savedArgs.put(MESSAGE, HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2));
	}
	
	/**
	 * 
	 */
	public void setThreeLineMessage(String msg1, String msg2, String msg3) {
		savedArgs.put(MESSAGE, HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2) + HtmlUtil.asP(msg3));
	}
	
	/**
	 * 
	 */
	public Photo getPhoto() {
		return (Photo) savedArgs.get(PHOTO);
	}
	
	/**
	 * 
	 */
	public void setPhoto(Photo newPhoto) {
		savedArgs.put(PHOTO, newPhoto);
	}
		
	/**
	 * 
	 */
	public Photo getPriorPhoto() {
		return (Photo) savedArgs.get(PRIOR_PHOTO);
	}
	
	/**
	 * 
	 */
	public void setPriorPhoto(Photo oldPhoto) {
		savedArgs.put(PRIOR_PHOTO, oldPhoto);
	}
	
	/**
	 * 
	 */
	public PhotoCase getPhotoCase() {
		return (PhotoCase) savedArgs.get(PHOTO_CASE);
	}
	
	/**
	 * 
	 */
	public void setPhotoCase(PhotoCase photoCase) {
		savedArgs.put(PHOTO_CASE, photoCase);
	}
	
	/**
	 * 
	 */
	public boolean isPhotoOwner(Photo photo) {
		boolean result = false;
		Client client = getClient();
		if ((photo != null) && (client instanceof User)) {
			User user = (User) client;
			result = photo.getOwnerName().equals(user.getName());
		}
		return result;
	}
	
	/**
	 * 
	 */
	public Object getSavedArg(String key) {
		return savedArgs.get(key);
	}

	/**
	 * 
	 */
	public void setSavedArg(String key, Object value) {
		savedArgs.put(key, value);
	}
	
	/**
	 * 
	 */
	public Map<String, Object> getSavedArgs() {
		return savedArgs;
	}
	
	/**
	 * 
	 */
	public void clearSavedArgs() {
		savedArgs.clear();
	}
	
	/**
	 * 
	 */
	public boolean isFormType(Map args, String type) {
		Object value = args.get(type);
		return (value != null) && !value.equals("");
	}

	/**
	 * 
	 */
	public String getAsString(Map args, String key) {
		String result = null;
		
		Object value = args.get(key);
		if (value == null) {
			result = "";
		} else if (value instanceof String) {
			result = (String) value;
		} else if (value instanceof String[]) {
			String[] array = (String[]) value;
			result = array[0];
		} else {
			result = value.toString();
		}
		
		return result;
	}

	/**
	 * 
	 */
	public String getAndSaveAsString(Map args, String key) {
		String result = getAsString(args, key);
		savedArgs.put(key, result);
		return result;
	}
	
}
