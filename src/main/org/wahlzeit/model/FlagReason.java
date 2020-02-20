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
 * A flag reason denotes why a photo was flagged for a photo case.
 * 
 * @author dirkriehle
 * 
 */
public enum FlagReason implements EnumValue {
	
	/**
	 * 
	 */
	MISMATCH(0), OFFENSIVE(1), COPYRIGHT(2), OTHER(3);
	
	protected static final int MAX_VALUE = 3;
	
	/**
	 * 
	 */
	private static FlagReason[] allValues = {
		MISMATCH, OFFENSIVE, COPYRIGHT, OTHER
	};
	
	/**
	 * 
	 */
	public static FlagReason getFromInt(int myValue) throws IllegalArgumentException {
		assertIsValidFlagReasonAsInt(myValue);

		return allValues[myValue];
	}
	
	/**
	 * 
	 */
	private static final String[] valueNames = {
		"mismatch", "offensive", "copyright", "other"
	};
	
	/**
	 * 
	 */
	public static FlagReason getFromString(String reason) throws IllegalArgumentException {
		int value = findFlagReasonIntFromString(reason);
		return getFromInt(value);
	}
	
	/**
	 * Used to index arrays
	 */
	private int value;
	
	/**
	 * 
	 */
	private FlagReason(int myValue) {
		assertIsValidFlagReasonAsInt(myValue);
		
		value = myValue;
		
		assertIsValidFlagReasonAsInt(value);
	}
	
	/**
	 * 
	 * @methodtype conversion
	 */
	public int asInt() {
		assertIsValidFlagReasonAsInt(value);
		
		return value;
	}
	
	/**
	 *
	 * @methodtype conversion
	 */
	public String asString() {
		String name = valueNames[value];
		
		assertIsValidFlagReasonAsString(name);
		
		return name;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public FlagReason[] getAllValues() {
		return allValues;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getTypeName() {
		return "FlagReason";
	}		

	/**
	 * 
	 */
	private static void assertIsValidFlagReasonAsInt(int myValue) {
		if ((myValue < 0) || (myValue > MAX_VALUE)) {
			throw new IllegalArgumentException("invalid FlagReason int: " + myValue);
		}
	}	
	
	/**
	 * 
	 */
	private static void assertIsValidFlagReasonAsString(String reason)	{
		if(findFlagReasonIntFromString(reason) == -1)	{
			throw new IllegalArgumentException("'" + reason + "' is not a valid reason!");
		}
	}
	
	/**
	 * 
	 */
	private static int findFlagReasonIntFromString(String reason) {
		int value = 0;
		
		for (String name : valueNames) {
			if (name.equals(reason))	{
				break;
			} else {
				value++;
			}
		}
		
		if (value > MAX_VALUE) {
			value = -1;
		}
		
		return value;
	}
	
	/**
	 * 
	 */	
	private static void assertIsValidFlagReason(FlagReason reason)	{
		if (reason == null)	{
			throw new IllegalArgumentException("invalid reason");
		}
	}
	
}
