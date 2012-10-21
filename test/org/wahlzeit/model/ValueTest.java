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
 * @author dirkriehle
 *
 */
public class ValueTest extends TestCase {
	
	/**
	 * 
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ValueTest.class);
	}

	/**
	 * 
	 */
	public ValueTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void testUserStatus() {
		assertTrue(UserStatus.CREATED == UserStatus.getFromInt(0));
		assertTrue(UserStatus.CONFIRMED == UserStatus.getFromInt(1));
		assertTrue(UserStatus.DISABLED == UserStatus.getFromInt(2));

		UserStatus us = UserStatus.CREATED;
		assertTrue(us.asInt() == 0);
		assertTrue(!us.isConfirmed());
		
		UserStatus us2 = us.asConfirmed();
		assertTrue(us != us2);
		assertTrue(us2.isConfirmed());
		
		UserStatus us3 = us2.asDisabled();
		assertTrue(us2 != us3);
		assertTrue(us3.isCreated());
		assertTrue(us3.isConfirmed());
		assertTrue(us3.isDisabled());
		
		UserStatus us4 = us.asDisabled();
		assertTrue(us4.isDisabled());
		assertTrue(us3 != us4);
		
		us3 = us3.asEnabled();
		assertTrue(us3.isConfirmed());
		assertTrue(!us3.isDisabled());
		
		us4 = us4.asEnabled();
		assertTrue(!us4.isConfirmed());
		assertTrue(!us4.isDisabled());
		
		us4 = us4.asConfirmed();
		assertTrue(us3 == us4);
	}

	/**
	 * 
	 */
	public void testPhotoStatus() {
		assertTrue(PhotoStatus.VISIBLE == PhotoStatus.getFromInt(0));
		assertTrue(PhotoStatus.INVISIBLE == PhotoStatus.getFromInt(1));
		assertTrue(PhotoStatus.FLAGGED2 == PhotoStatus.getFromInt(3));
		assertTrue(PhotoStatus.MODERATED == PhotoStatus.getFromInt(4));
		assertTrue(PhotoStatus.MODERATED3 == PhotoStatus.getFromInt(6));

		PhotoStatus ps = PhotoStatus.VISIBLE;
		assertTrue(ps.asInt() == 0);
		
		PhotoStatus ps2 = ps.asInvisible(true);
		assertTrue(ps != ps2);
		assertTrue(ps2.isInvisible());
		assertTrue(!ps2.isFlagged());
		assertTrue(!ps2.isModerated());
		assertTrue(!ps2.isDisplayable());
		
		PhotoStatus ps3 = ps2.asFlagged(true);
		assertTrue(ps2 != ps3);
		assertTrue(ps3.isInvisible());
		assertTrue(ps3.isFlagged());
		assertTrue(!ps3.isModerated());
		assertTrue(!ps3.isDisplayable());
		
		PhotoStatus ps3b = PhotoStatus.FLAGGED;
		assertTrue(ps3 != ps3b);
		assertTrue(!ps3b.isInvisible());
		assertTrue(ps3b.isFlagged());
		assertTrue(!ps3b.isModerated());
		assertTrue(!ps3b.isDisplayable());
		
		PhotoStatus ps2b = ps3b.asInvisible(true);
		assertTrue(ps2b != ps3b);
		
		PhotoStatus ps4 = ps3.asModerated(true);
		assertTrue(ps4.isInvisible());
		assertTrue(ps4.isFlagged());
		assertTrue(ps4.isModerated());
		assertTrue(!ps4.isDisplayable());
	}
	
	/**
	 * 
	 */
	public void testObjectId() {
		PhotoId test = PhotoId.getNextId();

		int testInt = test.asInt();
		assertTrue(test == PhotoId.getId(testInt));

		String testString = test.asString();
		assertTrue(test == PhotoId.getId(testString));
	}

}
