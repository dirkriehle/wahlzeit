/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.io.*;

/**
 * An interface that manages a simple key/value store.
 * Clients can get and set individual values; they can also load properties files.
 * Key names are configured during initialization and can't be changed afterwards.
 * Hence, any access using an unknown key leads to an IllegalArgumentException.
 */
public interface Configuration {

	/**
	 * 
	 */
	public boolean hasKey(String key);

	/**
	 * 
	 */
	public String getValue(String key) throws IllegalArgumentException; 
		
	/**
	 * 
	 */
	public void setValue(String key, String value) throws IllegalArgumentException;
	
	/**
	 * 
	 */
	public void loadProperties(String fileName) throws IllegalArgumentException, IOException;	

	/**
	 * 
	 */
	public void loadProperties(File file) throws IOException;	

}
