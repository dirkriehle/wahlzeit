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

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author dirkriehle
 *
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

