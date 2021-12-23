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

package org.wahlzeit.agent;

import jakarta.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The AgentManager singleton manages all Agent instances.
 */
@Singleton
public class AgentManager {

    private static final Logger LOG = LogManager.getLogger(AgentManager.class);

    /**
     *
     */
    protected final Map<String, AgentThread> threads = new ConcurrentHashMap<>();

    public AgentManager() {
        addAgent(new NotifyAboutPraiseAgent());
    }

    /**
     * @methodtype command
     */
    public void startAllThreads() {
        for (AgentThread thread : threads.values()) {
            startThread(thread);
        }
    }

    /**
     * @methodtype command
     */
    public void stopAllThreads() {
        for (AgentThread thread : threads.values()) {
            stopThread(thread);
        }
    }

    /**
     * @methodtype command
     */
    public void startThread(AgentThread thread) {
        thread.start();
        String name = thread.getAgent().getName();
        LOG.info("agent " + name + " agent/thread was started");
    }

    /**
     * @methodtype command
     */
    public void stopThread(AgentThread thread) {
        Agent agent = thread.getAgent();
        agent.stop();

        thread.interrupt();
        while (thread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                // do nothing
            }
        }

        String agentName = thread.getAgent().getName();
        LOG.info("agent " + agentName + " agent/thread was stopped");
    }

    /**
     * @methodtype get
     */
    @SuppressWarnings("unchecked")
    public <T extends Agent> T getAgent(String name) {
        AgentThread thread = threads.get(name);
        return (T) thread.getAgent();
    }

    /**
     * @methodtype set
     */
    public void addAgent(Agent agent) {
        String name = agent.getName();
        AgentThread thread = new AgentThread(agent);
        threads.put(name, thread);
    }

}