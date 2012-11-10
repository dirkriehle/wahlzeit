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
 * A set of utility functions to retrieve URLs and files.
 * Call setValue(key, value) to change default values, see below.
 * 
 * @author dirkriehle
 *
 */
public class SysConfig extends AbstractConfig {
	
	/**
	 * Key definitions
	 */
	public static final String HTTP_PORT = "HTTP_PORT";
	public static final String SITE_URL = "SITE_URL";
	
	public static final String PHOTOS_URL_PATH = "PHOTOS_URL_PATH";
	public static final String PHOTOS_URL = "PHOTOS_URL";
	public static final String PHOTOS_DIR = "PHOTOS_DIR";
	public static final String BACKUP_DIR = "BACKUP_DIR";
	
	public static final String TEMP_DIR = "TEMP_DIR";
	public static final String HEADING_IMAGE = "HEADING_IMAGE";
	public static final String LOGO_IMAGE = "LOGO_IMAGE";
	public static final String EMPTY_IMAGE = "EMPTY_IMAGE";
	
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
			SysLog.logInfo("setting generic SysConfig");
			setInstance(new SysConfig("localhost", "8585"));
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
		SysLog.logInfo("dropped current SysConfig");
	}
	
	/**
	 * 
	 */
	protected ConfigDir scriptsDir = new ConfigDir("config" + File.separator + "scripts");
	protected ConfigDir staticDir = new ConfigDir("config" + File.separator + "static");
	protected ConfigDir templatesDir = new ConfigDir("config" + File.separator + "templates");
	
	/**
	 * 
	 */
	public SysConfig(String host) {
		this(host, "80");
	}
	
	/**
	 *
	 */
	public SysConfig(String host, String port) {
		// Web frontend access
		if (!StringUtil.isNullOrEmptyString(port) && !port.equals("80")) {
			doSetValue(SysConfig.HTTP_PORT, port);
			doSetValue(SysConfig.SITE_URL, "http://" + host + ":" + port + "/");
		} else {
			doSetValue(SysConfig.HTTP_PORT, "80");
			doSetValue(SysConfig.SITE_URL, "http://" + host + "/");
		}

		doSetValue(SysConfig.PHOTOS_URL_PATH, "data/photos/");
		doSetValue(SysConfig.PHOTOS_URL, doGetValue(SysConfig.SITE_URL) + doGetValue(SysConfig.PHOTOS_URL_PATH));

		// Local filesystem access
		doSetValue(SysConfig.PHOTOS_DIR, "data" + File.separator + "photos" + File.separator);
		doSetValue(SysConfig.BACKUP_DIR, "data" + File.separator + "backup" + File.separator);
		doSetValue(SysConfig.TEMP_DIR, "data" + File.separator + "temp" + File.separator);
		
		// Some file names
		doSetValue(SysConfig.HEADING_IMAGE, "heading.png");
		doSetValue(SysConfig.LOGO_IMAGE, "wahlzeit.png");
		doSetValue(SysConfig.EMPTY_IMAGE, "empty.png");
	
		// Database connection
		doSetValue(SysConfig.DB_DRIVER, "org.postgresql.Driver");
		doSetValue(SysConfig.DB_CONNECTION, "jdbc:postgresql://localhost:5432/wahlzeit");
		doSetValue(SysConfig.DB_USER, "wahlzeit");
		doSetValue(SysConfig.DB_PASSWORD, "wahlzeit");
	}
		
	/**
	 * 
	 */
	public static String getSiteUrlAsString() {
		return getInstance().getValue(SysConfig.SITE_URL);
	}

	/**
	 * 
	 */
	public static String getLinkAsUrlString(String link) {
		return getSiteUrlAsString() + link + ".html";
	}
	
	/**
	 * 
	 */
	public static String getPhotosUrlAsString() {
		return getInstance().getValue(SysConfig.PHOTOS_URL);
	}

	/**
	 * 
	 */
	public static String getHttpPortAsString() {
		return getInstance().getValue(SysConfig.HTTP_PORT);
	}
	/**
	 * 
	 */
	public static int getHttpPortAsInt() {
		return Integer.parseInt(getHttpPortAsString());
	}

	/**
	 * 
	 */
	public static String getPhotosDirAsString() {
		return getInstance().getValue(SysConfig.PHOTOS_DIR);
	}

	/**
	 * 
	 * @return the photos URL path
	 */
	public static String getPhotosUrlPathAsString() {
		return getInstance().getValue(SysConfig.PHOTOS_URL_PATH);
	}

	/**
	 *
	 */
	public static String getBackupDirAsString() {
		return getInstance().getValue(SysConfig.BACKUP_DIR);
	}

	/**
	 * 
	 */
	public static String getTempDirAsString() {
		return getInstance().getValue(SysConfig.TEMP_DIR);
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
	public static String getHeadingImageAsUrlString(Language l) {
		String sfn = l.asIsoCode() + File.separator + getInstance().getValue(SysConfig.HEADING_IMAGE);
		String ffn = getStaticDir().getFullConfigFileUrl(sfn);
		return getSiteUrlAsString() + ffn;
	}
	
	/**
	 * 
	 */
	public static String getLogoImageAsUrlString(Language l) {
		String sfn = l.asIsoCode() + File.separator + getInstance().getValue(SysConfig.LOGO_IMAGE);
		String ffn = getStaticDir().getFullConfigFileUrl(sfn);
		return getSiteUrlAsString() + ffn;
	}
	
	/**
	 * 
	 */
	public static String getEmptyImageAsUrlString(Language l) {
		String sfn = l.asIsoCode() + File.separator + getInstance().getValue(SysConfig.EMPTY_IMAGE);
		String ffn = getStaticDir().getFullConfigFileUrl(sfn);
		return getSiteUrlAsString() + ffn;
	}

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
