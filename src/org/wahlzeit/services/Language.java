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

import org.wahlzeit.utils.*;

/**
 * 
 * @author dirkriehle
 * 
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
