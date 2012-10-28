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

import java.util.*;

import org.wahlzeit.utils.*;

/**
 * A Tags instance represents a set of tags; each tag ist just a string.
 * All tags are maintained lowercase and without whitespace.
 * I.e. "Captain America" turns into "captainamerica"
 * 
 * @author dirkriehle
 *
 */
public class Tags {
	
	/**
	 * 
	 */
	public static final char SEPARATOR_CHAR = ',';
	
	/**
	 * 
	 */
	public static final int MAX_NO_TAGS = 32;
	
	/**
	 * 
	 */
	public static final Tags EMPTY_TAGS = new Tags();
	
	/**
	 * 
	 */
	protected ArrayList<String> tags = new ArrayList<String>();
	
	/**
	 * 
	 */
	public Tags() {
		// do nothing
	}
	
	/**
	 * 
	 */
	public Tags(String myTags) {
		tags = getTagListFromString(myTags);
	}
	
	/**
	 * 
	 */
	public Tags(String myTags, char separator) {
		tags = getTagListFromString(myTags, separator);
	}
	
	/**
	 * 
	 */
	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}
	
	/**
	 * 
	 */
	public int getSize() {
		return tags.size();
	}
	
	/**
	 * 
	 */
	public boolean isEqual(Tags tag){
		return true;
		
	}
	
	/**
	 * 
	 */
	public String asString() {
		return asString(false, SEPARATOR_CHAR);
	}

	/**
	 * 
	 */
	public String asString(boolean lead, char sep) {
		StringBuffer result = new StringBuffer();
		String seps = (lead ? " " : "") + sep + " ";
		String[] myTags = asArray();
		for (int i = 0; i < myTags.length; i++) {
			if (i != 0 ) result.append(seps);
			result.append(myTags[i]);
		}
		return result.toString();
	}
	
	/**
	 * 
	 */
	public String[] asArray() {
		return (String[]) tags.toArray(new String[0]);
	}
	
	/**
	 * 
	 */
	public static ArrayList<String> getTagListFromString(String tags) {
		return getTagListFromString(tags, SEPARATOR_CHAR);
	}
	
	/**
	 * 
	 */
	public static ArrayList<String> getTagListFromString(String tags, char separator) {
		ArrayList<String> result = new ArrayList<String>(8);

		if (tags != null) {
			int i = 0;
			int j = 0;
			for(; i < tags.length(); i = j) {
				for (; ((i < tags.length()) && (tags.charAt(i) == separator)); ) {
					i++;
				}
				
				for (j = i; ((j < tags.length()) && (tags.charAt(j) != separator)); ) {
					j++;
				}
				
				if (i != j) {
					String tag = asTag(tags.substring(i, j));
					if (!result.contains(tag) && !StringUtil.isNullOrEmptyString(tag)) {
						result.add(tag);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	public static String asTag(String n) {
		StringBuffer result = new StringBuffer(n.length());

		for (int i = 0; i < n.length(); i++) {
			char c = n.charAt(i);
			if (Character.isLetter(c)) {
				result.append(Character.toLowerCase(c));
			} else if (Character.isDigit(c)) {
				result.append(c);
			}
		}
		
		return result.toString();
	}
	
}
