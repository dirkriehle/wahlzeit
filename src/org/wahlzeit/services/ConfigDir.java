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

import org.wahlzeit.utils.StringUtil;

/**
 * 
 * @author dirkriehle
 *
 */
public class ConfigDir {
	
	/**
	 * 
	 */
	protected String rootPath = "";
	protected String defaultPath = "";
	protected String customPath = "";
	
	/**
	 * 
	 */
	public ConfigDir(String dirName) {
		rootPath = "Documents/Workspace/wahlzeit/web/" + dirName;
		defaultPath = rootPath + File.separator + "default";
		customPath = rootPath + File.separator + "custom";
	}
	
	/**
	 * 
	 */
	public String getRootPath() {
		return rootPath;
	}
	
	/**
	 * @return the root path as URL fragment
	 */
	public String getRootUrl() {
		return StringUtil.pathAsUrlString(getRootPath());
	}

	/**
	 * 
	 */
	public String getFullConfigFileName(String shortFileName) {
		String customFileName = getCustomConfigFileName(shortFileName);
		if (doesFileExist(customFileName)) {
			return customFileName;
		}

		return getDefaultConfigFileName(shortFileName);
	}
	
	/**
	 * 
	 * @param shortFileName
	 * @return the full file path as URL
	 */
	public String getFullConfigFileUrl(String shortFileName) {
		return StringUtil.pathAsUrlString(getFullConfigFileName(shortFileName));
	}

	/**
	 *
	 */
	public String getDefaultConfigFileName(String shortFileName) {
		return defaultPath + File.separator + shortFileName;
	}

	/**
	 *
	 * @param shortFileName
	 * @return the default file path as URL
	 */
	public String getDefaultConfigFileUrl(String shortFileName) {
		return StringUtil.pathAsUrlString(getDefaultConfigFileName(shortFileName));
	}

	/**
	 *
	 */
	public String getCustomConfigFileName(String shortFileName) {
		return customPath + File.separator + shortFileName;
	}
	
	/**
	 * 
	 * @param shortFileName
	 * @return the custom file path as URL
	 */
	public String getCustomConfigFileUrl(String shortFileName) {
		return StringUtil.pathAsUrlString(getCustomConfigFileName(shortFileName));
	}

	/**
	 *
	 */
	public boolean hasDefaultFile(String shortFileName) {
		return doesFileExist(defaultPath + File.separator + shortFileName);
	}
	
	/**
	 * 
	 */
	public boolean hasCustomFile(String shortFileName) {
		return doesFileExist(customPath + File.separator + shortFileName);
	}
	
	/**
	 * 
	 */
	protected boolean doesFileExist(String fullFileName) {
		return new File(fullFileName).exists();
	}

}
