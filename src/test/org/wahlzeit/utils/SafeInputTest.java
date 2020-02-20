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
public class SafeInputTest extends TestCase {

	/**
	 * 
	 * @param name
	 */
	public SafeInputTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void testIsLegalUserName() {
		assertTrue(StringUtil.isLegalUserName("abcd"));
		assertTrue(StringUtil.isLegalUserName("ab cd"));
		assertTrue(StringUtil.isLegalUserName("._ _. !@#$%^*()"));
		
		assertFalse(StringUtil.isLegalUserName("a<>b"));
		assertFalse(StringUtil.isLegalUserName("a&b"));
		assertFalse(StringUtil.isLegalUserName("a\\b"));
		assertFalse(StringUtil.isLegalUserName("a\"b"));
		assertFalse(StringUtil.isLegalUserName("a\\'b"));
		assertFalse(StringUtil.isLegalUserName("a`b"));
		assertFalse(StringUtil.isLegalUserName("a\\`b"));
		
		assertFalse(StringUtil.isLegalUserName(""));
	}

	/**
	 * 
	 */
	public void testIsLegalPassword() {
		assertTrue(StringUtil.isLegalPassword("abcd"));
		assertTrue(StringUtil.isLegalPassword("ab cd"));
		assertTrue(StringUtil.isLegalPassword("._ _. !@#$%^*()"));
		
		assertFalse(StringUtil.isLegalPassword(""));
	}

	/**
	 * 
	 */
	public void testIsValidStrictEmailAddress() {
		assertTrue(StringUtil.isValidStrictEmailAddress("admin@wahlzeit.org"));
		assertTrue(StringUtil.isValidStrictEmailAddress("a@b2.com"));
		assertTrue(StringUtil.isValidStrictEmailAddress("a@b2.de"));

		assertFalse(StringUtil.isValidStrictEmailAddress("a b@b2.com"));
		assertFalse(StringUtil.isValidStrictEmailAddress("@."));
		assertFalse(StringUtil.isValidStrictEmailAddress("a@be.c"));		
		assertFalse(StringUtil.isValidStrictEmailAddress("a@.com"));		
		assertFalse(StringUtil.isValidStrictEmailAddress("a@domain.c"));

		assertFalse(StringUtil.isValidStrictEmailAddress("a<!>@b2.com"));
		assertFalse(StringUtil.isValidStrictEmailAddress("a`@b2.com"));
		assertFalse(StringUtil.isValidStrictEmailAddress("a\'@b2.com"));
		assertFalse(StringUtil.isValidStrictEmailAddress("a\\@b2.com"));
		assertFalse(StringUtil.isValidStrictEmailAddress("a\\'@b2.com"));
		assertFalse(StringUtil.isValidStrictEmailAddress("a\\\\@b2.com"));
	}

	/**
	 * 
	 */
	public void testIsLegalCharacterName() {
		// same as user name
	}

	/**
	 * 
	 */
	public void testIsLegalSeriesName() {
		// same as user name
	}

	/**
	 * 
	 */
	public void testIsLegalTagsString() {
		// same as user name
	}

}

