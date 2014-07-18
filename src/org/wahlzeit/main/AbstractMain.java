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

import javax.servlet.http.*;

import org.wahlzeit.services.*;

/**
 * 
 * @author dirkriehle
 *
 */
public abstract class AbstractMain {
	
	/**
	 * 
	 */
	protected static AbstractMain instance;
	
	/**
	 * 
	 */
	protected SysSession mainSession = null;
	
	/**
	 * 
	 */
	protected boolean isToStop = false;

	/**
	 * 
	 */
	protected boolean isInProduction = false;

	/**
	 * 
	 */
	public static void requestStop() {
		synchronized(instance) {
			instance.isToStop = true;
		}
	}
	
	/**
	 * 
	 */
	public static boolean isShuttingDown() {
		return instance.isToStop;
	}
		
	/**
	 * 
	 */
	public static boolean isInProduction() {
		return instance.isInProduction;
	}
	
	/**
	 * 
	 */
	protected AbstractMain() {
		instance = this;
	}
	
	/**
	 * 
	 */
	protected void handleArgv(String[] argv) {
		// do nothing
	}

	/**
	 * 
	 */
	protected void startUp() throws Exception {
		SysLog.initialize(isInProduction);
		SysConfig.setInstance(createSysConfig());
		
		mainSession = new SysSession("system");
		SessionManager.setThreadLocalSession(mainSession);
	}
	
	/**
	 * 
	 */
	protected SysConfig createSysConfig() {
		if (isInProduction()) {
			return createProdSysConfig();
		} else {
			return createDevSysConfig();
		}
	}
	
	/**
	 * 
	 */
	protected SysConfig createProdSysConfig() {
		return createDevSysConfig(); 
	}
	
	/**
	 * 
	 */
	protected SysConfig createDevSysConfig() {
		//@FIXME
		return new SysConfig("localhost", "8585");
	}
	
	/**
	 * 
	 */
	protected void shutDown() throws Exception {
		SysConfig.dropInstance();
	}
	
}
