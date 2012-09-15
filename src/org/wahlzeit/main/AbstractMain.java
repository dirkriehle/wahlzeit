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
	protected static boolean isToStopFlag = false;

	/**
	 * 
	 */
	protected static boolean isInProductionFlag = false;
	
	/**
	 * 
	 */
	public static void requestStop() {
		synchronized(instance) {
			isToStopFlag = true;
			instance.notify();
		}
	}
	
	/**
	 * 
	 */
	public static boolean isShuttingDown() {
		return isToStopFlag;
	}
		
	/**
	 * 
	 */
	public static boolean isInProduction() {
		return isInProductionFlag;
	}
	
	/**
	 * 
	 */
	public synchronized void run(String[] argv) {
		handleArgv(argv);
		
		try {
			startUp();
			execute();
		} catch(Exception ex) {
			SysLog.logThrowable(ex);
		}

		try {
			shutDown();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
	} 

	/**
	 * 
	 */
	protected void handleArgv(String[] argv) {
		for (int i = 0; i < argv.length; i++) {
			String arg = argv[i];
			if (arg.equals("-P") || arg.equals("--production")) {
				AbstractMain.isInProductionFlag = true;
			} else if (arg.equals("-D") || arg.equals("--development")) {
				AbstractMain.isInProductionFlag = false;
			}
		}		
	}

	/**
	 * 
	 */
	protected void startUp() throws Exception {
		SysLog.initialize(isInProductionFlag);
		SysConfig.setInstance(createSysConfig());
		
		Session ctx = new SysSession("system");
		ContextManager.setThreadLocalContext(ctx);
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
		return new SysConfig("localhost", "8585");
	}
	
	/**
	 * 
	 */
	protected void execute() throws Exception {
		// do nothing
	}

	/**
	 * 
	 */
	protected void shutDown() throws Exception {
		SysConfig.dropInstance();
	}
	
}
