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

import java.util.*;
import javax.mail.internet.*;

import org.wahlzeit.utils.*;

/**
 * An email address provides a simple email address representation.
 * It is a value object and implemented as immutable.
 * 
 * @author dirkriehle
 *
 */
public class EmailAddress {
	
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
	public static EmailAddress getFromString(String myValue) {
		assertIsRelaxedValidString(myValue);
		return doGetFromString(myValue);
	}	
	
	/**
	 * 
	 */
	protected static EmailAddress doGetFromString(String myValue) {
		EmailAddress result = instances.get(myValue);
		if (result == null) {
			synchronized(instances) {
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
	protected static void assertIsRelaxedValidString(String address) throws IllegalArgumentException {
		if (StringUtil.isNullOrEmptyString(address) || !StringUtil.isValidLocalEmailAddress(address)) {
			throw new IllegalArgumentException(address + " is not a relaxed valid email address");
		}
	}
		
	/**
	 * 
	 */
	protected String value;
		
	/**
	 * 
	 */
	protected EmailAddress(String myAddress) {
		value = myAddress;
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
	public boolean isEmpty() {
		return this == EMPTY;
	}
	
	/**
	 * 
	 */

	public boolean isValid() {
		return !isEmpty();
	}

}
