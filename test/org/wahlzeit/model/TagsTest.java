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
		
		//testing some thoughts
		Tags tags2 = new Tags("tag1 + tag2 , tag3, + tag4 + ,");
		assertEquals(tags2.asString(true, '+'), "tag1tag2 + tag3 + tag4");
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
		
		//testing some thoughts
		Tags tags3 = new Tags("  $%&/(,     $%&/()");
		String[] tags3array = tags3.asArray();
		assertEquals(tags3array.length, 0);
	}
	
	//Testmethode fuer hasTag()
	public void testHasTag() {		
		Tags tags1 = new Tags("a,a a,b!@b");
		assertTrue(tags1.hasTag("a"));
		assertTrue(tags1.hasTag("aa"));
		assertTrue(tags1.hasTag("bb"));
		assertFalse(tags1.hasTag(""));
		assertFalse(tags1.hasTag("a a"));
		
		Tags tags2 = new Tags("");
		assertFalse(tags2.hasTag(""));
	}
	
	//Testmethode fuer asArray()
	public void testAsArray() {
		Tags tags1 = new Tags("a, b, c, d, e");
		assertEquals(tags1.asArray().length, 5);
		assertEquals(tags1.asArray()[0], "a");
		
		Tags tags2 = new Tags("a a, &/B,     , c");
		assertSame(tags2.getSize(), tags2.asArray().length);
		assertEquals(tags2.asArray().length, 3);
		assertEquals(tags2.asArray()[1], "b");
		assertEquals(tags2.asArray()[2], "c");
	}
	
	//Test auf doppelte Tags
	public void testDoubleTags() {
		Tags tags1 = new Tags("bla, blub, bla, Bla, b$%&l    a");
		assertEquals("bla, blub", tags1.asString());
	}
}
