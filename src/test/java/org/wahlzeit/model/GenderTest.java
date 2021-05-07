/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test cases for the Gender class.
 */
public class GenderTest {

	/**
	 *
	 */
	@Test
	public void testGetFromInt() {
		assertTrue(Gender.getFromInt(Gender.UNDEFINED.asInt()) == Gender.UNDEFINED);
		assertTrue(Gender.getFromInt(Gender.MALE.asInt()) == Gender.MALE);
		assertTrue(Gender.getFromInt(Gender.FEMALE.asInt()) == Gender.FEMALE);
		assertTrue(Gender.getFromInt(Gender.OTHER.asInt()) == Gender.OTHER);
	}

	/**
	 *
	 */
	@Test
	public void testGetFromString() {
		assertTrue(Gender.getFromString(Gender.UNDEFINED.asString()).equals(Gender.UNDEFINED));
		assertTrue(Gender.getFromString(Gender.MALE.asString()).equals(Gender.MALE));
		assertTrue(Gender.getFromString(Gender.FEMALE.asString()).equals(Gender.FEMALE));
		assertTrue(Gender.getFromString(Gender.OTHER.asString()).equals(Gender.OTHER));
	}

	/**
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeValueShouldThrowException() {
		Gender.getFromInt(-1);
	}

	/**
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void tooBigValueShouldThrowException() {
		Gender.getFromInt(4);
	}
}
