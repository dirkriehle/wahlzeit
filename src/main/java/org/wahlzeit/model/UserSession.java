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

package org.wahlzeit.model;

import org.wahlzeit.services.Language;
import org.wahlzeit.services.Session;
import org.wahlzeit.utils.HtmlUtil;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Wrapper class for {@link HttpSession} to provide a readable interface for Wahlzeit.
 * {@link HttpSession}s are managed automatically by Google App Engine.
 */
public class UserSession extends Session implements Serializable {

    private static Logger log = Logger.getLogger(UserSession.class.getName());
    protected HttpSession httpSession;
    /**
     * Keys to store the according properties in the <code>HttpSession</code>
     */
    public static final String PHOTO_CASE = "photoCase";
    public static final String PHOTO_FILTER = "photoFilter";
    public static final String PRAISED_PHOTOS = "praisedPhotoIds";
    public static final String MESSAGE = "message";
    public static final String HEADING = "heading";
    public static final String CLIENT_ID = "clientId";
    public static final String SITE_URL = "siteUrl";
    public static final String SAVED_ARGS = "savedArgs";
    public static final String INITIALIZED = "initialized";
    public static final String ANONYMOUS_CLIENT = "anon";

    /**
     *
     */
    public UserSession(String myName, String mySiteUrl, HttpSession myHttpSession, String myLanguage) {
        httpSession = myHttpSession;
        initialize(myName);
        if (httpSession.getAttribute(INITIALIZED) == null) {
            httpSession.setAttribute(SITE_URL, mySiteUrl);
            httpSession.setAttribute(PHOTO_FILTER, PhotoFactory.getInstance().createPhotoFilter());

            setClient(new Guest());
            try {
                Language language = Language.getFromIsoCode(myLanguage);
                getClient().setLanguage(language);
            } catch (IllegalArgumentException e) {
                // default language of guest is english
            }

            clearDisplayedPhotos();
            clearPraisedPhotos();
            clearSavedArgs();
            httpSession.setAttribute(INITIALIZED, INITIALIZED);

        }
    }

    /**
     *
     */
    public void clearDisplayedPhotos() {
        PhotoFilter photoFilter = (PhotoFilter) httpSession.getAttribute(PHOTO_FILTER);
        if (photoFilter != null) {
            photoFilter.clear();
            httpSession.setAttribute(PHOTO_FILTER, photoFilter);
        } else {
            log.warning("No PhotoFilter found in HttpSession to clear.");
        }
    }

    /**
     *
     */
    public void clearPraisedPhotos() {
        httpSession.setAttribute(PRAISED_PHOTOS, new HashSet<Photo>());
    }

    /**
     * @methodtype init
     */
    public void clearSavedArgs() {
        httpSession.setAttribute(SAVED_ARGS, new HashMap<String, Object>());
    }

    /**
     * @methodtype init
     */
    public void clear() {
        clearDisplayedPhotos();
        clearPraisedPhotos();
    }

    /**
     * @methodtype get
     */
    public String getSiteUrl() {
        return (String) httpSession.getAttribute(SITE_URL);
    }

    /**
     * @methodtype get
     */
    public Client getClient() {
        String clientName = (String) httpSession.getAttribute(CLIENT_ID);
        return UserManager.getInstance().getClientById(clientName);
    }

    /**
     * @methodtype set
     */
    public void setClient(Client newClient) {
        String previousClientId = (String) httpSession.getAttribute(CLIENT_ID);
        if (previousClientId != null) {
            Client previousClient = UserManager.getInstance().getClientById(previousClientId);
            if (previousClient instanceof Guest) {
                UserManager.getInstance().deleteClient(previousClient);
            }
        }

        httpSession.setAttribute(CLIENT_ID, newClient.getId());
        UserManager.getInstance().addHttpSessionIdToClientMapping(httpSession.getId(), newClient);
    }

    /**
     *
     */
    public PhotoFilter getPhotoFilter() {
        return (PhotoFilter) httpSession.getAttribute(PHOTO_FILTER);
    }

    /**
     * @methodtype boolean query
     */
    public boolean hasPraisedPhoto(Photo photo) {
        Set praisedPhotos = (Set) httpSession.getAttribute(PRAISED_PHOTOS);
        if (praisedPhotos != null) {
            return praisedPhotos.contains(photo);
        } else {
            log.warning("Found no set of praised Photos to search for Photo.");
            return false;
        }
    }

