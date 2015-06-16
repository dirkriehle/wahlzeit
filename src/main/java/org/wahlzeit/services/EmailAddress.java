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

package org.wahlzeit.services;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An email address provides a simple email address representation.
 * It is a value object and implemented as immutable.
 *
 * @author dirkriehle
 */
public class EmailAddress implements Serializable {

    /**
     *
     */
    protected static final Map<String, EmailAddress> instances = new HashMap<String, EmailAddress>();

    /**
     *
     */
    public static final EmailAddress EMPTY = doGetFromString(""); // after map initialization...
    /**
     *
     */
    protected String value;

    private EmailAddress() {
        // for Objectify to load
    }

    /**
     *
     */
    protected EmailAddress(String myAddress) {
        value = myAddress;
    }

    /**
     *
     */
    public static EmailAddress getFromString(String myValue) {
        return doGetFromString(myValue);
    }

    /**
     *
     */
    protected static EmailAddress doGetFromString(String myValue) {
        EmailAddress result = instances.get(myValue);
        if (result == null) {
            synchronized (instances) {
                result = instances.get(myValue);
                if (result == null) {
                    result = new EmailAddress(myValue);
                    instances.put(myValue, result);
                }
            }
        }

        return result;
    }

    /**
     *
     */
    public String asString() {
        return value;
    }

    /**
     *
     */
    public InternetAddress asInternetAddress() {
        InternetAddress result = null;

        try {
            result = new InternetAddress(value);
        } catch (AddressException ex) {
            // should not happen
        }

        return result;
    }

    /**
     * @methodtype boolean-query
     */
    public boolean isEqual(EmailAddress emailAddress) {
        return this == emailAddress;
    }

    /**
     *
     */

    public boolean isValid() {
        return !isEmpty();
    }

    /**
     *
     */
    public boolean isEmpty() {
        return this == EMPTY;
    }

}
