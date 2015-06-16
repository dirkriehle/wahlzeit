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

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author dirkriehle
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
            fail(".getVersionAsInt(\"" + invalidString + "\") should throw NumberFormatException not " + e.getClass().toString());
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
            fail(".getMajorNumberAsInt(\"" + invalidString + "\") should throw NumberFormatException not " + e.getClass().toString());
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
