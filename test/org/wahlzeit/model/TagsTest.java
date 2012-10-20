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

import java.util.Arrays;

import junit.framework.*;

/**
 * 
 * @author dirkriehle
 * 
 */
public class TagsTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TagsTest.class);
	}

	public TagsTest(String name) {
		super(name);
	}
	
	public void testAsTag() {		
		assertEquals(Tags.asTag("flo wer"), "flower");
		assertEquals(Tags.asTag(" 35j lNM#&In>B << f2"), "35jlnminbf2");
		assertEquals(Tags.asTag(",,,,,,"), "");
	}
	
	public void testAsString() {
		Tags tags1 = new Tags("tag1, tag2");
		assertSame(tags1.getSize(), 2);
		assertEquals(tags1.asString(), "tag1, tag2");
		assertEquals(tags1.asString(true, '+'), "tag1 + tag2");		
	}

	public void testTagList() {
		Tags tags1 = new Tags(" flo wer , Kinokuniya, bingo, bongo");
		String[] tags1array = tags1.asArray();
		assertTrue(tags1array.length == 4);
		assertEquals(tags1array[0], "flower");
		assertEquals(tags1array[1], "kinokuniya");
		assertEquals(tags1array[2], "bingo");
		assertEquals(tags1array[3], "bongo");
		assertEquals(tags1.asString(), "flower, kinokuniya, bingo, bongo");
		
		Tags tags2 = new Tags(" @ 2a hum5ug ; yah!, ohmpf ,,,");
		String[] tags2array = tags2.asArray();
		assertEquals(tags2array[0], "2ahum5ugyah");
		assertEquals(tags2array[1], "ohmpf");
	}

	public void testAsArray() {
		Tags tags1 = new Tags("tag1, t a g 2, tag 3 ");
		String[] tags1AsArray1 = tags1.asArray();
		assertEquals(3, tags1AsArray1.length);
		assertEquals("tag1", tags1AsArray1[0]);
		assertEquals("tag2", tags1AsArray1[1]);
		assertEquals("tag3", tags1AsArray1[2]);

		String[] tags1AsArray2 = tags1.asArray();
		assertFalse(tags1AsArray1 == tags1AsArray2); // should return new instances
		assertTrue(Arrays.deepEquals(tags1AsArray1, tags1AsArray2)); // but with equal elements

		String[] tags2AsArray = new Tags().asArray();
		assertEquals(0, tags2AsArray.length);

		String test = "tag0";
		for(int i = 1; i < 100; i++)
			test += ", tag" + i;
		String[] tags3AsArray = new Tags(test).asArray();
		assertEquals(100, tags3AsArray.length);
		for(int i = 0; i < 100; i++)
			assertEquals("tag" + i, tags3AsArray[i]);
	}
}
