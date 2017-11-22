/*
 *  Copyright
 *
 *  Classname: Client
 *  Author: Tango1266
 *  Version: 08.11.17 22:26
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

package org.wahlzeit.model;

import com.google.appengine.api.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Parent;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Client uses the system. It is an abstract superclass. This package defines guest, user, moderator, and
 * administrator clients.
 */
@Entity
public abstract class Client implements Serializable, Persistent {

    private String httpSessionId;
    @Id
    protected String id;
    protected String nickName;
    @Parent
    protected Key parent = ObjectManager.applicationRootKey;
    /**
     *
     */
    protected EmailAddress emailAddress = EmailAddress.EMPTY;
    /**
     *
     */
    protected AccessRights accessRights = AccessRights.NONE;
    @Ignore
    protected int writeCount = 0;
    protected Language language = Language.ENGLISH;
    protected PhotoSize photoSize = PhotoSize.MEDIUM;
    protected List<PhotoId> praisedPhotoIds = new ArrayList<>();
    protected List<PhotoId> skippedPhotoIds = new ArrayList<>();
    public static final String ID = "id";
    public static final String NICK_NAME = "nickName";
    public static final String LANGUAGE = "language";

    /**
     *
     */
    Client() {
        // do nothing
    }

    /**
     * @methodtype get
     */
    public String getId() {
        return id;
    }

    /**
     * @methodtype get
     */
    public AccessRights getAccessRights() {
        return accessRights;
    }

    /**
     * @methodtype set
     */
    public void setAccessRights(AccessRights newRights) {
        accessRights = newRights;
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @methodtype set
     */
    public void setNickName(String nickName) throws IllegalArgumentException {
        UserManager.getInstance().changeNickname(this.nickName, nickName);
        this.nickName = nickName;
        incWriteCount();
    }

    /**
     * @methodtype boolean-query
     */
    public boolean hasGuestRights() {
        return hasRights(AccessRights.GUEST);
    }

    /**
     * @methodtype boolean-query
     */
    public boolean hasRights(AccessRights otherRights) {
        return AccessRights.hasRights(accessRights, otherRights);
    }

    /**
     *
     */
    public boolean hasUserRights() {
        return hasRights(AccessRights.USER);
    }

    /**
     * @methodtype boolean-query
     */
    public boolean hasModeratorRights() {
        return hasRights(AccessRights.MODERATOR);
    }

    /**
     * @methodtype boolean-query
     */
    public boolean hasAdministratorRights() {
        return hasRights(AccessRights.ADMINISTRATOR);
    }

    /**
     * @methodtype get
     */
    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    /**
     *
     */
    public void removeHttpSessionId() {
        httpSessionId = null;
        incWriteCount();
    }

    public String getHttpSessionId() {
        return httpSessionId;
    }

    /**
     *
     */
    public void setHttpSessionId(String httpSessionId) {
        this.httpSessionId = httpSessionId;
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @methodtype set
     */
    public void setLanguage(Language newLanguage) {
        language = newLanguage;
        incWriteCount();
        doSetLanguage(newLanguage);
    }

    /**
     * @methodtype get
     */
    public ModelConfig getLanguageConfiguration() {
        return LanguageConfigs.get(language);
    }

    /**
     * @methodtype get
     */
    public PhotoSize getPhotoSize() {
        return photoSize;
    }

    /**
     * @methodtype set
     */
    public void setPhotoSize(PhotoSize photoSize) {
        this.photoSize = photoSize;
    }

    /**
     * @methodtype get
     */
    public List<PhotoId> getPraisedPhotoIds() {
        return praisedPhotoIds;
    }

    /**
     * @methodtype set
     */
    public void setPraisedPhotoIds(List<PhotoId> praisedPhotoIds) {
        this.praisedPhotoIds = praisedPhotoIds;
    }

    /**
     * @methodtype set
     */
    public void addPraisedPhotoId(PhotoId ratedPhotoId) {
        praisedPhotoIds.add(ratedPhotoId);
        removeSkippedPhotoId(ratedPhotoId);
    }

    /**
     * @methodtype get
     */
    public Photo getLastPraisedPhoto() {
        int indexOfLastPraisedPhoto = praisedPhotoIds.size() - 1;
        Photo result = null;
        while (indexOfLastPraisedPhoto >= 0 && result == null) {
            PhotoId lastPraisedPhotoId = praisedPhotoIds.get(indexOfLastPraisedPhoto);
            result = PhotoManager.getInstance().getPhoto(lastPraisedPhotoId);
            if (!result.isVisible()) {
                result = null;
                indexOfLastPraisedPhoto--;
            }
        }
        return result;
    }

    /**
     * @methodtype get
     */
    public List<PhotoId> getSkippedPhotoIds() {
        return skippedPhotoIds;
    }

    /**
     * @methodtype get
     */
    public void setSkippedPhotoIds(List<PhotoId> skippedPhotoIds) {
        this.skippedPhotoIds = skippedPhotoIds;
    }

    /**
     * @methodtype set
     */
    public void removeSkippedPhotoId(PhotoId skippedPhotoIdToRemove) {
        skippedPhotoIds.remove(skippedPhotoIdToRemove);
    }

    /**
     * @methodtype set
     */
    public void addSkippedPhotoId(PhotoId skippedPhotoId) {
        if (!skippedPhotoIds.contains(skippedPhotoId)) {
            skippedPhotoIds.add(skippedPhotoId);
        }
    }

    /**
     * @methodtype initialization
     */
    protected void initialize(String id, String nickName, EmailAddress emailAddress, AccessRights accessRights,
                              Client previousClient) {
        this.id = id;
        this.nickName = nickName;
        this.accessRights = accessRights;
        this.emailAddress = emailAddress;

        // use some of the existing properties for the new user
        if (previousClient != null) {
            setLanguage(previousClient.getLanguage());
            setPraisedPhotoIds(previousClient.getPraisedPhotoIds());
            setPhotoSize(previousClient.getPhotoSize());
        }

        incWriteCount();

        UserManager.getInstance().addClient(this);
    }

    /**
     * @methodtype set
     * @methodpoperty hook
     */
    protected void doSetLanguage(Language newLanguage) {
    }

    /**
     *
     */
    @Override
    public boolean isDirty() {
        return writeCount != 0;
    }

    /**
     *
     */
    @Override
    public void incWriteCount() {
        writeCount++;
    }

    /**
     *
     */
    @Override
    public void resetWriteCount() {
        writeCount = 0;
    }
}
