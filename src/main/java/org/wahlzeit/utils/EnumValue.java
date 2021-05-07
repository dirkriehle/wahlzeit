/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.utils;

/**
 * An EnumValue is a an enum wrapper that makes handling enums easier.
 */
public interface EnumValue {
	
	/**
	 * 
	 * @methodtype conversion
	 */
	public int asInt();
	
	/**
	 * 
	 * @methodtype conversion
	 */
	public String asString();
	
	/**
	 * 
	 * @methodtype get
	 */
	public EnumValue[] getAllValues(); 
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getTypeName();
	
}
