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

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for the FlagReason class.
 */
public class FlagReasonTest {

	/**
	 *
	 */
	private Map<FlagReason, TestEntry> entries;

	/**
	 *
	 */
	@Before
	public void setUp() {
		entries = new HashMap<FlagReason, TestEntry>();

		addEntry(FlagReason.MISMATCH, "mismatch", 0);
		addEntry(FlagReason.OFFENSIVE, "offensive", 1);
		addEntry(FlagReason.COPYRIGHT, "copyright", 2);
		addEntry(FlagReason.OTHER, "other", 3);
	}

	/**
	 * @param reason
	 * @param str
	 * @param i
	 */
	protected void addEntry(FlagReason reason, String str, int i) {
		entries.put(reason, new TestEntry(str, i));
	}

	/**
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void tooBigIndexShouldCauseException() {
		FlagReason.getFromInt(FlagReason.MAX_VALUE + 1);
	}

	/**
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeIndexShouldCauseException() {
		FlagReason.getFromInt(-1);
	}

	/**
	 *
	 */
	@Test
	public void testInvalidGetFromString() {
		failGetFromString(null);
		failGetFromString("");
		failGetFromString("some_random_string");
		failGetFromString("MiSmAtCh");
	}

	/**
	 * @param invalidString
	 */
	protected void failGetFromString(String invalidString) {
		try {
			FlagReason.getFromString(invalidString);
			fail(".getFromString(" + invalidString + ") should throw IllegalArgumentException.");
		} catch (IllegalArgumentException expectedException) {
		} catch (Exception unexpectedException) {
			fail(".getFromString(" + invalidString + ") should throw IllegalArgumentException not " +
					unexpectedException.getClass().toString());
		}
	}

	/**
	 *
	 */
	@Test
	public void testGetFromInt() {
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getKey(), FlagReason.getFromInt(entry.getValue().getInt()));
		}
	}

	/**
	 *
	 */
	@Test
	public void testGetFromString() {
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getKey(), FlagReason.getFromString(entry.getValue().getString()));
		}
	}

	/**
	 *
	 */
	@Test
	public void testAsInt() {
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getValue().getInt(), entry.getKey().asInt());
		}
	}

	/**
	 *
	 */
	@Test
	public void testAsString() {
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getValue().getString(), entry.getKey().asString());
		}
	}

	/**
	 *
	 */
	@Test
	public void testGetValues() {
		List<FlagReason> values = Arrays.asList(FlagReason.values());

		assertNotNull(values);
		assertEquals(FlagReason.MAX_VALUE + 1, values.size());

		for (FlagReason flagReason : entries.keySet()) {
			assertTrue(values.contains(flagReason));
		}
	}

	/**
	 *
	 */
	private class TestEntry {
		private String str;
		private int i;

		public TestEntry(String str, int i) {
			this.str = str;
			this.i = i;
		}

		public String getString() {
			return str;
		}

		public int getInt() {
			return i;
		}
	}
}
