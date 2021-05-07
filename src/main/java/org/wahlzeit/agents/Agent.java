/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.agents;

import org.wahlzeit.services.*;

/**
 * An Agent executes background tasks.
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
	 * @methodtype initialization
	 */
	protected void initialize(String myName, long myPeriod) {
		name = myName;
		period = myPeriod;
	}
	
	/**
	 * @methodtype get
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @methodtype command
	 */
	public void run() {
		synchronized(Agent.class) {
			String agentName = "agent" + id++;
			Session agentSession = new SysSession(agentName);
			SessionManager.setThreadLocalSession(agentSession);
			SysLog.logInfo("started new agent", agentName);
		}

		while(!isToStop) {
			try {
				SysLog.logSysInfo("going to sleep for: " + (period / 1000) + " seconds");
				Thread.sleep(period);
			} catch (Exception ex) {
				// do nothing
			}
			SysLog.logSysInfo("just woke up");
			doRun();
		}
	}
	
	/**
	 * @methodproperty hook
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
