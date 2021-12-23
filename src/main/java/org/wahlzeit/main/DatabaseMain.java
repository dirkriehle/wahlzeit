/*
 * Copyright (c) 2006-2009 Dirk Riehle, http://dirkriehle.com
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

package org.wahlzeit.main;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wahlzeit.config.WahlzeitConfig;
import org.wahlzeit.database.SessionManager;

public class DatabaseMain extends ModelMain {

    protected static final Logger LOG = LogManager.getLogger(DatabaseMain.class);

    public DatabaseMain(WahlzeitConfig config) {
        super(config);
    }

    public void shutDown() {
        SessionManager.dropSession();
        LOG.info("Shutting down database");
    }
}
