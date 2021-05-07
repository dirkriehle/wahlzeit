/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.io.*;

/**
 * A basic set of system configuration data
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
			setInstance(new SysConfig());
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
		this("", "localhost");
	}
	
	/**
	 *
	 */
	public SysConfig(String myRootDir, String dbHostName) {
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
		doSetValue(SysConfig.DB_CONNECTION, "jdbc:postgresql://" + dbHostName + ":5432/wahlzeit");
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
