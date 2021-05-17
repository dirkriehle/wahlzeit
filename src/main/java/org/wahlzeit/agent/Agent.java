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


import org.apache.log4j.Logger;

/**
 * An Agent executes background tasks.
 */
public abstract class Agent implements Runnable {

    private static final Logger LOG = Logger.getLogger(Agent.class);

    /**
     *
     */
    protected static int id = 0;

    /**
     *
     */
    protected String name = "no_name";

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
        synchronized (Agent.class) {
            String agentName = "agent" + id++;
            LOG.info("started new agent: " + agentName);
        }

        while (!isToStop) {
            try {
                LOG.info("going to sleep for: " + (period / 1000) + " seconds");
                Thread.sleep(period);
            } catch (Exception ex) {
                // do nothing
            }
            LOG.info("just woke up");
            doRun();
        }
    }

    /**
     * @methodtype command
     */
    public void stop() {
        isToStop = true;
    }

    /**
     * @methodproperty hook
     */
    protected void doRun() {
        // do nothing
    }

    /**
     * @methodtype get
     */
    public long getPeriod() {
        return period;
    }

}
