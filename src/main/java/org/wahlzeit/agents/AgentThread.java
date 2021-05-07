/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.agents;

/**
 * 
 * @author dirkriehle
 *
 */
public class AgentThread extends Thread {
	
	/**
	 * 
	 */
	protected Agent agent = null;
	
	/**
	 * 
	 */
	public AgentThread(Agent myAgent) {
		super(myAgent);
		agent = myAgent;
	}
	
	/**
	 * 
	 */
	public Agent getAgent() {
		return agent;
	}

}
