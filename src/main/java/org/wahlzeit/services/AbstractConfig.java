/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.util.*;
import java.io.*;

/**
 * A generic implementation to manage simple key/value store.
 * Clients can get and set individual values; they can also load properties files.
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
