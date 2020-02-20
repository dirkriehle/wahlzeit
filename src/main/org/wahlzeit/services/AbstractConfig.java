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

import java.util.*;
import java.io.*;

/**
 * A generic implementation to manage simple key/value store.
 * Clients can get and set individual values; they can also load properties files.
 *
 * @author dirkriehle
 *
 */
public abstract class AbstractConfig implements Configuration {

	/**
	 * 
	 */
	protected Map<String, String> store = new HashMap<String, String>();
	
	/**
	 * 
	 */
	public boolean hasKey(String key) {
		return store.containsKey(key);
	}
	
	/**
	 * 
	 */
	public String getValue(String key) throws IllegalArgumentException {
		assertHasKey(key);
		return doGetValue(key);
	}
	
	/**
	 * 
	 */
	protected final String doGetValue(String key) {
		return store.get(key);
	}
	
	/**
	 * 
	 */
	public void setValue(String key, String value) throws IllegalArgumentException {
		assertHasKey(key);
		doSetValue(key, value);
	}
	
	/**
	 * 
	 */
	protected final void doSetValue(String key, String value) {
		store.put(key, value);
	}

	/**
	 * @methodtype assertion
	 */
	protected final void assertHasKey(String key) throws IllegalArgumentException {
		if (!store.containsKey(key)) {
			throw new IllegalArgumentException("unknown key: " + key);
		}
	}

	/**
	 * 
	 */
	public void loadProperties(String fileName) throws IllegalArgumentException, IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new IllegalArgumentException("file does not exist: " + fileName);
		}
		
		loadProperties(file);
	}
	
	/**
	 * 
	 */
	public void loadProperties(File file) throws IOException {
		Properties input = new Properties();

		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			input.load(stream);

			for (Enumeration e = input.propertyNames(); e.hasMoreElements(); ) {
				String key = (String) e.nextElement();
				doSetValue(key, input.getProperty(key));
			}
		} finally {
			if (stream != null) {
				stream.close();				
			}
		}
	}
}
