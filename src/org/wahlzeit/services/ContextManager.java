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
 * A manager for Context objects (user (web) sessions, agent threads, etc.)
 * Clients can look up the context by thread.
 * 
 * @author driehle
 *
 */
public class ContextManager {
	
	/**
	 * 
	 */
	protected static ThreadLocal<Session> contexts = new ThreadLocal<Session>();
	
	/**
	 * 
	 */
	public static Session getThreadLocalContext() {
		return contexts.get();
	}
	
	/**
	 * 
	 */
	public static void setThreadLocalContext(Session ctx) {
		contexts.set(ctx);
	}
	
	/**
	 * 
	 */
	public static void dropThreadLocalContext() {
		setThreadLocalContext(null);
	}

	/**
	 * 
	 */
	public static DatabaseConnection getDatabaseConnection() {
		return contexts.get().getDatabaseConnection();
	}
	
}
