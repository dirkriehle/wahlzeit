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

import com.googlecode.objectify.annotation.Subclass;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.services.LogBuilder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A User is a client that is logged-in, that is, has registered with the system. A user has a fair amount of
 * information associated with it, most notably his/her photos. Also, his/her contact information and whether the
 * account has been confirmed. Users can have a home page which may be elsewhere on the net.
 *
 * @author dirkriehle
 */
@Subclass(index = true)
public class User extends Client {

    /**
     *
     */
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String TERMS = "termsAndConditions";

    /**
     *
     */
    public static final String STATUS = "status";
    public static final String RIGHTS = "accessRights";
    public static final String GENDER = "gender";
    public static final String LANGUAGE = "language";
    public static final String NOTIFY_ABOUT_PRAISE = "notifyAboutPraise";
    public static final String MEMBER_SINCE = "memberSince";
    public static final String NO_PHOTOS = "noPhotos";

    private static final Logger log = Logger.getLogger(User.class.getName());

    /**
     *
     */
    protected Language language = Language.ENGLISH;
    protected boolean notifyAboutPraise = true;
    protected Gender gender = Gender.UNDEFINED;
    protected UserStatus status = UserStatus.CREATED;

    /**
     *
     */
    protected Photo userPhoto = null;
    protected Set<Photo> photos = new HashSet<Photo>();
    /**
     *
     */
    protected long creationTime = System.currentTimeMillis();


    /**
     *
     */
    public User(String id, String myName, String myEmailAddress) {
        this(id, myName, EmailAddress.getFromString(myEmailAddress));
    }

    /**
     *
     */
    public User(String id, String nickname, EmailAddress emailAddress) {
        initialize(id, nickname, emailAddress, AccessRights.USER);
    }

    /**
     * @methodtype initialization
     */
    protected void initialize(String id, String nickName, EmailAddress emailAddress, AccessRights accessRights) {
        super.initialize(id, nickName, emailAddress, accessRights);

        log.config(LogBuilder.createSystemMessage().
                addAction("initialize user").
                addParameter("id", id).
                addParameter("name", nickName).
                addParameter("E-Mail", emailAddress.asString()).toString());
        incWriteCount();
    }

    /**
     *
     */
    protected User() {
        // do nothing
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

        for (Iterator<Photo> i = photos.iterator(); i.hasNext(); ) {
            Photo photo = i.next();
            photo.setOwnerLanguage(language);
        }
    }

    /**
     * @methodtype boolean query
     */
    public boolean getNotifyAboutPraise() {
        return notifyAboutPraise;
    }

    /**
     * @methodtype set
     */
    public void setNotifyAboutPraise(boolean notify) {
        notifyAboutPraise = notify;

        for (Iterator<Photo> i = photos.iterator(); i.hasNext(); ) {
            Photo photo = i.next();
            photo.setOwnerNotifyAboutPraise(notifyAboutPraise);
        }

        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @methodtype set
     */
    public void setGender(Gender newGender) {
        gender = newGender;
        incWriteCount();
    }

    /**
     * @methodtype boolean query
     */
    public boolean isConfirmed() {
        return getStatus().isConfirmed();
    }

    /**
     * @methodtype get
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * @methodtype set
     */
    public void setStatus(UserStatus newStatus) {
        status = newStatus;
        incWriteCount();
    }

    /**
     * @methodtype set
     */
    public void setConfirmed() {
        setStatus(status.asConfirmed());
        incWriteCount();
    }

    /**
     * @methodtype boolean query
     */
    public boolean hasUserPhoto() {
        return userPhoto != null;
    }

    /**
     * @methodtype get
     */
    public Photo getUserPhoto() {
        return userPhoto;
    }

    /**
     * @methodtype set
     */
    public void setUserPhoto(Photo newPhoto) {
        userPhoto = newPhoto;
        log.info("SetUserPhoto: " + newPhoto.getIdAsString());
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * @methodtype set
     */
    public void addPhoto(Photo newPhoto) {
        photos.add(newPhoto);

        newPhoto.setOwnerId(id);
        newPhoto.setOwnerNotifyAboutPraise(notifyAboutPraise);
        newPhoto.setOwnerEmailAddress(emailAddress);
        newPhoto.setOwnerLanguage(language);

        incWriteCount();
    }

    /**
     * @methodtype set
     */
    public void removePhoto(Photo notMyPhoto) {
        photos.remove(notMyPhoto);
        incWriteCount();
    }

    /**
     * @methodtype get
     */
    public int getNoOfPhotos() {
        return photos.size();
    }

    /**
     * @methodtype get
     */
    public Photo[] getPhotos() {
        return getPhotosReverseOrderedByPraise();
    }

    /**
     * @methodtype conversion
     */
    public Photo[] getPhotosReverseOrderedByPraise() {
        Photo[] result = photos.toArray(new Photo[0]);
        Arrays.sort(result, getPhotoByPraiseReverseComparator());
        return result;
    }

    /**
     * @methodtype get
     */
    public static Comparator<Photo> getPhotoByPraiseReverseComparator() {
        return new Comparator<Photo>() {
            public int compare(Photo p1, Photo p2) {
                double sc1 = p1.getPraise();
                double sc2 = p2.getPraise();
                if (sc1 == sc2) {
                    String id1 = String.valueOf(p1.getId());
                    String id2 = String.valueOf(p2.getId());
                    return id1.compareTo(id2);
                } else if (sc1 < sc2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
    }

}