    /**
     *
     */
    public void addProcessedPhoto(Photo photo) {
        PhotoFilter photoFilter = (PhotoFilter) httpSession.getAttribute(PHOTO_FILTER);
        if (photoFilter != null) {
            photoFilter.addProcessedPhoto(photo);
            httpSession.setAttribute(PHOTO_FILTER, photoFilter);
        } else {
            log.warning("No PhotoFilter found in HttpSession to add Photo.");
        }
    }

    /**
     * @methodtype get
     */
    public String getHeading() {
        return (String) httpSession.getAttribute(HEADING);
    }

    /**
     * @methodtype set
     */
    public void setHeading(String myHeading) {
        httpSession.setAttribute(HEADING, myHeading);
    }

    /**
     * @methodtype get
     */
    public String getMessage() {
        return (String) httpSession.getAttribute(MESSAGE);
    }

    /**
     * @methodtype set
     */
    public void setMessage(String myMessage) {
        httpSession.setAttribute(MESSAGE, HtmlUtil.asP(myMessage));
    }

    /**
     * @methodtype set
     */
    public void setTwoLineMessage(String msg1, String msg2) {
        httpSession.setAttribute(MESSAGE, HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2));
    }

    /**
     * @methodtype set
     */
    public void setThreeLineMessage(String msg1, String msg2, String msg3) {
        httpSession.setAttribute(MESSAGE, HtmlUtil.asP(msg1) + HtmlUtil.asP(msg2) + HtmlUtil.asP(msg3));
    }

    /**
     * @methodtype get
     */
    public PhotoId getPhotoId() {
        return (PhotoId) httpSession.getAttribute(Photo.ID);
    }

    /**
     * @methodtype set
     */
    public void setPhotoId(PhotoId newPhotoId) {
        httpSession.setAttribute(Photo.ID, newPhotoId);
    }

    /**
     * @methodtype get
     */
    public PhotoCase getPhotoCase() {
        return (PhotoCase) httpSession.getAttribute(PHOTO_CASE);
    }

    /**
     * @methodtype set
     */
    public void setPhotoCase(PhotoCase photoCase) {
        httpSession.setAttribute(PHOTO_CASE, photoCase);
    }

    /**
     * @methodtype boolean query
     */
    public boolean isPhotoOwner(Photo photo) {
        boolean result = false;
        Client client = getClient();
        if ((photo != null) && (client instanceof User)) {
            User user = (User) client;
            result = photo.getOwnerId().equals(user.getId());
        }
        return result;
    }

    /**
     * @methodtype get
     */
    public Object getSavedArg(String key) {
        Map<String, Object> savedArgs = (Map<String, Object>) httpSession.getAttribute(SAVED_ARGS);
        return savedArgs.get(key);
    }

    /**
     * @methodtype boolean query
     */
    public boolean isFormType(Map args, String type) {
        Object value = args.get(type);
        return (value != null) && !value.equals("");
    }

    /**
     * @methodtype set, get
     */
    public String getAndSaveAsString(Map args, String key) {
        String result = getAsString(args, key);
        setSavedArg(key, result);
        return result;
    }

    /**
     * @methodtype conversion
     */
    public String getAsString(Map args, String key) {
        String result;

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
     * @methodtype set
     */
    public void setSavedArg(String key, Object value) {
        Map<String, Object> savedArgs = getSavedArgs();
        savedArgs.put(key, value);
        httpSession.setAttribute(SAVED_ARGS, savedArgs);
    }

    /**
     * @methodtype get
     */
    public Map<String, Object> getSavedArgs() {
        return (Map<String, Object>) httpSession.getAttribute(SAVED_ARGS);
    }

    @Override
    protected void notifyNameChanged() {
        httpSession.setAttribute(Session.NAME, super.getName());
    }

    /**
     * @methodtype convert Returns some signifier of current user
     */
    @Override
    public String getClientId() {
        return (String) httpSession.getAttribute(CLIENT_ID);
    }

    @Override
    protected void notifyProcessingTimeChanged() {
        httpSession.setAttribute(Session.PROCESSING_TIME, super.getProcessingTime());
    }

}
