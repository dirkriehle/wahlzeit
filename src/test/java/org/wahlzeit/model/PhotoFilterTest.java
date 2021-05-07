/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package org.wahlzeit.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PhotoFilterTest {

	private PhotoFilter photoFilter;

	@Before
	public void initPhotoFilter() {
		photoFilter = new PhotoFilter();
	}

	/**
	 *
	 */
	@Test
	public void testConstructor() {
		assertNotNull(photoFilter);

		// Check properties after creation
		assertEquals("", photoFilter.userName);
		assertEquals(Tags.EMPTY_TAGS, photoFilter.tags);

		assertEquals(0, photoFilter.processedPhotoIds.size());
		assertEquals(0, photoFilter.displayablePhotoIds.size());
	}

	/**
	 *
	 */
	@Test
	public void testStaticProperties() {
		assertEquals("userName", PhotoFilter.USER_NAME);
		assertEquals("tags", PhotoFilter.TAGS);
	}

	/**
	 *
	 */
	@Test
	public void testClear() {
		photoFilter.setUserName("test");
		assertEquals("test", photoFilter.getUserName());

		photoFilter.setTags(new Tags("test"));
		assertEquals("test", photoFilter.getTags().asString());

		photoFilter.displayablePhotoIds.add(new PhotoId(3));
		assertTrue(photoFilter.displayablePhotoIds.size() == 1);

		photoFilter.processedPhotoIds.add(new PhotoId(2));
		assertTrue(photoFilter.processedPhotoIds.size() == 1);

		photoFilter.clear();

		assertEquals("", photoFilter.getUserName());
		assertEquals(Tags.EMPTY_TAGS, photoFilter.getTags());
		assertTrue(photoFilter.displayablePhotoIds.isEmpty());
		assertTrue(photoFilter.processedPhotoIds.isEmpty());
	}

}
