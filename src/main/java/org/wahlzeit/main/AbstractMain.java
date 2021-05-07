/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.main;

import java.util.Optional;

import org.wahlzeit.services.*;

/**
 * A superclass for a Main class for system startup and shutdown.
 */
public abstract class AbstractMain {
	
	private static final String DB_HOST = Optional.ofNullable(System.getenv("WAHLZEIT_DB_HOST")).orElse("localhost");
	
	/**
	 * 
	 */
	protected SysSession mainSession = null;
	
	/**
	 * 
	 */
	protected AbstractMain() {
		// do nothing
	}
	
	/**
	 * 
	 */
	protected void startUp(String rootDir) throws Exception {
		SysConfig.setInstance(new SysConfig(rootDir, DB_HOST));
		
		boolean dbAvailable = DatabaseConnection.waitForDatabaseIsReady(30, 1000);
		if (!dbAvailable) {
			throw new RuntimeException("Unable to proceed with wahlzeit app. DB connection could not be established.");
		}		

		mainSession = new SysSession("system");
		SessionManager.setThreadLocalSession(mainSession);
	}
	
	/**
	 * 
	 */
	protected void shutDown() throws Exception {
		SysConfig.dropInstance();
	}
	
}
