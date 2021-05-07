/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
	public static String safelyReadFileAsString(String fileName) {
		String result = "";
		
		File file = new File(fileName);
		SysLog.logSysInfo("file name", fileName, "opened file for safe string reading");

		FileReader reader = null;
		try {
			reader = new FileReader(file);
			
			// @FIXME Assumes files are always < 50000 bytes
			char[] readBuffer = new char[50000];
			int status = reader.read(readBuffer);

			SysLog.logSysInfo("file size", Integer.toString(status), "read file");
			
			if (status != -1) {
				result = new String(readBuffer, 0, status);
			}
		} catch(IOException ioex) {
			SysLog.logThrowable(ioex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioex) {
					SysLog.logThrowable(ioex);
				}
			}
		}
		
		return result;
	}

}
