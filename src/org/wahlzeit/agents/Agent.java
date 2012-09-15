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

package org.wahlzeit.agents;

import org.wahlzeit.services.*;

/**
 * 
 * @author dirkriehle
 *
 */
public abstract class Agent implements Runnable {
	
	/**
	 * 
	 */
	protected static int id = 0;
	
	/**
	 * 
	 */
	protected String name = "no name";
	
	/**
	 * 
	 */
	protected boolean isToStop = false;
	
	/**
	 * Full period after which task gets repeated
	 */
	protected long period = 60 * 60 * 1000; // default = 1h, in millis
	
	/**
	 * 
	 */
	protected Agent() {
		// do nothing
	}
	
	/**
	 * 
	 */
	protected void initialize(String myName, long myPeriod) {
		name = myName;
		period = myPeriod;
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
	public void run() {
		synchronized(Agent.class) {
			Session ctx = new SysSession("agent" + id++);
			ContextManager.setThreadLocalContext(ctx);
		}

		while(!isToStop) {
			try {
				SysLog.logInfo("going to sleep for: " + (period / 1000) + " seconds");
				Thread.sleep(period);
			} catch (Exception ex) {
				// do nothing
			}
			SysLog.logInfo("just woke up");
			doRun();
		}
	}
	
	/**
	 * 
	 */
	protected void doRun() {
		// do nothing
	}
	
	/**
	 * 
	 */
	public long getPeriod() {
		return period;
	}
	
	
	/**
	 *
	 */
	public void stop() {
		isToStop = true;
	}
	
}
