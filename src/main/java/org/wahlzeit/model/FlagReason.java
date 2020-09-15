/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle https://dirkriehle.com
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.utils.*;

/**
 * A flag reason denotes why a photo was flagged for a photo case.
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
