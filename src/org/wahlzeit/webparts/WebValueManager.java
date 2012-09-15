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

package org.wahlzeit.webparts;

import java.util.*;

/**
 * The WebValueManager provides (and creates on-demand) WebValues.
 * It constructs the data for a WebValue from the provided arguments.
 * 
 * @author dirkriehle
 *
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
