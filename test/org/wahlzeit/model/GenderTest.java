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
package org.wahlzeit.model;

import junit.framework.*;

/**
 * 
 * @author dirk
 *
 */
public class GenderTest extends TestCase {

	/**
	 * 
	 * @param name
	 */
	public GenderTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void testGetFromInt() {
		assertTrue(Gender.getFromInt(Gender.UNDEFINED.asInt()) == Gender.UNDEFINED);
		assertTrue(Gender.getFromInt(Gender.MALE.asInt()) == Gender.MALE);
		assertTrue(Gender.getFromInt(Gender.FEMALE.asInt()) == Gender.FEMALE);
		assertTrue(Gender.getFromInt(Gender.OTHER.asInt()) == Gender.OTHER);
	}

	/**
	 * 
	 */
	public void testGetFromString() {
		assertTrue(Gender.getFromString(Gender.UNDEFINED.asString()).equals(Gender.UNDEFINED));
		assertTrue(Gender.getFromString(Gender.MALE.asString()).equals(Gender.MALE));
		assertTrue(Gender.getFromString(Gender.FEMALE.asString()).equals(Gender.FEMALE));
		assertTrue(Gender.getFromString(Gender.OTHER.asString()).equals(Gender.OTHER));
	}
	
	/**
	 * 
	 */
	public void testNonValidValues() {
		try {
			Gender.getFromInt(-1);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			Gender.getFromInt(4);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}
