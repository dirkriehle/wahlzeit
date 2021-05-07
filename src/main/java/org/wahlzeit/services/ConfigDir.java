/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.io.File;

/**
 * A ConfigDir is a Directory that can provides a two-way switch between a default directory and custom directory.
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
