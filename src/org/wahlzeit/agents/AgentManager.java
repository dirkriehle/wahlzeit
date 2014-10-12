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

import java.util.*;

import org.wahlzeit.services.SysLog;

/**
 * 
 * @author dirkriehle
 *
 */
public class AgentManager {
	
	/**
	 * 
	 */
	protected static AgentManager instance = null;
	
	/**
	 * @methodtype initialization
	 */
	protected static void initInstance() {
		getInstance().addAgent(new NotifyAboutPraiseAgent());
	}
	
	/**
	 * 
	 */
	public static synchronized AgentManager getInstance() {
		if (instance == null) {
			instance = new AgentManager();
			initInstance();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	protected Map<String, AgentThread> threads = new HashMap<String, AgentThread>();
	
	/**
	 * 
	 */
	protected AgentManager() {
		// do nothing
	}
	
	/**
	 * 
	 */
	public Agent getAgent(String name) {
		AgentThread thread = getThread(name);
		return thread.getAgent();
	}
	
	/**
	 * 
	 */
	public void addAgent(Agent agent) {
		synchronized(threads) {
			String name = agent.getName();
			AgentThread thread = new AgentThread(agent);
			threads.put(name, thread);
		}
	}
	
	/**
	 * @methodtype get
	 */
	public AgentThread getThread(String name) {
		return threads.get(name);	
	}
	
	/**
	 * 
	 */
	public void startAllThreads() {
		for (Iterator<AgentThread> i = threads.values().iterator(); i.hasNext(); ) {
			AgentThread thread = i.next();
			startThread(thread);
		}
	}
	
	/**
	 * 
	 */
	public void startThread(String name) {
		startThread(getThread(name));
	}
		
	/**
	 * 
	 */
	public void startThread(AgentThread thread) {
		thread.start();
		String name = thread.getAgent().getName();
		SysLog.logSysInfo("agent", name, "agent/thread was started");
	}
	
	/**
	 * 
	 */
	public void stopAllThreads() {
		for (Iterator<AgentThread> i = threads.values().iterator(); i.hasNext(); ) {
			AgentThread thread = i.next();
			stopThread(thread);
		}
	}
	
	/**
	 * 
	 */
	public void stopThread(String name) {
		stopThread(getThread(name));
	}
	
	/**
	 * 
	 */
	public void stopThread(AgentThread thread) {
		Agent agent = thread.getAgent();
		agent.stop();
		
		thread.interrupt();
		while(thread.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				// do nothing
			}
		}

		String agentName = thread.getAgent().getName();
		SysLog.logSysInfo("agent", agentName, "agent/thread was stopped");
	}

}
