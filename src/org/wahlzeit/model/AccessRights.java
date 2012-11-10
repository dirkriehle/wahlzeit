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

import org.wahlzeit.utils.*;

/**
 * An AccessRight is an enum value that represents a defined level of access.
 * Possible access levels are none, guest, user, moderator, and administrator.
 * However, higher levels of access subsume lower levels of access rights.
 * How they are handled depends on the application.
 * 
 * @author dirkriehle
 *
 */
public enum AccessRights implements EnumValue {

	/**
	 * 
	 */
	NONE(0), GUEST(1), USER(2), MODERATOR(3), ADMINISTRATOR(4);
	
	/**
	 * 
	 */
	private static AccessRights[] allValues = {
		NONE, GUEST, USER, MODERATOR, ADMINISTRATOR
	};
	
	/**
	 * 
	 */
	public static AccessRights getFromInt(int myValue) throws IllegalArgumentException {
		assertIsValidIntValue(myValue);
		return allValues[myValue];
	}

	/**
	 * 
	 */
	private static final String[] valueNames = {
		"none", "guest", "user", "moderator", "administrator"
	};
	
	/**
	 * 
	 */
	public static AccessRights getFromString(String myRights) throws IllegalArgumentException {
		for (AccessRights rights : AccessRights.values()) {
			if (valueNames[rights.asInt()].equals(myRights)) {
				return rights;
			}
		}
		
		throw new IllegalArgumentException("invalid AccessRight string: " + myRights);
	}
	
	/**
	 * 
	 */
	private int value;
	
	/**
	 * 
	 */
	private AccessRights(int myValue) {
		value = myValue;
	}
	
	/**
	 * 
	 * @methodtype conversion
	 */
	public int asInt() {
		return value;
	}

	/**
	 * 
	 * @methodtype conversion
	 */
	public String asString() {
		return valueNames[value];
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public AccessRights[] getAllValues() {
		return allValues;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getTypeName() {
		return "AccessRights";
	}
	
	/**
	 * @methodtype comparison
	 */
	public static boolean hasRights(AccessRights a, AccessRights b) {
		return a.value >= b.value;
	}
	
	/**
	 * 
	 * @methodtype assertion
	 */
	private static void assertIsValidIntValue(int myValue) {
		if ((myValue < 0) || (myValue > 4)) {
			throw new IllegalArgumentException("invalid AccessRights int: " + myValue);
		}
	}
		
}
