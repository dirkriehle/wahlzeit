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

package org.wahlzeit_revisited.main;


import org.wahlzeit_revisited.database.DatabaseConnection;
import org.wahlzeit_revisited.database.SessionManager;
import org.wahlzeit_revisited.database.SysSession;

/**
 * A superclass for a Main class for system startup and shutdown.
 */
public abstract class AbstractMain {

    /*
     * members
     */

    protected SysSession mainSession;

    /**
     * contract methods
     */

    protected void startUp() throws Exception {
        boolean dbAvailable = DatabaseConnection.waitForDatabaseIsReady(30, 1000);
        if (!dbAvailable) {
            throw new RuntimeException("Unable to proceed with wahlzeit app. DB connection could not be established.");
        }

        mainSession = new SysSession("system");
        SessionManager.setSession(mainSession);
    }

}
