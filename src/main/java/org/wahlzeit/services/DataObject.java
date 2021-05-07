/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

/**
 * A simple abstract implementation of Persistent with write count and dirty bit.
 * Also defines (but does not use) the field "ID" for subclass use.
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

}
