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

import org.wahlzeit.services.LogBuilder;

import java.util.logging.Logger;

/**
 * @author dirkriehle
 */
public abstract class Agent {

	private static Logger log = Logger.getLogger(Agent.class.getName());

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
	protected Agent() {
		// do nothing
	}

	/**
	 * @methodtype initialization
	 */
	protected void initialize(String myName) {
		name = myName;
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
		synchronized (Agent.class) {
			String agentName = "agent" + id++;
			log.config(LogBuilder.createSystemMessage().addAction("started new agent").addParameter("name", agentName)
					.addParameter("ID", id).toString());
		}

		try {
			doRun();
		} catch (Exception e) {
			log.config(LogBuilder.createSystemMessage().addParameter("agent name", name).addException(
					"Problem when executing task", e).toString());
		}
	}

	/**
	 * @methodproperty hook
	 */
	protected void doRun() {
		// do nothing
	}

}
