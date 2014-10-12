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

public class PhotoFilterTest extends TestCase {

	/**
	 * 
	 * @param name
	 */
	public PhotoFilterTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void testConstructor() {
		PhotoFilter pf = new PhotoFilter();
		assertNotNull(pf);

		// Check properties after creation
		assertEquals("", pf.userName);
		assertEquals(Tags.EMPTY_TAGS, pf.tags);

		assertEquals(0, pf.processedPhotoIds.size());
		assertEquals(0, pf.displayablePhotoIds.size());
	}

	/**
	 * 
	 */
	public void testStaticProperties() {
		assertEquals("userName", PhotoFilter.USER_NAME);
		assertEquals("tags", PhotoFilter.TAGS);
	}

	/**
	 * 
	 */
	public void testClear() {
		PhotoFilter pf = new PhotoFilter();
		pf.setUserName("test");
		assertEquals("test", pf.getUserName());

		pf.setTags(new Tags("test"));
		assertEquals("test", pf.getTags().asString());

		// @FIXME
		// pf.displayablePhotoIds.add(new PhotoId(3));
		// assertTrue(pf.displayablePhotoIds.size() == 1);

		pf.processedPhotoIds.add(new PhotoId(2));
		assertTrue(pf.processedPhotoIds.size() == 1);

		pf.clear();

		assertEquals("", pf.getUserName());
		assertEquals(Tags.EMPTY_TAGS, pf.getTags());
		assertTrue(pf.displayablePhotoIds.isEmpty());
		assertTrue(pf.processedPhotoIds.isEmpty());
	}

}
