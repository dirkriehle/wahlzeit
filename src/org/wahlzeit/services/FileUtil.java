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
 * Some convenience methods for dealing with the file system.
 * 
 * @author dirkriehle
 *
 */
public class FileUtil {
	
	/**
	 * 
	 */
	public static String safelyReadFileAsString(String fileName) throws IOException {
		String result = "";
		
		File file = new File(fileName);
		SysLog.logValueWithInfo("file name", fileName, "opened file for safe string reading");

		FileReader reader = null;
		try {
			reader = new FileReader(file);
			
			// @FIXME: Assumes files are always < 50000 bytes
			char[] readBuffer = new char[50000];
			int status = reader.read(readBuffer);

			SysLog.logValueWithInfo("file size", Integer.toString(status), "read file");
			
			if (status != -1) {
				result = new String(readBuffer, 0, status);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		return result;
	}

}
