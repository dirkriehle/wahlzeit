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

/**
 * A manager for Session objects (user (web) sessions, agent threads, etc.)
 * Clients can look up the session by thread.
 * 
 * @author dirkriehle
 *
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
