/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.webparts;

import java.util.*;
import java.io.*;

import org.wahlzeit.services.*;

/**
 * The WebPartTemplateService creates WebPartTemplates upon request by reading them from disk.
 * It requires configuration with a template directory and uses the following naming convention: tmplDir/language/part-type/part-name.html
 */
public class WebPartTemplateService {

	/**
	 * 
	 */
	protected static final WebPartTemplateService instance = new WebPartTemplateService();

	/**
	 * Convenience method...
	 */
	public static WebPartTemplateService getInstance() {
		return instance;
	}

	/**
	 *
	 */
	protected Map<String, WebPartTemplate> templates = new HashMap<String, WebPartTemplate>();

	/**
	 * 
	 */
	protected ConfigDir templatesDir = null;
	
	/**
	 *
	 */
	protected WebPartTemplateService() {
		// do nothing	
	}
	
	/**
	 * 
	 */
	public ConfigDir getTemplatesDir() {
		return templatesDir;
	}
	
	/**
	 * 
	 */
	public void setTemplatesDir(ConfigDir newTemplatesDir) {
		templatesDir = newTemplatesDir;
	}

	/**
	 * 
	 */
	public WebPartTemplate getTemplate(String lang, String name) {
		String shortName = lang + File.separator + name;
		WebPartTemplate result = templates.get(shortName);

		if (result == null) {
			try {
				loadTemplate(shortName);
				result = templates.get(shortName);
			} catch (IOException ioex) {
				SysLog.logThrowable(ioex);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	protected void loadTemplate(String shortName) throws IOException {
		WebPartTemplate template = new WebPartTemplate(shortName);
		String fileName = getTemplatesDir().getAbsoluteConfigFileName(shortName + ".html");
		File file = new File(fileName);
		SysLog.logSysInfo("file name", fileName, "opened HTML template file");

		FileReader reader = null;
		try {
			reader = new FileReader(file);
			
			// @FIXME: Assumes files are always < 50000 bytes
			char[] readBuffer = new char[50000];
			int status = reader.read(readBuffer);

			SysLog.logSysInfo("file size", Integer.toString(status), "read HTML template file");
			
			if (status != -1) {
				String source = new String(readBuffer, 0, status);
				template.initialize(source);
				SysLog.logCreatedObject("WebPartTmpl", shortName);
			}

			templates.put(shortName, template);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
}
