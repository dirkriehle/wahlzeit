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

import java.sql.*;

/**
 * Logging class for logging system-level messages.
 * System-level log entries are the result of system services and activities.
 * 
 * @author dirkriehle
 *
 */
public class SysLog extends Log {
	
	/**
	 * 
	 */
	public static void logSysInfo(String s) {
		Log.logInfo("sl", s);
	}
	
	/**
	 * 
	 */
	public static void logSysInfo(String type, String value) {
		Log.logInfo("sl", type, value);
	}
	
	/**
	 * 
	 */
	public static void logSysInfo(String type, String value, String info) {
		Log.logInfo("sl", type, value, info);
	}
	
	/**
	 * 
	 */
	public static void logCreatedObject(String type, String object) {
		Log.logCreatedObject("sl", type, object);
	}
	
	/**
	 * 
	 */
	public static void logSysError(String s) {
		Log.logError("sl", s);
	}
	
	/**
	 * 
	 */
	public static final void logQuery(Statement q) {
		StringBuffer sb = createSysLogEntry();
		addLogType(sb, "info");
		addQuery(sb, q);
		log(sb);
	}
	
	/**
	 * 
	 */
	public static final void logQuery(String s) {
		StringBuffer sb = createSysLogEntry();
		addLogType(sb, "info");
		addField(sb, "query", s);
		log(sb);
	}
	
	/**
	 * 
	 */
	public static final void logThrowable(Throwable t) {
		Throwable cause = t.getCause();
		if (cause != null) {
			logThrowable(cause);
		}
		
		StringBuffer sb = createSysLogEntry();
		addLogType(sb, "exception");
		addThrowable(sb, t);
		addStacktrace(sb, t);
		log(sb);
	}

	/**
	 * 
	 */
	protected static StringBuffer createSysLogEntry() {
		return createLogEntry("sl");
	}

}
