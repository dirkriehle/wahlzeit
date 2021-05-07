/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.util.*;
import javax.mail.internet.*;

import org.wahlzeit.utils.*;

/**
 * An email address provides a simple email address representation.
 * It is a value object and implemented as immutable.
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
