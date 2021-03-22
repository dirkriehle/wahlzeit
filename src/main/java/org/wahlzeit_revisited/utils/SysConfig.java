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

package org.wahlzeit_revisited.utils;

import jakarta.inject.Singleton;
import org.wahlzeit_revisited.database.config.AbstractConfig;
import org.wahlzeit_revisited.database.config.ConfigDir;
import org.wahlzeit_revisited.database.config.Directory;

import java.io.File;
import java.util.Optional;

/**
 * A basic set of system configuration data
 */
@Singleton
public class SysConfig extends AbstractConfig implements WahlzeitConfig {

    /**
     * Database driver definitions
     */

    public static final String ROOT_DIR = "src/main/webapp";
    public static final String MAPPING_SLUG = "/static";

    public static final String DB_HOST = Optional.ofNullable(System.getenv("WAHLZEIT_DB_HOST")).orElse("localhost");
    public static final String DB_DRIVER = "DB_DRIVER";
    public static final String DB_CONNECTION = "DB_CONNECTION";
    public static final String DB_USER = "DB_USER";
    public static final String DB_PASSWORD = "DB_PASSWORD";

    /**
     * base directories
     */
    protected String rootDir;
    protected String staticMappingSlug;

    /**
     * Config directories
     */
    protected ConfigDir scriptsDir;
    protected ConfigDir staticDir;
    protected ConfigDir templatesDir;

    /**
     * Data directories
     */
    protected Directory photosDir;
    protected Directory backupDir;
    protected Directory tempDir;

    /**
     * Constructor
     */

    public SysConfig() {
        // Root directory
        rootDir = ROOT_DIR;
        staticMappingSlug = MAPPING_SLUG;

        // Config directories
        scriptsDir = new ConfigDir(rootDir, "config" + File.separator + "scripts");
        staticDir = new ConfigDir(rootDir, "config" + File.separator + "static");
        templatesDir = new ConfigDir(rootDir, "config" + File.separator + "templates");

        // Data directories
        photosDir = new Directory(rootDir, "data" + File.separator + "photos");
        backupDir = new Directory(rootDir, "data" + File.separator + "backup");
        tempDir = new Directory(rootDir, "data" + File.separator + "temp");

        // Database connection
        doSetValue(SysConfig.DB_DRIVER, "org.postgresql.Driver");
        doSetValue(SysConfig.DB_CONNECTION, "jdbc:postgresql://" + DB_HOST + ":5432/wahlzeit");
        doSetValue(SysConfig.DB_USER, "wahlzeit");
        doSetValue(SysConfig.DB_PASSWORD, "wahlzeit");
    }

    /**
     * getter
     */

    public String getRootDirAsString() {
        return rootDir;
    }

    public ConfigDir getStaticDir() {
        return staticDir;
    }

    public ConfigDir getScriptsDir() {
        return scriptsDir;
    }

    public ConfigDir getTemplatesDir() {
        return templatesDir;
    }

    public Directory getPhotosDir() {
        return photosDir;
    }

    public Directory getBackupDir() {
        return backupDir;
    }

    public Directory getTempDir() {
        return tempDir;
    }

    public String getStaticFileMappingPath() {
        return staticMappingSlug;
    }

    public String getDbDriverAsString() {
        return getValue(DB_DRIVER);
    }

    public String getDbConnectionAsString() {
        return getValue(DB_CONNECTION);
    }

    public String getDbUserAsString() {
        return getValue(DB_USER);
    }

    public String getDbPasswordAsString() {
        return getValue(DB_PASSWORD);
    }

}
