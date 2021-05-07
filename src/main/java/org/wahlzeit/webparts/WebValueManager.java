/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.webparts;

import java.util.*;

/**
 * The WebValueManager provides (and creates on-demand) WebValues.
 * It constructs the data for a WebValue from the provided arguments.
 */
public class WebValueManager {

	/**
	 * 
	 */
	protected static final WebValueManager instance = new WebValueManager();

	/**
	 * Convenience method...
	 */
	public static WebValueManager getInstance() {
		return instance;
	}

	/**
	 *
	 */
	protected Map<String, WebValue> webValues = new HashMap<String, WebValue>();

	/**
	 *
	 */
	protected WebValueManager() {
		// do nothing	
	}

	/**
	 * 
	 */
	public WebValue getWebValue(Class javaClass, String value) {
		WebValue result = null;
		if ((javaClass != null) && (value != null)) {
			String key = javaClass.getName() + "#" + value;
			result = webValues.get(key);
			if (result == null) {
				result = createWebValue(javaClass, value);
				if (result != null) {
					webValues.put(key, result);
				}	
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	protected WebValue createWebValue(Class javaClass, String value) {
		String className = javaClass.getSimpleName();
		String checkedKey = value + className + "Checked";
		String selectedKey = value + className + "Selected";
		return new WebValue(checkedKey, selectedKey);
	}
}
