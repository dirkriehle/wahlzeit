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

package org.wahlzeit.utils;

import java.io.File;

/**
 * A set of utility functions for HTML formatting.
 * 
 * @author dirkriehle
 *
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
