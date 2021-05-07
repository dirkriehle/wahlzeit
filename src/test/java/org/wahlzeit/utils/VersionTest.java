/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for the Version class.
 */
public class VersionTest {

	/**
	 *
	 */
	@Test
	public void testValidGetVersionAsInt() {
		assertTrue(Version.getVersionAsInt("0.0.0") == 0);
		assertTrue(Version.getVersionAsInt("0.0.1") == 1);
		assertTrue(Version.getVersionAsInt("0.1.0") == 1000);
		assertTrue(Version.getVersionAsInt("1.0.0") == 1000000);
		assertTrue(Version.getVersionAsInt("12.33.99") == 12 * 1000000 + 33 * 1000 + 99);
	}

	/**
	 *
	 */
	@Test
	public void testInvalidGetVersionAsInt() {
		doTestInvalidGetVersionAsInt("..");
		doTestInvalidGetVersionAsInt("a.b.c");
		doTestInvalidGetVersionAsInt("-.,.\"");
		doTestInvalidGetVersionAsInt("7.4.1.1");
	}

	/**
	 *
	 */
	protected void doTestInvalidGetVersionAsInt(String invalidString) {
		try {
			Version.getVersionAsInt(invalidString);
			fail(".getVersionAsInt(\"" + invalidString + "\") should throw NumberFormatException.");
		} catch (NumberFormatException e) {
			// expected case
		} catch (Exception e) {
			fail(".getVersionAsInt(\"" + invalidString + "\") should throw NumberFormatException not " +
					e.getClass().toString());
		}
	}

	/**
	 *
	 */
	@Test
	public void testValidGetMajorVersionAsInt() {
		assertTrue(Version.getMajorNumberAsInt("12.33.15") == 12);
	}

	/**
	 *
	 */
	@Test
	public void testInvalidGetMajorVersionAsInt() {
		doTestInvalidMajorVersionAsInt("1,2.2.3");
		doTestInvalidMajorVersionAsInt("a.2.3");
		doTestInvalidMajorVersionAsInt(".2.3");
	}

	/**
	 *
	 */
	protected void doTestInvalidMajorVersionAsInt(String invalidString) {
		try {
			Version.getMajorNumberAsInt(invalidString);
			fail(".getMajorNumberAsInt(\"" + invalidString + "\") should throw NumberFormatException.");
		} catch (NumberFormatException e) {
			// expected case
		} catch (Exception e) {
			fail(".getMajorNumberAsInt(\"" + invalidString + "\") should throw NumberFormatException not " +
					e.getClass().toString());
		}
	}

	/**
	 *
	 */
	@Test
	public void testGetMinorVersionAsInt() {
		assertTrue(Version.getMinorNumberAsInt("12.33.15") == 33);
	}

	/**
	 *
	 */
	@Test(expected = StringIndexOutOfBoundsException.class)
	public void testGetMinorNumberAsIntWithBrokenString() {
		Version.getMinorNumberAsInt(".");
	}

	/**
	 *
	 */
	@Test(expected = NumberFormatException.class)
	public void testGetMinorNumberAsIntWithMissingMinorNumber() {
		Version.getMinorNumberAsInt("1..3");
	}

	/**
	 *
	 */
	@Test(expected = NumberFormatException.class)
	public void testGetMinorNumberAsIntWithCharacter() {
		Version.getMinorNumberAsInt("1.b.3");
	}

	/**
	 *
	 */
	@Test(expected = NumberFormatException.class)
	public void testGetMinorNumberAsIntWithFloatingPointNumber() {
		Version.getMinorNumberAsInt("1.2,2.3");
	}

	/**
	 *
	 */
	@Test
	public void testGetRevisionVersionAsInt() {
		assertTrue(Version.getRevisionNumberAsInt("12.33.15") == 15);
	}

	/**
	 *
	 */
	@Test(expected = NumberFormatException.class)
	public void testGetRevisionVersionAsIntWithMissingRevision() {
		Version.getRevisionNumberAsInt("12.33.");
	}

	/**
	 *
	 */
	@Test(expected = NumberFormatException.class)
	public void testGetRevisionVersionAsIntWithCharacter() {
		Version.getRevisionNumberAsInt("12.33.l");
	}

	/**
	 *
	 */
	@Test(expected = NumberFormatException.class)
	public void testGetRevisionVersionAsIntWithEmptyString() {
		Version.getRevisionNumberAsInt("");
	}

}
