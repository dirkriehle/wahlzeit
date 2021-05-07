/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.utils;

import java.io.File;

/**
 * A set of utility functions for HTML formatting.
 */
public class HtmlUtil {
	
	/**
	 * 
	 */
	public static final String RADIO_BUTTON_CHECK = "CHECKED";
	public static final String CHECKBOX_CHECK = "CHECKED";
	public static final String SELECT_SELECTED = "SELECTED";
	
	/**
	 * 
	 */
	public static String asPath(String dirName) {
		return dirName.replace(File.separatorChar, '/');
	}
	
	/**
	 * 
	 */
	public static String asBold(String html) {
		return "<b>" + html + "</b>";
	}
	
	/**
	 * 
	 */
	public static String asRadioButtonCheck(boolean checked) {
		return checked ? RADIO_BUTTON_CHECK : "";
	}

	/**
	 * 
	 */
	public static String asCheckboxCheck(boolean checked) {
		return checked ? CHECKBOX_CHECK : "";
	}

	/**
	 * 
	 */
	public static String asSelectSelected(boolean selected) {
		return selected ? SELECT_SELECTED : "";
	}
	
	/**
	 * 
	 */
	public static String asHref(String link) {
		return asHref(link, link);
	}
		
	/**
	 * 
	 */
	public static String asHref(String link, String body) {
		return "<a href=\"" + link + "\" rel=\"nofollow\">" + body + "</a>";
	}
	
	/**
	 * 
	 */
	public static String asImg(String link) {
		return "<img src=\"" + link + "\" />";
	}
	
	/**
	 * 
	 */
	public static String asImg(String link, int width, int height) {
		return "<img src=\"" + link + "\" width=\"" + width + "\" height=\"" + height + "\" />";
	}

	/**
	 * 
	 */
	public static String asP(String value) {
		return "<p>" + value + "</p>";
	}
	
	/**
	 * 
	 */
	public static String maskForWeb(String s) {
		StringBuffer result = new StringBuffer(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '&') {
				result.append("&amp;");
			} else if (c == '"') {
				result.append("&quot;");
			} else if (c == '<') {
				result.append("&lt;");
			} else if (c == '>') {
				result.append("&gt;");
			} else {
				result.append(c);
			}
		}
		
		return result.toString();
	}
	
}
