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

package org.wahlzeit.services;


/**
 * A Session object maintains a DatabaseConnection and helps track processing time.
 * Typically, there is one for each working thread, be it a system thread or a web session.
 *
 * @author dirkriehle
 */
public class Session {

    public static final String NAME = "name";
    public static final String PROCESSING_TIME = "processingTime";
    public static final String NO_SESSION = "no sessioin";

    /**
     * Session state
     */
    protected String name = null;


    /**
     * processing time for requests
     */
    protected long processingTime = 0;

    /**
     *
     */
    protected Session() {
        // do nothing
    }

    /**
     * @methodtype init
     */
    protected void initialize(String myName) {
        name = myName;
        notifyNameChanged();
    }

    /**
     * @methodproperty hook
     *
     * Hook method for subclasses to get to know when name changes.
     * @see #initialize(String)
     */
    protected void notifyNameChanged() {
    }

    /**
     * @methodtype get
     */
    public String getName() {
        return name;
    }

    /**
     * @methodtype get
     */
    public String getClientId() {
        return "system";
    }

    /**
     * @methodtype init
     */
    public void resetProcessingTime() {
        processingTime = 0;
        notifyProcessingTimeChanged();
    }

    /**
     * @methodproperty hook
     *
     * Hook method for subclasses to get to know when processing time changes.
     * @see #addProcessingTime(long)
     * @see #resetProcessingTime()
     */
    protected void notifyProcessingTimeChanged() {
    }

    /**
     * @methodtype set
     */
    public void addProcessingTime(long time) {
        processingTime += time;
        notifyProcessingTimeChanged();
    }

    /**
     * @methodtype get
     */
    public long getProcessingTime() {
        return processingTime;
    }

}
