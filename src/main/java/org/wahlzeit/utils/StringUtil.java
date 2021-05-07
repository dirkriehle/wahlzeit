/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.utils;

import java.io.File;
import java.net.*;
import java.util.regex.*;

/**
 * A set of utility functions for basic string manipulations.
 */
public class StringUtil {
	
	/**
	 * 
	 */
	public final static boolean isLegalUserName(String s) {
		return isSafeString(s) && !s.equals("");
	}
	
	/**
	 * 
	 */
	public final static boolean isLegalPassword(String s) {
		return isSafeString(s) && !s.equals("");
	}
	
	/**
	 * 
	 */
	public final static boolean isValidStrictEmailAddress(String s) {
		int a = s.indexOf('@');
		int d = s.lastIndexOf('.');
		return isSafeIncluding(s, "_-+@.") && (a > 0) && (a < (d - 2)) && (d < (s.length() - 2));
	}
	
	/**
	 * 
	 */
	public final static boolean isValidLocalEmailAddress(String s) {
		int a = s.indexOf('@');
		int d = s.lastIndexOf('@');
		return isSafeIncluding(s, "_-+@.") && (a > 0) && (a == d);
	}
	
	/**
	 * 
	 */
	public final static boolean isLegalCharacterName(String s) {
		return isSafeString(s);
	}
	
	/**
	 * 
	 */
	public final static boolean isLegalSeriesName(String s) {
		return isSafeString(s);
	}
	
	/**
	 * 
	 */
	public final static boolean isLegalTagsString(String s) {
		return isSafeString(s);
	}
	
	/**
	 * 
	 */
	public final static boolean isValidURL(String s) {
		try {
			return new URL(s) != null;
		} catch (MalformedURLException ex) {
			return false;
		}
	}
	
	/**
	 * 
	 */
	public final static boolean isNullOrEmptyString(Object s) {
		return (s == null) || s.equals("");
	}
	
	/**
	 * 
	 */
	public final static boolean isSafeString(String s) {
		return isSafeWebString(s); // & isSafeQueryArg(s);
	}
		
	/**
	 * 
	 */
	public final static boolean isSafeWebString(String s) {
		return isSafeExcluding(s, "`'&<>;\"\\");
	}
	
	/**
	 * 
	 */
	public final static boolean isSafeQueryArg(String s) {
		return isSafeWebString(s);
	}
	
	/**
	 * @methodtype boolean-query
	 */
	protected final static boolean isSafeIncluding(String s, String l) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!Character.isLetterOrDigit(c) && (l.indexOf(c) == -1)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	protected final static boolean isSafeExcluding(String s, String l) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!Character.isLetterOrDigit(c) && (l.indexOf(c) != -1)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	public final static String maskChar(String s, char c) {
		StringBuffer result = new StringBuffer(s.length() + 4);
		for (int i = 0; i < s.length(); i++) {
			char v = s.charAt(i);
			if (v == c) {
				result.append('\\');
			}
			result.append(v);
		}
		
		return result.toString();
	}

	/**
	 * 
	 */
	public static String asFourDigits(long id) {
		if (id < 10) {
			return "000" + id;
		} else if (id < 100) {
			return "00" + id;
		} else if (id < 1000) {
			return "0" + id;
		} else {
			return "" + id;
		}
	}

	/**
	 * 
	 */
	public static String asThreeDigits(long id) {
		if (id < 10) {
			return "00" + id;
		} else if (id < 100) {
			return "0" + id;
		} else {
			return "" + id;
		}
	}

	/**
	 * 
	 */
	public final static String asStringInSeconds(long duration) {
		long seconds = duration / 1000;
		long milliSeconds = duration - (seconds * 1000);
		return String.valueOf(seconds) + "." + asThreeDigits(milliSeconds);
	}
	
	/**
	 * The string, which separates path segments in a URL
	 */
	private static final String URL_SEPARATOR = "/";

	/**
	 * Convert separators in a filesystem path to URL separators.
	 * It does not escape the URL characters.
	 * Use java.net.URLEncoder for this.
	 * 
	 * @param path
	 * @return
	 * 
	 * @fixme Review for performance
	 */
	public final static String pathAsUrlString(String path) {
		if(!File.separator.equals(URL_SEPARATOR)) {
			// We are not on a platform where file separator matches the url separator,
			// we need to convert between them.
			path = path.replaceAll(Pattern.quote(File.separator), Matcher.quoteReplacement(URL_SEPARATOR));
		}
		return path;
	}

	/**
	 * 
	 */
	public final static URL asUrl(String s) throws IllegalArgumentException {
		try {
			return new URL(s);
		} catch (MalformedURLException ex) {
			throw new IllegalArgumentException("invalid URL string");
		}
	}
	
	/**
	 * 
	 */
	public final static URL asUrlOrDefault(String s, URL defval) {
		try {
			return new URL(s);
		} catch (MalformedURLException ex) {
			return defval;
		}
	}
	
}
