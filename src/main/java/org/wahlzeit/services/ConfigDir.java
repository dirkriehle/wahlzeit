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

/**
 * A ConfigDir is a Directory that can provides a two-way switch between a default directory and custom directory.
 *
 * @author dirkriehle
 */
public class ConfigDir extends Directory {

    /**
     *
     */
    public static final String DEFAULT_DIR_NAME = "default";
    public static final String CUSTOM_DIR_NAME = "custom";

    /**
     *
     */
    protected String defaultDirName;
    protected String customDirName;

    /**
     *
     */
    public ConfigDir(String newRootDir, String newRelativeDir) {
        super(newRootDir, newRelativeDir);
        defaultDirName = asString() + File.separator + DEFAULT_DIR_NAME;
        customDirName = asString() + File.separator + CUSTOM_DIR_NAME;
    }

    /**
     *
     */
    public String getAbsoluteConfigFileName(String shortFileName) {
        return getRootDir() + File.separator + getRelativeConfigFileName(shortFileName);
    }

    /**
     *
     */
    public String getRelativeConfigFileName(String shortFileName) {
        if (hasDefaultFile(shortFileName)) {
            return getRelativeDefaultConfigFileName(shortFileName);
        } else {
            return getRelativeCustomConfigFileName(shortFileName);
        }
    }

    /**
     *
     */
    public String getAbsoluteDefaultConfigFileName(String shortFileName) {
        return getRootDir() + File.separator + getRelativeDefaultConfigFileName(shortFileName);
    }

    /**
     *
     */
    public String getRelativeDefaultConfigFileName(String shortFileName) {
        return getRelativeDir() + File.separator + DEFAULT_DIR_NAME + File.separator + shortFileName;
    }

    /**
     *
     */
    public String getAbsoluteCustomConfigFileName(String shortFileName) {
        return getRootDir() + File.separator + getRelativeCustomConfigFileName(shortFileName);
    }

    /**
     *
     */
    public String getRelativeCustomConfigFileName(String shortFileName) {
        return getRelativeDir() + File.separator + CUSTOM_DIR_NAME + File.separator + shortFileName;
    }

    /**
     *
     */
    public boolean hasDefaultFile(String shortFileName) {
        return doesFileExist(defaultDirName + File.separator + shortFileName);
    }

    /**
     *
     */
    public boolean hasCustomFile(String shortFileName) {
        return doesFileExist(customDirName + File.separator + shortFileName);
    }

    /**
     *
     */
    protected boolean doesFileExist(String fullFileName) {
        return new File(fullFileName).exists();
    }

}
