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

import java.lang.reflect.Field;

/**
 * A simple abstract implementation of Persistent with write count and dirty bit.
 * Also defines (but does not use) the field "ID" for subclass use.
 * 
 * @author dirkriehle
 *
 */
public abstract class DataObject implements Persistent {
	
	/**
	 * Not used in the class but needed by broad array of subclasses
	 */
	public static final String ID = "id";

	/**
	 * 
	 */
	protected transient int writeCount = 0;
	
	/**
	 * 
	 */
	public final boolean isDirty() {
		return writeCount != 0;
	}
	
	/**
	 * 
	 */
	public final void resetWriteCount() {
		writeCount = 0;
	}
	
	/**
	 * 
	 */
	public final void incWriteCount() {
		writeCount++;
	}
	
	/**
	 * 
	 */
	public final void touch() {
		incWriteCount();
	}
			
	/**
	 * 
	 */
	public boolean setAttributeValue(String attributeName, Object value) {
		Field f = null;
		try {
			f = this.getClass().getDeclaredField(attributeName);
			f.setAccessible(true); // required if field is not normally accessible
		} catch (SecurityException e1) {
			SysLog.logError(e1.getMessage());
			return false;
		} catch (NoSuchFieldException e1) {
			SysLog.logError(e1.getMessage());
			return false;
		}

		try {
			f.set(this, value);
		} catch (Exception e) {
			SysLog.logError(e.getMessage());
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 */
	public Object getAttributeValue(String attributeName){
		Field f = null;
		Object res = null;
		try {
			f = this.getClass().getDeclaredField(attributeName);
			f.setAccessible(true);
		} catch (Exception e) {
			SysLog.logError(e.getMessage());
			return null;
		}
		
		try {
			res = f.get(this);
		} catch (Exception e) {
			SysLog.logError(e.getMessage());
			return null;
		}		
		return res;
	}
	
}
