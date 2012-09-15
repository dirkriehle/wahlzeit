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
		rootPath = dirName;
		defaultPath = dirName + File.separator + "default";
		customPath = dirName + File.separator + "custom";
	}
	
	/**
	 * 
	 */
	public String getRootPath() {
		return rootPath;
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
	 */
	public String getDefaultConfigFileName(String shortFileName) {
		return defaultPath + File.separator + shortFileName;
	}
	
	/**
	 * 
	 */
	public String getCustomConfigFileName(String shortFileName) {
		return customPath + File.separator + shortFileName;
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
