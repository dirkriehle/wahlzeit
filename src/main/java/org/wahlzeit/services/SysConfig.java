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

import java.io.File;
import java.util.logging.Logger;

/**
 * A basic set of system configuration data
 *
 * @author dirkriehle
 */
public class SysConfig extends AbstractConfig {

    public static String DATA_PATH = "org-wahlzeit-data";
    /**
     *
     */
    protected static SysConfig instance = null;
    private static Logger log = Logger.getLogger(SysConfig.class.getName());
    /**
     *
     */
    protected String rootDir;
    /**
     *
     */
    protected ConfigDir scriptsDir;
    protected ConfigDir staticDir;
    protected ConfigDir templatesDir;
    /**
     *
     */
    protected Directory photosDir;
    protected Directory backupDir;
    protected Directory tempDir;
    /**
     *
     */
    public SysConfig() {
        this("");
    }

    /**
     *
     */
    public SysConfig(String myRootDir) {
        // Root directory
        rootDir = myRootDir;

        // Config directories
        scriptsDir = new ConfigDir(rootDir, "config" + File.separator + "scripts");
        staticDir = new ConfigDir(rootDir, "config" + File.separator + "static");
        templatesDir = new ConfigDir(rootDir, "config" + File.separator + "templates");

        // Data directories
        photosDir = new Directory(rootDir, DATA_PATH + File.separator + "photos");
        backupDir = new Directory(rootDir, DATA_PATH + File.separator + "backup");
        tempDir = new Directory(rootDir, DATA_PATH + File.separator + "temp");
    }

    /**
     * Drop singleton instance to cope with repeated startup/shutdown scenarios
     */
    public static synchronized void dropInstance() {
        log.config(LogBuilder.createSystemMessage().addAction("drop SysConfig instance").toString());
        instance = null;
    }

    /**
     *
     */
    public static String getRootDirAsString() {
        return getInstance().rootDir;
    }

    /**
     *
     */
    public static SysConfig getInstance() {
        if (instance == null) {
            log.config(LogBuilder.createSystemMessage().addAction("create generic SysConfig").toString());
            setInstance(new SysConfig(""));
        }
        return instance;
    }

    /**
     * Sets the singleton instance of SysConfig.
     *
     * @methodtype set
     * @methodproperty composed, class
     */
    public static synchronized void setInstance(SysConfig sysConfig) {
        assertIsUninitialized();
        instance = sysConfig;
    }

    /**
     * @methodtype assertion
     * @methodproperty primitive, class
     */
    public static synchronized void assertIsUninitialized() {
        if (instance != null) {
            throw new IllegalStateException("attempt to initalize SysConfig again");
        }
    }

    /**
     *
     */
    public static ConfigDir getStaticDir() {
        return getInstance().staticDir;
    }

    /**
     *
     */
    public static ConfigDir getScriptsDir() {
        return getInstance().scriptsDir;
    }

    /**
     *
     */
    public static ConfigDir getTemplatesDir() {
        return getInstance().templatesDir;
    }

    /**
     *
     */
    public static Directory getPhotosDir() {
        return getInstance().photosDir;
    }

    /**
     *
     */
    public static Directory getBackupDir() {
        return getInstance().backupDir;
    }

    /**
     *
     */
    public static Directory getTempDir() {
        return getInstance().tempDir;
    }
}
