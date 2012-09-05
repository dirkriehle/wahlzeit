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

/**
 * An email address provides a simple email address representation.
 * It is a value object and implemented as immutable.
 * 
 * @author driehle
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
	public static final EmailAddress NONE = getFromString(""); // after map initialization...
	
	/**
	 * 
	 */
	public static EmailAddress getFromString(String myValue) {
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
	public boolean isEqual(EmailAddress emailAddress) {
		return this == emailAddress;
	}
		
}
