/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
