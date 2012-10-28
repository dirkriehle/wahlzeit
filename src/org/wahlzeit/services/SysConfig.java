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
	
	public static class KeyCollection {
		public final String HTTP_PORT = "HTTP_PORT";
		public final String SITE_URL = "SITE_URL";
		
		public final String PHOTOS_URL_PATH = "PHOTOS_URL_PATH";
		public final String PHOTOS_URL = "PHOTOS_URL";
		public final String PHOTOS_DIR = "PHOTOS_DIR";
		public final String BACKUP_DIR = "BACKUP_DIR";
		
		public final String TEMP_DIR = "TEMP_DIR";
		public final String HEADING_IMAGE = "HEADING_IMAGE";
		public final String LOGO_IMAGE = "LOGO_IMAGE";
		public final String EMPTY_IMAGE = "EMPTY_IMAGE";
		
		public final String DB_DRIVER = "DB_DRIVER";
		public final String DB_CONNECTION = "DB_CONNECTION";
		public final String DB_USER = "DB_USER";
		public final String DB_PASSWORD = "DB_PASSWORD";
	}
	
	public static KeyCollection DEFAULT_KEYS = new KeyCollection();
	
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
	 * Method to set the singleton instance of SysConfig.
	 */
	public static synchronized void setInstance(SysConfig sysConfig) {
		if (instance != null) {
			throw new IllegalStateException("attempt to initalize SysConfig again");
		}
		
		instance = sysConfig;
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
			doSetValue(DEFAULT_KEYS.HTTP_PORT, port);
			doSetValue(DEFAULT_KEYS.SITE_URL, "http://" + host + ":" + port + "/");
		} else {
			doSetValue(DEFAULT_KEYS.HTTP_PORT, "80");
			doSetValue(DEFAULT_KEYS.SITE_URL, "http://" + host + "/");
		}

		doSetValue(DEFAULT_KEYS.PHOTOS_URL_PATH, "data/photos/");
		doSetValue(DEFAULT_KEYS.PHOTOS_URL, doGetValue(DEFAULT_KEYS.SITE_URL) + doGetValue(DEFAULT_KEYS.PHOTOS_URL_PATH));

		// Local filesystem access
		doSetValue(DEFAULT_KEYS.PHOTOS_DIR, "data" + File.separator + "photos" + File.separator);
		doSetValue(DEFAULT_KEYS.BACKUP_DIR, "data" + File.separator + "backup" + File.separator);
		doSetValue(DEFAULT_KEYS.TEMP_DIR, "data" + File.separator + "temp" + File.separator);
		
		// Some file names
		doSetValue(DEFAULT_KEYS.HEADING_IMAGE, "heading.png");
		doSetValue(DEFAULT_KEYS.LOGO_IMAGE, "wahlzeit.png");
		doSetValue(DEFAULT_KEYS.EMPTY_IMAGE, "empty.png");
	
		// Database connection
		doSetValue(DEFAULT_KEYS.DB_DRIVER, "org.postgresql.Driver");
		doSetValue(DEFAULT_KEYS.DB_CONNECTION, "jdbc:postgresql://localhost:5432/wahlzeit");
		doSetValue(DEFAULT_KEYS.DB_USER, "wahlzeit");
		doSetValue(DEFAULT_KEYS.DB_PASSWORD, "wahlzeit");
	}
		
	/**
	 * 
	 */
	public static String getSiteUrlAsString() {
		return getInstance().getValue(DEFAULT_KEYS.SITE_URL);
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
		return getInstance().getValue(DEFAULT_KEYS.PHOTOS_URL);
	}

	/**
	 * 
	 */
	public static String getHttpPortAsString() {
		return getInstance().getValue(DEFAULT_KEYS.HTTP_PORT);
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
		return getInstance().getValue(DEFAULT_KEYS.PHOTOS_DIR);
	}

	/**
	 * 
	 * @return the photos URL path
	 */
	public static String getPhotosUrlPathAsString() {
		return getInstance().getValue(DEFAULT_KEYS.PHOTOS_URL_PATH);
	}

	/**
	 *
	 */
	public static String getBackupDirAsString() {
		return getInstance().getValue(DEFAULT_KEYS.BACKUP_DIR);
	}

	/**
	 * 
	 */
	public static String getTempDirAsString() {
		return getInstance().getValue(DEFAULT_KEYS.TEMP_DIR);
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
		String sfn = l.asIsoCode() + File.separator + getInstance().getValue(DEFAULT_KEYS.HEADING_IMAGE);
		String ffn = getStaticDir().getFullConfigFileUrl(sfn);
		return getSiteUrlAsString() + ffn;
	}
	
	/**
	 * 
	 */
	public static String getLogoImageAsUrlString(Language l) {
		String sfn = l.asIsoCode() + File.separator + getInstance().getValue(DEFAULT_KEYS.LOGO_IMAGE);
		String ffn = getStaticDir().getFullConfigFileUrl(sfn);
		return getSiteUrlAsString() + ffn;
	}
	
	/**
	 * 
	 */
	public static String getEmptyImageAsUrlString(Language l) {
		String sfn = l.asIsoCode() + File.separator + getInstance().getValue(DEFAULT_KEYS.EMPTY_IMAGE);
		String ffn = getStaticDir().getFullConfigFileUrl(sfn);
		return getSiteUrlAsString() + ffn;
	}

	public static String getDbDriverAsString()	{
		return getInstance().getValue(DEFAULT_KEYS.DB_DRIVER);
	}
	
	/**
	 * 
	 */
	public static String getDbConnectionAsString() {
		return getInstance().getValue(DEFAULT_KEYS.DB_CONNECTION);
	}
	
	/**
	 * 
	 */
	public static String getDbUserAsString() {
		return getInstance().getValue(DEFAULT_KEYS.DB_USER);
	}
	
	/**
	 * 
	 */
	public static String getDbPasswordAsString() {
		return getInstance().getValue(DEFAULT_KEYS.DB_PASSWORD);
	}

}
