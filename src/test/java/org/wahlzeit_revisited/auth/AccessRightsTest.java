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
package org.wahlzeit_revisited.auth;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * All test cases of the class {@link AccessRights}.
 */
public class AccessRightsTest {

	// test cases that cover valid behavior

	@Test
	public void getFromIntShouldMatchEnums() {
		assertSame(AccessRights.NONE, AccessRights.getFromInt(0));
		assertSame(AccessRights.GUEST, AccessRights.getFromInt(1));
		assertSame(AccessRights.USER, AccessRights.getFromInt(2));
		assertSame(AccessRights.MODERATOR, AccessRights.getFromInt(3));
		assertSame(AccessRights.ADMINISTRATOR, AccessRights.getFromInt(4));
	}

	@Test
	public void getFromStringShouldMatchEnums() {
		assertSame(AccessRights.NONE, AccessRights.getFromString("none"));
		assertSame(AccessRights.GUEST, AccessRights.getFromString("guest"));
		assertSame(AccessRights.USER, AccessRights.getFromString("user"));
		assertSame(AccessRights.MODERATOR, AccessRights.getFromString("moderator"));
		assertSame(AccessRights.ADMINISTRATOR, AccessRights.getFromString("administrator"));
	}

	@Test
	public void testHasRights() {
		assertTrue(AccessRights.hasRights(AccessRights.ADMINISTRATOR, AccessRights.MODERATOR));
		assertTrue(AccessRights.hasRights(AccessRights.MODERATOR, AccessRights.USER));
		assertTrue(AccessRights.hasRights(AccessRights.USER, AccessRights.GUEST));
		assertTrue(AccessRights.hasRights(AccessRights.GUEST, AccessRights.NONE));
		assertTrue(AccessRights.hasRights(AccessRights.NONE, AccessRights.NONE));
		// hasRights() implements a transitive relation -> all cases covered
		assertFalse(AccessRights.hasRights(AccessRights.NONE, AccessRights.GUEST));
		assertFalse(AccessRights.hasRights(AccessRights.GUEST, AccessRights.USER));
		assertFalse(AccessRights.hasRights(AccessRights.USER, AccessRights.MODERATOR));
		assertFalse(AccessRights.hasRights(AccessRights.MODERATOR, AccessRights.ADMINISTRATOR));
	}


	// test cases that cover behavior in case of an error

	@Test(expected = IllegalArgumentException.class)
	public void negativeIndexShouldThrowException() {
		AccessRights.getFromInt(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooBigIndexShouldThrowException() {
		AccessRights.getFromInt(5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullStringShouldThrowException() {
		AccessRights.getFromString(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void noneStringShouldThrowException() {
		AccessRights.getFromString("NonE");
	}

	@Test(expected = IllegalArgumentException.class)
	public void CapitalGuestShouldThrowException() {
		AccessRights.getFromString("Guest");
	}

	@Test(expected = IllegalArgumentException.class)
	public void CapitalUserShouldThrowException() {
		AccessRights.getFromString("User");
	}

	@Test(expected = IllegalArgumentException.class)
	public void CapitalModeratorShouldThrowException() {
		AccessRights.getFromString("Moderator");
	}

	@Test(expected = IllegalArgumentException.class)
	public void CapitalAdministratorShouldThrowException() {
		AccessRights.getFromString("Administrator");
	}
}
