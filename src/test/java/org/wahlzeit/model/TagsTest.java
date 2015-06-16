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

import junit.framework.TestCase;

import java.util.Set;

/**
 * 
 * @author dirkriehle
 * 
 */
public class TagsTest extends TestCase {

	/**
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		junit.textui.TestRunner.run(TagsTest.class);
	}

	/**
	 * 
	 * @param name
	 */
	public TagsTest(final String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void testEquals() {
		Tags tags1 = new Tags("a, b, c");
		Tags tags2 = new Tags("a, b, c");
		Tags tags3 = new Tags("a, c, b");
		Tags tags4 = new Tags("a, b");
		Tags tags5 = new Tags("a, a, b, c");
		assertTrue(tags1.equals(tags1));
		assertTrue(tags1.equals(tags2));
		assertTrue(tags1.equals(tags3));
		assertFalse(tags1.equals(tags4));
		assertTrue(tags1.equals(tags5));
		assertFalse(tags1.equals(null));
		assertFalse(tags1.equals(new Object()));
	}

	/**
	 * 
	 */
	public void testAsTag() {
		assertEquals(Tags.asTag("flo wer"), "flower");
		assertEquals(Tags.asTag(" 35j lNM#&In>B << f2"), "35jlnminbf2");
		assertEquals(Tags.asTag(",,,,,,"), "");
	}

	/**
	 * 
	 */
	public void testAsString() {
		Tags tags1 = new Tags("tag1, tag2");
		assertSame(tags1.getSize(), 2);
		assertEquals(tags1.asString(), "tag1, tag2");
		assertEquals(tags1.asString(true, '+'), "tag1 + tag2");
	}

	/**
	 * 
	 */
	public void testAsArray1() {
		Tags tags1 = new Tags("blume, zeug");
		String[] array1 = tags1.asArray();
		assertEquals(array1.length, 2);
		assertEquals(array1[0], "blume");
		assertEquals(array1[1], "zeug");

		Tags tags2 = new Tags();
		String[] array2 = tags2.asArray();
		assertEquals(array2.length, 0);
	}

	/**
	 * 
	 */
	public void testAsArray2() {
		Tags tags1 = new Tags("a, b, c, d, e");
		assertEquals(tags1.asArray().length, 5);
		assertEquals(tags1.asArray()[0], "a");
		
		Tags tags2 = new Tags("a a, &/B,     , c");
		assertSame(tags2.getSize(), tags2.asArray().length);
		assertEquals(tags2.asArray().length, 3);
		assertEquals(tags2.asArray()[1], "b");
		assertEquals(tags2.asArray()[2], "c");
	}

	/**
	 * 
	 */
	public void testGetSize() {
		Tags tags1 = new Tags("tag1, tag2, tag3");
		assertEquals(3, tags1.getSize());

		Tags tags2 = new Tags("");
		assertSame(tags2.getSize(), 0);

		Tags t1 = new Tags("x,y,y,,");
		assertEquals(2, t1.getSize());
		
		Tags t2 = new Tags();
		assertEquals(0, t2.getSize());
		
		Tags empty = Tags.EMPTY_TAGS;
		assertTrue(empty.getSize() == 0);
	}

	/**
	 * Make sure not only ',' can be used a separator
	 */
	public void testUsesCustomSeparator() {
		Tags tags1 = new Tags("a-b", '-');
		assertTrue(tags1.hasTag("a"));
		assertTrue(tags1.hasTag("b"));

		// Causes failure because separator is not stored
		assertEquals("a- b", tags1.asString());

		Tags tags2 = new Tags("a,b", '-');
		assertTrue(tags2.hasTag("ab"));

		Tags tags3 = new Tags("aab", 'a');
		assertTrue(tags3.hasTag("b"));
		assertFalse(tags3.hasTag("a"));

	}

	/**
	 * 
	 */
	public void testHasTag1() {
		assertFalse(new Tags(null).hasTag(null));
		assertFalse(new Tags("").hasTag(null));
		assertFalse(new Tags("").hasTag(""));

		assertTrue(new Tags("tag").hasTag("tag"));

		assertTrue(new Tags("tag,tag").hasTag("tag"));

		assertTrue(new Tags("tag1,tag2").hasTag("tag1"));
		assertTrue(new Tags("tag1,tag2").hasTag("tag2"));

		assertFalse("Only complete Tags should match",
				new Tags("tag1,tag2").hasTag("t"));

		assertFalse("No filtering on hasTag",
				new Tags("tag1,tag 2").hasTag("tag 2"));
	}

	/**
	 * 
	 */
	public void testHasTag2() {
		Tags tags1 = new Tags(" flo wer , Kinokuniya, bingo, bongo");
		assertTrue(tags1.hasTag("flower"));
		assertTrue(tags1.hasTag("kinokuniya"));
		assertFalse(tags1.hasTag("Kinokuniya"));
		assertTrue(tags1.hasTag("bingo"));
		assertFalse(tags1.hasTag("bingo, bongo"));
		assertFalse(tags1.hasTag("notexisting"));
		assertFalse(tags1.hasTag(""));

		Tags tags2 = new Tags("    ");
		assertFalse(tags2.hasTag("testemptytags"));
		assertFalse(tags2.hasTag(""));
	}

	/**
	 * 
	 */
	public void testHasTag3() {
		Tags tags1 = new Tags("\"/&%$(&a,\"\\'");
		assertTrue(tags1.hasTag("a"));
		assertFalse(tags1.hasTag("\"\\'"));
		assertFalse(tags1.hasTag(""));
	}

	/**
	 * 
	 */
	public void testHasTag4()	{
		Tags t = new Tags("tag, long tag, even@longer;tag");

		assertTrue(t.hasTag("tag"));
		assertTrue(t.hasTag("longtag"));
		assertTrue(t.hasTag("evenlongertag"));

		assertFalse(t.hasTag(null));
		assertFalse(t.hasTag(""));
	}

	/**
	 * 
	 */
	public void testHasSetSemantics() {
		Tags tags1 = new Tags("tag1, tag1");
		assertEquals(1, tags1.getSize());

		Tags tags2 = new Tags("bla, blub, bla, Bla, b$%&l    a");
		assertEquals("bla, blub", tags2.asString());

		Tags tags3 = new Tags("bingo, bingo");
		String[] tags3array = tags3.asArray();
		assertTrue(tags3array.length == 1);
	}

	/**
	 * 
	 */
	public void testTagList() {
		Tags tags1 = new Tags(" flo wer , Kinokuniya, bingo, bongo");
		String[] tags1array = tags1.asArray();
		assertTrue(tags1array.length == 4);
		assertEquals(tags1array[0], "bingo");
		assertEquals(tags1array[1], "bongo");
		assertEquals(tags1array[2], "flower");
		assertEquals(tags1array[3], "kinokuniya");

		assertEquals(tags1.asString(), "bingo, bongo, flower, kinokuniya");

		Tags tags2 = new Tags(" @ 2a hum5ug ; yah!, ohmpf ,,,");
		String[] tags2array = tags2.asArray();
		assertTrue(tags2array.length == 2);
		assertEquals(tags2array[0], "2ahum5ugyah");
		assertEquals(tags2array[1], "ohmpf");

		// Testing special chars
		Tags tags3 = new Tags("  $%&/(,     $%&/()");
		String[] tags3array = tags3.asArray();
		assertEquals(tags3array.length, 0);
	}

	/**
	 * 
	 */
	public void testGetTagListFromString()	{
		Set<String> set = Tags.asTagSetFromString("x-y--z@!;,b---ni hao", '-');
		
		assertTrue(set != null);
		assertTrue(set.size() == 4);
		assertTrue(set.contains("x"));
		assertTrue(set.contains("y"));
		assertTrue(set.contains("zb"));
		assertTrue(set.contains("nihao"));
	}
}
