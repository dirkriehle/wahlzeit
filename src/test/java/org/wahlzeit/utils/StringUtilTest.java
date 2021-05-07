/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for the StringUtil class.
 */
public class StringUtilTest {

	/**
	 *
	 */
	@Test
	public void testPathAsUrlString() {
		String path1 = "folder" + File.separator + "subfolder" + File.separator + "file.test";
		String url1 = StringUtil.pathAsUrlString(path1);
		assertEquals("folder/subfolder/file.test", url1);

		String path2 = File.separator + "folder" + File.separator + "sub123 fol_der_" + File.separator;
		String url2 = StringUtil.pathAsUrlString(path2);
		assertEquals("/folder/sub123 fol_der_/", url2);

		assertEquals("/", StringUtil.pathAsUrlString(File.separator));
	}

}

