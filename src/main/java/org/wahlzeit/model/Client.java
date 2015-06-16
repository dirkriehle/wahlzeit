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

import com.google.appengine.api.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Parent;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;

import java.io.Serializable;

/**
 * A Client uses the system. It is an abstract superclass. This package defines guest, user, moderator, and
 * administrator clients.
 *
 * @author dirkriehle
 */
@Entity
public abstract class Client implements Serializable, Persistent {

    public static final String ID = "id";
    public static final String NICK_NAME = "nickName";

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

    private String httpSessionId;


    /**
     *
     */
    Client() {
        // do nothing
    }

    /**
     * @methodtype initialization
     */
    protected void initialize(String id, String nickName, EmailAddress emailAddress, AccessRights accessRights) {
        this.id = id;
        this.nickName = nickName;
        this.accessRights = accessRights;
        this.emailAddress = emailAddress;

        incWriteCount();

        UserManager.getInstance().addClient(this);
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
    public boolean isDirty() {
        return writeCount != 0;
    }

    /**
     *
     */
    public void incWriteCount() {
        writeCount++;
    }

    /**
     *
     */
    public void resetWriteCount() {
        writeCount = 0;
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

}
