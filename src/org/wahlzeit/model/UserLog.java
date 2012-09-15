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
	public static StringBuffer createUserLogEntry() {
		return createLogEntry("ul");
	}

	/**
	 * 
	 */
	public static void logInfo(String s) {
		logInfo("ul", s);
	}
	
	/**
	 * 
	 */
	public static void logError(String s) {
		logError("ul", s);
	}
	
	/**
	 * 
	 */
	public static void logValue(String type, String value) {
		logValue("ul", type, value);
	}
	
	/**
	 * 
	 */
	public static void logValueWithInfo(String type, String value, String info) {
		logValueWithInfo("ul", type, value, info);
	}
	
	/**
	 * 
	 */
	public static void logPerformedAction(String action) {
		log(createActionEntry(action));
	}
	
	/**
	 * 
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
