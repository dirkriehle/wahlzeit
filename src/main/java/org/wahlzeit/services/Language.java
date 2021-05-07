/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import org.wahlzeit.utils.*;

/**
 * A value object of capturing the available languages.
 */
public enum Language implements EnumValue {

	/**
	 * 
	 */
	ENGLISH(0),	SPANISH(1), GERMAN(2), JAPANESE(3);
	
	/**
	 * 
	 */
	private static Language[] allValues = {
		ENGLISH, SPANISH, GERMAN, JAPANESE
	};
	
	/**
	 * 
	 */
	public static Language getFromInt(int myValue) throws IllegalArgumentException {
		if ((myValue >= 0) && (myValue <= 3)) {
			return allValues[myValue];
		}

		throw new IllegalArgumentException("invalid Language int: " + myValue);
	}
	
	/**
	 * 
	 */
	private static final String[] isoCodes = {
		"en", "es", "de", "ja"
	};
	
	/**
	 * 
	 */
	public static Language getFromIsoCode(String isoCode) throws IllegalArgumentException {
		for (Language language : Language.values()) {
			if (isoCodes[language.asInt()].equals(isoCode)) {
				return language;
			}
		}
		
		throw new IllegalArgumentException("invalid Language ISO code: " + isoCode);
	}
	
	/**
	 * 
	 */
	private static final String[] valueNames = {
		"english", "spanish", "german", "japanese"
	};
	
	/**
	 * @methodtype conversion
	 */
	public static Language getFromString(String myLanguage) throws IllegalArgumentException {
		for (Language language: Language.values()) {
			if (valueNames[language.asInt()].equals(myLanguage)) {
				return language;
			}
		}
		
		throw new IllegalArgumentException("invalid Language string: " + myLanguage);
	}
	
	/**
	 * 
	 */
	private int value;
	
	/**
	 * 
	 */
	private Language(int myValue) {
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
	public String asIsoCode() {
		return isoCodes[value];
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
	public Language[] getAllValues() {
		return allValues;
	}

	/**
	 * 
	 */
	public String getTypeName() {
		return "Language";
	}
	
}
