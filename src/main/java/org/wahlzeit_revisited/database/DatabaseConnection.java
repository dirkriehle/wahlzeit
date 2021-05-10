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

package org.wahlzeit_revisited.database;

import org.wahlzeit_revisited.utils.SysConfig;
import org.wahlzeit_revisited.utils.SysLog;

import java.sql.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A database connection wraps an RDMBS connection object.
 * It pools and reuses existing connections; it caches common SQL statements.
 *
 * @author dirkriehle
 */
public class DatabaseConnection {

    /**
     *
     */
    protected static final Set<DatabaseConnection> pool = ConcurrentHashMap.newKeySet();
    protected static final SysConfig sysConfig = new SysConfig();

    /**
     *
     */
    protected static int dbcId = 0;

    /**
     *
     */
    public static synchronized DatabaseConnection ensureDatabaseConnection() throws SQLException {
        DatabaseConnection result;
        if (pool.isEmpty()) {
            result = new DatabaseConnection("dbc" + dbcId++);
            SysLog.logCreatedObject("DatabaseConnection", result.getName());
        } else {
            result = pool.iterator().next();
            pool.remove(result);
        }

        return result;
    }

    /**
     * Wait until a database connection can be established or the max retry counter is reached.
     *
     * @param retries                 amount of retries until fail.
     * @param sleepTimeBetweenRetries time to wait until next retry in milliseconds.
     * @return true if connection could be established, otherwise return false.
     */
    public static synchronized boolean waitForDatabaseIsReady(final int retries, final int sleepTimeBetweenRetries) {
        int retryCounter = retries;
        String dbUrl = sysConfig.getDbConnectionAsString();
        do {
            try {
                DatabaseConnection.ensureDatabaseConnection();
                SysLog.logSysInfo("[success] Service check for URL " + dbUrl);
                return true;
            } catch (final SQLException e) {
                final String msg = String.format("[%d/%d] Retry service check for URL %s", retryCounter, retries, dbUrl);
                SysLog.logSysInfo(msg);
                doSleep(sleepTimeBetweenRetries);
            }

            retryCounter--;
        } while (retryCounter > 0);

        SysLog.logSysError("[failure] Service check for URL  " + dbUrl);
        return false;
    }

    private static void doSleep(final int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (final InterruptedException e) {
        }
    }

    /*
     * Ensure Database driver is available inside of the project
     */
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            SysLog.logThrowable(ex);
        }
    }

    /**
     *
     */
    public static Connection openRdbmsConnection() throws SQLException {
        String dbConnection = sysConfig.getDbConnectionAsString();
        String dbUser = sysConfig.getDbUserAsString();
        String dbPassword = sysConfig.getDbPasswordAsString();
        Connection result = DriverManager.getConnection(dbConnection, dbUser, dbPassword);
        SysLog.logSysInfo("opening database connection: " + result.toString());
        return result;
    }

    /**
     *
     */
    public static void closeConnection(Connection cn) throws SQLException {
        SysLog.logSysInfo("closing database connection: " + cn.toString());
        cn.close();
    }


    /**
     *
     */
    protected String name;

    /**
     *
     */
    protected Connection rdbmsConnection;

    /**
     *
     */
    protected DatabaseConnection(String dbcName) throws SQLException {
        name = dbcName;
        rdbmsConnection = openRdbmsConnection();
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public boolean isOpen() {
        boolean result = false;

        try {
            result = (rdbmsConnection != null) && !rdbmsConnection.isClosed();
        } catch (SQLException ex) {
            SysLog.logThrowable(ex);
        }

        return result;
    }

    /**
     *
     */
    public Connection getRdbmsConnection() throws SQLException {
        return rdbmsConnection;
    }

    /**
     *
     */
    public PreparedStatement getReadingStatement(String stmt) throws SQLException {
        PreparedStatement result = getRdbmsConnection().prepareStatement(stmt);
        return result;
    }

    /**
     *
     */
    public PreparedStatement getUpdatingStatement(String stmt) throws SQLException {
        PreparedStatement result = getRdbmsConnection().prepareStatement(stmt, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        return result;

    }

}
