/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.agents;

import java.util.*;

import org.wahlzeit.services.SysLog;

/**
 * The AgentManager singleton manages all Agent instances.
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
