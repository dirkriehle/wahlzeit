/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.services.*;

/**
 * Logging class for logging user-level messages.
 * User-level log entries are the result of user activities.
 * 
 * @author dirkriehle
 *
 */
public class UserLog extends Log {

	/**
	 * 
	 */
	public static void logUserInfo(String s) {
		Log.logInfo("ul", s);
	}
	
	/**
	 * 
	 */
	public static void logUserInfo(String type, String value) {
		Log.logInfo("ul", type, value);
	}
	
	/**
	 * 
	 */
	public static void logUserInfo(String type, String value, String info) {
		Log.logInfo("ul", type, value, info);
	}
	
	/**
	 * 
	 */
	public static void logUserError(String s) {
		Log.logError("ul", s);
	}
	
	/**
	 * 
	 */
	public static StringBuffer createUserLogEntry() {
		return Log.createLogEntry("ul");
	}

	/**
	 * 
	 */
	public static void logPerformedAction(String action) {
		log(createActionEntry(action));
	}
	
	/**
	 * @methodtype factory
	 */
	public static StringBuffer createActionEntry(String action) {
		StringBuffer sb = createUserLogEntry();
		addLogType(sb, "info");
		addField(sb, "action", action);
		return sb;
	}
	
	/**
	 * 
	 */
	public static void addCreatedObject(StringBuffer sb, String type, String object) {
		addField(sb, "created", type);
		addField(sb, "object", object);
	}
	
	/**
	 * 
	 */
	public static void addUpdatedObject(StringBuffer sb, String type, String object) {
		addField(sb, "updated", type);
		addField(sb, "object", object);
	}
	
	/**
	 * 
	 */
	public static void addDeletedObject(StringBuffer sb, String type, String object) {
		addField(sb, "deleted", type);
		addField(sb, "object", object);
	}
	
}
