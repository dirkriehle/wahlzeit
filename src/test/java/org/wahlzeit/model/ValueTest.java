/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test cases for a variety of value object classes.
 */
public class ValueTest {

	/**
	 *
	 */
	@Test
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
	@Test
	public void testPhotoStatus() {
		assert (PhotoStatus.VISIBLE == PhotoStatus.getFromInt(0));
		assert (PhotoStatus.INVISIBLE == PhotoStatus.getFromInt(1));
		assert (PhotoStatus.FLAGGED2 == PhotoStatus.getFromInt(3));
		assert (PhotoStatus.MODERATED == PhotoStatus.getFromInt(4));
		assert (PhotoStatus.MODERATED3 == PhotoStatus.getFromInt(6));

		PhotoStatus ps = PhotoStatus.VISIBLE;
		assert (ps.asInt() == 0);

		PhotoStatus ps2 = ps.asInvisible(true);
		assert (ps != ps2);
		assert (ps2.isInvisible());
		assert (!ps2.isFlagged());
		assert (!ps2.isModerated());
		assert (!ps2.isDisplayable());

		PhotoStatus ps3 = ps2.asFlagged(true);
		assert (ps2 != ps3);
		assert (ps3.isInvisible());
		assert (ps3.isFlagged());
		assert (!ps3.isModerated());
		assert (!ps3.isDisplayable());

		PhotoStatus ps3b = PhotoStatus.FLAGGED;
		assert (ps3 != ps3b);
		assert (!ps3b.isInvisible());
		assert (ps3.isFlagged());
		assert (!ps3.isModerated());
		assert (!ps3.isDisplayable());

		PhotoStatus ps2b = ps3b.asInvisible(true);
		assert (ps2b != ps3b);

		PhotoStatus ps4 = ps3.asModerated(true);
		assert (ps4.isInvisible());
		assert (ps4.isFlagged());
		assert (ps4.isModerated());
		assert (!ps4.isDisplayable());
	}

	/**
	 *
	 */
	@Test
	public void testObjectId() {
		PhotoId test = PhotoId.getNextId();

		int testInt = test.asInt();
		assert (test == PhotoId.getIdFromInt(testInt));

		String testString = test.asString();
		assert (test == PhotoId.getIdFromString(testString));
	}

}
