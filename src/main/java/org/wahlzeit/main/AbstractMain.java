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
