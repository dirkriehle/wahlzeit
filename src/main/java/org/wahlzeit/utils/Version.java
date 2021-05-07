/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.utils;

public class Version {
	
	/**
	 * 
	 */
	public static final String CODE_VERSION = "3.0";
	
	/**
	 * 
	 */
	public static final String DATA_VERSION = "2.0";
	
	/**
	 * 
	 */
	public static int getCodeVersionAsInt() {
		return getVersionAsInt(CODE_VERSION);
	}
	
	/**
	 * 
	 */
	public static int getDataVersionAsInt() {
		return getVersionAsInt(DATA_VERSION);
	}
	
	/**
	 * 
	 */
	public static int getVersionAsInt(String version) {
		int majorNumber = getMajorNumberAsInt(version); 
		int minorNumber = getMinorNumberAsInt(version);
		int revisionNumber = getRevisionNumberAsInt(version);
		return majorNumber * 1000000 + minorNumber * 1000 + revisionNumber;
	}
	
	/**
	 * 
	 */
	public static int getMajorNumberAsInt(String version) {
		int endIndex = version.indexOf('.');
		String number = version.substring(0, endIndex);
		return Integer.valueOf(number);
	}

	/**
	 * 
	 */
	public static int getMinorNumberAsInt(String version) {
		int startIndex = version.indexOf('.') + 1;
		int endIndex = version.indexOf('.', startIndex);
		String number = version.substring(startIndex, endIndex);
		return Integer.valueOf(number);
	}

	/**
	 * 
	 */
	public static int getRevisionNumberAsInt(String version) {
		int startIndex = version.indexOf('.') + 1;
		startIndex = version.indexOf('.', startIndex) + 1;
		String number = version.substring(startIndex);
		return Integer.valueOf(number);
	}

}
