/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

/**
 * A manager for Session objects (user (web) sessions, agent threads, etc.) Clients can look up the session by thread.
 */
public class SessionManager {
	
	/**
	 * 
	 */
	protected static ThreadLocal<Session> sessions = new ThreadLocal<Session>();
	
	/**
	 * 
	 */
	public static Session getThreadLocalSession() {
		return sessions.get();
	}
	
	/**
	 * 
	 */
	public static void setThreadLocalSession(Session ctx) {
		sessions.set(ctx);
	}
	
	/**
	 * 
	 */
	public static void dropThreadLocalSession() {
		setThreadLocalSession(null);
	}

	/**
	 * 
	 */
	public static DatabaseConnection getDatabaseConnection() {
		return getThreadLocalSession().ensureDatabaseConnection();
	}
	
}
