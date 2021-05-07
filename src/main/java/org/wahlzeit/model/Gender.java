/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.utils.*;

/**
 * The gender denotes some user's/person's/character's/whatever gender. The undefined value denotes that no value was
 * provided or the entity is not human.
 */
public enum Gender implements EnumValue {
	
	/**
	 * UNDEFINED = user never entered anything
	 */
	UNDEFINED(0), MALE(1), FEMALE(2), OTHER(3);
	
	/**
	 * 
	 */
	private static Gender[] allValues = {
		UNDEFINED, MALE, FEMALE, OTHER
	};
	
	/**
	 * @methodtype conversion
	 */
	public static Gender getFromInt(int myValue) throws IllegalArgumentException {
		assertIsValidGenderAsInt(myValue);
		return allValues[myValue];
	}

	/**
	 * 
	 */
	protected static void assertIsValidGenderAsInt(int myValue) throws IllegalArgumentException {
		if ((myValue < 0) || (myValue > 3)) {
			throw new IllegalArgumentException("invalid Gender int: " + myValue);
		}
	}
	
	/**
	 * 
	 */
	private static final String[] valueNames = {
		"undefined", "male", "female", "other"
	};
	
	/**
	 * @methodtype conversion
	 */
	public static Gender getFromString(String myGender) throws IllegalArgumentException {
		for (Gender gender : Gender.values()) {
			if (valueNames[gender.asInt()].equals(myGender)) {
				return gender;
			}
		}
		
		throw new IllegalArgumentException("invalid Gender string: " + myGender);
	}
	
	/**
	 * 
	 */
	private int value;
	
	/**
	 * 
	 */
	private Gender(int myValue) {
		value = myValue;
	}
			
	/**
	 * 
	 */
	public int asInt() {
		return value;
	}
	
	/**
	 * 
	 */
	public String asString() {
		return valueNames[value];
	}
	
	/**
	 * 
	 */
	public Gender[] getAllValues() {
		return allValues;
	}
	
	/**
	 * 
	 */
	public String getTypeName() {
		return "Gender";
	}
		
	/**
	 * 
	 */
	public boolean isUndefined() {
		return (this == UNDEFINED);
	}
	
	/**
	 * 
	 */
	public boolean isMale() {
		return (this == MALE);
	}
	
	/**
	 * 
	 */
	public boolean isFemale() {
		return (this == FEMALE);
	}
	
	/**
	 * 
	 */
	public boolean isOther() {
		return (this == OTHER);
	}

}
