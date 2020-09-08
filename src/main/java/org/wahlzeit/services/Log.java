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

import java.io.*;
import java.sql.*;
import java.text.*;

import org.apache.log4j.Logger;

/**
 * Simple logging class; should be replaced with log4j or the like.
 * 
 * @author dirkriehle
 *
 */
public class Log {
	
	/**
	 * 
	 */
	protected static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	/**
	 * 
	 */
	public static void logInfo(String l, String s) {
		StringBuffer sb = createLogEntry(l);
		addLogType(sb, "info");
		addField(sb, "info", s);
		log(sb);
	}
	
	/**
	 * 
	 */
	public static void logInfo(String level, String type, String value) {
		StringBuffer sb = createLogEntry(level);
		addLogType(sb, "info");
		addField(sb, type, value);
		log(sb);
	}
	
	/**
	 * 
	 */
	public static void logInfo(String level, String type, String value, String info) {
		StringBuffer sb = createLogEntry(level);
		addLogType(sb, "info");
		addField(sb, type, value);
		addField(sb, "info", info);
		log(sb);
	}
	
	/**
	 * 
	 */
	public static void logCreatedObject(String level, String type, String object) {
		StringBuffer sb = createLogEntry(level);
		addLogType(sb, "info");
		addField(sb, "created", type);
		addField(sb, "object", object);
		log(sb);
	}

	/**
	 * 
	 */
	public static void logError(String l, String s) {
		StringBuffer sb = createLogEntry(l);
		addLogType(sb, "error");
		addField(sb, "error", s);
		log(sb);
	}
	
	/**
	 * 
	 */
	protected static final StringBuffer createLogEntry(String level) {
		StringBuffer sb = new StringBuffer(256);
		String date = null;
		synchronized (dateFormatter) {
			date = dateFormatter.format(System.currentTimeMillis());
		}
		sb.append(date);
		addField(sb, "level", level);
		addSession(sb);
		return sb;
	}
	
	/**
	 * 
	 */
	public static final void addField(StringBuffer sb, String name, String value) {
		sb.append(", " + name + "=" + value);
	}
	
	/**
	 * 
	 */
	public static final void addSession(StringBuffer sb) {
		Session session = SessionManager.getThreadLocalSession();

		String id = (session != null) ? session.getName() : "no-session";
		addField(sb, "session", id);

		String dbc = (session != null) && session.hasDatabaseConnection() ? session.ensureDatabaseConnection().getName() : "no-database-connection";
		addField(sb, "databaseConnection", dbc);
		
		String threadId = String.valueOf(Thread.currentThread().getId());
		addField(sb, "threadId", threadId);
		
		String cn = (session != null) ? session.getClientName() : "no-client";
		addField(sb, "client", cn);
	}
	
	/**
	 * 
	 */
	public static final void addLogType(StringBuffer sb, String logType) {
		addField(sb, "logType", logType);
	}
	
	/**
	 *
	 */
	public static final void addThrowable(StringBuffer sb, Throwable t) {
		addField(sb, "throwable", t.toString());
	}
	
	/**
	 * 
	 */
	public static final void addStacktrace(StringBuffer sb, Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		addField(sb, "stacktrace", sw.toString());
	}

	/**
	 * 
	 */
	public static final void addQuery(StringBuffer sb, Statement q) {
		addField(sb, "query", q.toString());
	}

	/**
	 * 
	 */
	protected static Logger logger = Logger.getLogger(Log.class.getName());
	
	/**
	 * 
	 */
	public static final void log(StringBuffer sb) {
		 logger.info(sb.toString());
	}
	
}
