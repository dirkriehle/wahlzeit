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

package org.wahlzeit.utils;

import junit.framework.*;

/**
 * 
 * @author dirkriehle
 * 
 */
public class VersionTest extends TestCase {

	/**
	 * 
	 */
	public void testGetVersionAsInt() {
		assertTrue(Version.getVersionAsInt("0.0.0") == 0);
		assertTrue(Version.getVersionAsInt("0.0.1") == 1);
		assertTrue(Version.getVersionAsInt("0.1.0") == 1000);
		assertTrue(Version.getVersionAsInt("1.0.0") == 1000000);
		assertTrue(Version.getVersionAsInt("12.33.99") == 12 * 1000000 + 33 * 1000 + 99);
		//FIXME add exception test cases
	}
	
	/**
	 * 
	 */
	public void testGetMajorVersionAsInt() {
		assertTrue(Version.getMajorNumberAsInt("12.33.15") == 12);		
		//FIXME add exception test cases
	}

	/**
	 * 
	 */
	public void testGetMinorVersionAsInt() {
		assertTrue(Version.getMinorNumberAsInt("12.33.15") == 33);		
		//FIXME add exception test cases
	}

	/**
	 * 
	 */
	public void testGetRevisionVersionAsInt() {
		assertTrue(Version.getRevisionNumberAsInt("12.33.15") == 15);		
		//FIXME add exception test cases
	}

}
