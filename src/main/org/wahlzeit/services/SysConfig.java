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

import java.io.*;

import org.wahlzeit.utils.*;

/**
 * A basic set of system configuration data
 * 
 * @author dirkriehle
 *
 */
public class SysConfig extends AbstractConfig {
	
	/**
	 * Database driver definitions
	 */
	public static final String DB_DRIVER = "DB_DRIVER";
	public static final String DB_CONNECTION = "DB_CONNECTION";
	public static final String DB_USER = "DB_USER";
	public static final String DB_PASSWORD = "DB_PASSWORD";
	
	/**
	 * 
	 */
	protected static SysConfig instance = null;
	
	/**
	 * 
	 */
	public static SysConfig getInstance() {
		if (instance == null) {
			SysLog.logSysInfo("creating generic SysConfig");
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
	 * Drop singleton instance to cope with repeated startup/shutdown scenarios
	 */
	public static synchronized void dropInstance() {
		instance = null;
		SysLog.logSysInfo("dropped current SysConfig");
	}
	
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
		photosDir = new Directory(rootDir, "data" + File.separator + "photos");
		backupDir = new Directory(rootDir, "data" + File.separator + "backup");
		tempDir = new Directory(rootDir, "data" + File.separator + "temp");
		
		// Database connection
		doSetValue(SysConfig.DB_DRIVER, "org.postgresql.Driver");
		doSetValue(SysConfig.DB_CONNECTION, "jdbc:postgresql://localhost:5432/wahlzeit");
		doSetValue(SysConfig.DB_USER, "wahlzeit");
		doSetValue(SysConfig.DB_PASSWORD, "wahlzeit");
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

	/**
	 * 
	 */
	public static String getDbDriverAsString()	{
		return getInstance().getValue(SysConfig.DB_DRIVER);
	}
	
	/**
	 * 
	 */
	public static String getDbConnectionAsString() {
		return getInstance().getValue(SysConfig.DB_CONNECTION);
	}
	
	/**
	 * 
	 */
	public static String getDbUserAsString() {
		return getInstance().getValue(SysConfig.DB_USER);
	}
	
	/**
	 * 
	 */
	public static String getDbPasswordAsString() {
		return getInstance().getValue(SysConfig.DB_PASSWORD);
	}

}
