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
import org.wahlzeit_revisited.config.AbstractConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A basic set of system configuration data
 */
@Singleton
public class SysConfig extends AbstractConfig implements WahlzeitConfig {

    /**
     * Database driver definitions
     */

    public static final String DB_HOST = Optional.ofNullable(System.getenv("WAHLZEIT_DB_HOST")).orElse("localhost");
    public static final String DB_DRIVER = "DB_DRIVER";
    public static final String DB_CONNECTION = "DB_CONNECTION";
    public static final String DB_USER = "DB_USER";
    public static final String DB_PASSWORD = "DB_PASSWORD";

    /**
     * Config directories
     */
    protected File photosDir;
    protected Path scriptsPath;
    protected Path languagePath;

    /**
     * Constructor
     */

    public SysConfig() {
        // Config directories
        String photoDirPath = Path.of("config", "photos").toString();
        photosDir = new File(getClass().getClassLoader().getResource(photoDirPath).getFile());
        scriptsPath = Path.of("config", "db");
        languagePath = Path.of("config", "lang");

        // Database connection
        doSetValue(SysConfig.DB_DRIVER, "org.postgresql.Driver");
        doSetValue(SysConfig.DB_CONNECTION, "jdbc:postgresql://" + DB_HOST + ":5432/wahlzeit");
        doSetValue(SysConfig.DB_USER, "wahlzeit");
        doSetValue(SysConfig.DB_PASSWORD, "wahlzeit");
    }


    /**
     * @methodtype get
     */
    @Override
    public File getPhotosDir() {
        return photosDir;
    }

    /**
     * @methodtype get
     */
    @Override
    public Path getScriptsPath() {
        return scriptsPath;
    }

    @Override
    public Path getLanguagePath() {
        return languagePath;
    }

    /**
     * @methodtype get
     */
    @Override
    public String getDbDriverAsString() {
        return getValue(DB_DRIVER);
    }

    /**
     * @methodtype get
     */
    @Override
    public String getDbConnectionAsString() {
        return getValue(DB_CONNECTION);
    }

    /**
     * @methodtype get
     */
    @Override
    public String getDbUserAsString() {
        return getValue(DB_USER);
    }

    /**
     * @methodtype get
     */
    @Override
    public String getDbPasswordAsString() {
        return getValue(DB_PASSWORD);
    }

}
