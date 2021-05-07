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


import org.wahlzeit_revisited.config.ConfigDir;
import org.wahlzeit_revisited.database.DatabaseConnection;
import org.wahlzeit_revisited.database.SessionManager;
import org.wahlzeit_revisited.utils.FileUtil;
import org.wahlzeit_revisited.utils.SysLog;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

import java.sql.*;

/**
 * A single-threaded Main class with database connection. Can be used by tools that don't want to start a server.
 */
public abstract class ModelMain extends AbstractMain {

    private final WahlzeitConfig config;

    protected ModelMain(WahlzeitConfig config) {
        this.config = config;
    }

    /**
     *
     */
    public void startUp() throws Exception {
        super.startUp();
        if (!hasGlobals()) {
            tearDownDatabase();
            setUpDatabase();
        }
    }

    /**
     *
     */
    protected boolean hasGlobals() throws SQLException {
        DatabaseConnection dbc = mainSession.ensureDatabaseConnection();
        Connection conn = dbc.getRdbmsConnection();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "globals", null);
        return tables.next();
    }

    /**
     *
     */
    public void setUpDatabase() throws SQLException {
        runScript("CreateTables.sql");
    }

    /**
     *
     */
    public void tearDownDatabase() throws SQLException {
        runScript("DropTables.sql");
    }

    /**
     *
     */
    protected void runScript(String scriptName) throws SQLException {
        DatabaseConnection dbc = SessionManager.getDatabaseConnection();
        Connection conn = dbc.getRdbmsConnection();

        ConfigDir scriptsDir = config.getScriptsDir();

        if (scriptsDir.hasDefaultFile(scriptName)) {
            String defaultScriptFileName = scriptsDir.getAbsoluteDefaultConfigFileName(scriptName);
            runScript(conn, defaultScriptFileName);
        }

        if (scriptsDir.hasCustomFile(scriptName)) {
            String customConfigFileName = scriptsDir.getAbsoluteCustomConfigFileName(scriptName);
            runScript(conn, customConfigFileName);
        }
    }

    /**
     *
     */
    protected void runScript(Connection conn, String fullFileName) throws SQLException {
        String query = FileUtil.safelyReadFileAsString(fullFileName);
        SysLog.logQuery(query);

        Statement stmt = conn.createStatement();
        stmt.execute(query);
    }

}
