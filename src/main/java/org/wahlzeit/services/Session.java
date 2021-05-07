/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;


/**
 * A Session object maintains a DatabaseConnection and helps track processing time. Typically, there is one for each
 * working thread, be it a system thread or a web session.
 */
public class Session {

	/**
	 * Session state
	 */
	protected String name = null;

	/**
	 * Database stuff
	 */
	protected DatabaseConnection databaseConnection = null;
	
	/**
	 * processing time for requests
	 */
	protected long processingTime = 0;
	
	/**
	 * 
	 */
	protected Session() {
		// do nothing
	}
	
	/**
	 * 
	 */
	protected void initialize(String myName) {
		name = myName;
	}
	
	/**
	 * @methodproperty hook
	 *
	 * Hook method for subclasses to get to know when name changes.
	 * @see #initialize(String)
	 */
	protected void finalize() throws Throwable {
		try {
			returnDatabaseConnection();
		} finally {
			super.finalize();
		}
	}
	
	/**
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 */
	public boolean hasDatabaseConnection() {
		return databaseConnection != null;
	}
	
	/**
	 * 
	 */
	public DatabaseConnection ensureDatabaseConnection() {
		if (databaseConnection == null) {
			try {
				databaseConnection = DatabaseConnection.ensureDatabaseConnection();
			} catch (Throwable t) {
				SysLog.logThrowable(t);
			}
		}
		
		return databaseConnection;
	}
	
	/**
	 * @methodproperty hook
	 *
	 * Hook method for subclasses to get to know when processing time changes.
	 * @see #addProcessingTime(long)
	 * @see #resetProcessingTime()
	 */
	public void returnDatabaseConnection() {
		if (databaseConnection != null) {
			DatabaseConnection.returnDatabaseConnection(databaseConnection);
			databaseConnection = null;
		}
	}
	
	/**
	 * 
	 */
	public String getClientName() {
		return "system";
	}
	
	/**
	 * 
	 */
	public void resetProcessingTime() {
		processingTime = 0;
	}
	
	/**
	 * 
	 */
	public void addProcessingTime(long time) {
		processingTime += time;
	}
	
	/**
	 * 
	 */
	public long getProcessingTime() {
		return processingTime;
	}
	
}
