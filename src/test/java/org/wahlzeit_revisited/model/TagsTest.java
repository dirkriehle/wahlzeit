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

package org.wahlzeit_revisited.model;

import junit.framework.TestCase;

import java.util.Set;

import static org.junit.Assert.assertNotEquals;

/**
 * Test cases for the Tags class.
 */
public class TagsTest extends TestCase {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TagsTest.class);
    }

    /**
     * @param name
     */
    public TagsTest(final String name) {
        super(name);
    }

    /**
     *
     */
    public void testEquals() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("a, b, c");
        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags("a, b, c");
        org.wahlzeit.model.Tags tags3 = new org.wahlzeit.model.Tags("a, c, b");
        org.wahlzeit.model.Tags tags4 = new org.wahlzeit.model.Tags("a, b");
        org.wahlzeit.model.Tags tags5 = new org.wahlzeit.model.Tags("a, a, b, c");
        assertEquals(tags1, tags1);
        assertEquals(tags1, tags2);
        assertEquals(tags1, tags3);
        assertNotEquals(tags1, tags4);
        assertEquals(tags1, tags5);
        assertNotEquals(null, tags1);
        assertNotEquals(tags1, new Object());
    }

    /**
     *
     */
    public void testAsTag() {
        assertEquals(org.wahlzeit.model.Tags.asTag("flo wer"), "flower");
        assertEquals(org.wahlzeit.model.Tags.asTag(" 35j lNM#&In>B << f2"), "35jlnminbf2");
        assertEquals(org.wahlzeit.model.Tags.asTag(",,,,,,"), "");
    }

    /**
     *
     */
    public void testAsString() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("tag1, tag2");
        assertSame(tags1.getSize(), 2);
        assertEquals(tags1.asString(), "tag1, tag2");
        assertEquals(tags1.asString(true, '+'), "tag1 + tag2");
    }

    /**
     *
     */
    public void testAsArray1() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("blume, zeug");
        String[] array1 = tags1.asArray();
        assertEquals(array1.length, 2);
        assertEquals(array1[0], "blume");
        assertEquals(array1[1], "zeug");

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags();
        String[] array2 = tags2.asArray();
        assertEquals(array2.length, 0);
    }

    /**
     *
     */
    public void testAsArray2() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("a, b, c, d, e");
        assertEquals(tags1.asArray().length, 5);
        assertEquals(tags1.asArray()[0], "a");

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags("a a, &/B,     , c");
        assertSame(tags2.getSize(), tags2.asArray().length);
        assertEquals(tags2.asArray().length, 3);
        assertEquals(tags2.asArray()[1], "b");
        assertEquals(tags2.asArray()[2], "c");
    }

    /**
     *
     */
    public void testGetSize() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("tag1, tag2, tag3");
        assertEquals(3, tags1.getSize());

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags("");
        assertSame(tags2.getSize(), 0);

        org.wahlzeit.model.Tags t1 = new org.wahlzeit.model.Tags("x,y,y,,");
        assertEquals(2, t1.getSize());

        org.wahlzeit.model.Tags t2 = new org.wahlzeit.model.Tags();
        assertEquals(0, t2.getSize());

        org.wahlzeit.model.Tags empty = org.wahlzeit.model.Tags.EMPTY_TAGS;
        assertEquals(0, empty.getSize());
    }

    /**
     * Make sure not only ',' can be used a separator
     */
    public void testUsesCustomSeparator() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("a-b", '-');
        assertTrue(tags1.hasTag("a"));
        assertTrue(tags1.hasTag("b"));

        // Causes failure because separator is not stored
        assertEquals("a- b", tags1.asString());

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags("a,b", '-');
        assertTrue(tags2.hasTag("ab"));

        org.wahlzeit.model.Tags tags3 = new org.wahlzeit.model.Tags("aab", 'a');
        assertTrue(tags3.hasTag("b"));
        assertFalse(tags3.hasTag("a"));

    }

    /**
     *
     */
    public void testHasTag1() {
        assertFalse(new org.wahlzeit.model.Tags(null).hasTag(null));
        assertFalse(new org.wahlzeit.model.Tags("").hasTag(null));
        assertFalse(new org.wahlzeit.model.Tags("").hasTag(""));

        assertTrue(new org.wahlzeit.model.Tags("tag").hasTag("tag"));

        assertTrue(new org.wahlzeit.model.Tags("tag,tag").hasTag("tag"));

        assertTrue(new org.wahlzeit.model.Tags("tag1,tag2").hasTag("tag1"));
        assertTrue(new org.wahlzeit.model.Tags("tag1,tag2").hasTag("tag2"));

        assertFalse("Only complete Tags should match",
                new org.wahlzeit.model.Tags("tag1,tag2").hasTag("t"));

        assertFalse("No filtering on hasTag",
                new org.wahlzeit.model.Tags("tag1,tag 2").hasTag("tag 2"));
    }

    /**
     *
     */
    public void testHasTag2() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags(" flo wer , Kinokuniya, bingo, bongo");
        assertTrue(tags1.hasTag("flower"));
        assertTrue(tags1.hasTag("kinokuniya"));
        assertFalse(tags1.hasTag("Kinokuniya"));
        assertTrue(tags1.hasTag("bingo"));
        assertFalse(tags1.hasTag("bingo, bongo"));
        assertFalse(tags1.hasTag("notexisting"));
        assertFalse(tags1.hasTag(""));

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags("    ");
        assertFalse(tags2.hasTag("testemptytags"));
        assertFalse(tags2.hasTag(""));
    }

    /**
     *
     */
    public void testHasTag3() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("\"/&%$(&a,\"\\'");
        assertTrue(tags1.hasTag("a"));
        assertFalse(tags1.hasTag("\"\\'"));
        assertFalse(tags1.hasTag(""));
    }

    /**
     *
     */
    public void testHasTag4() {
        org.wahlzeit.model.Tags t = new org.wahlzeit.model.Tags("tag, long tag, even@longer;tag");

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
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags("tag1, tag1");
        assertEquals(1, tags1.getSize());

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags("bla, blub, bla, Bla, b$%&l    a");
        assertEquals("bla, blub", tags2.asString());

        org.wahlzeit.model.Tags tags3 = new org.wahlzeit.model.Tags("bingo, bingo");
        String[] tags3array = tags3.asArray();
        assertEquals(1, tags3array.length);
    }

    /**
     *
     */
    public void testTagList() {
        org.wahlzeit.model.Tags tags1 = new org.wahlzeit.model.Tags(" flo wer , Kinokuniya, bingo, bongo");
        String[] tags1array = tags1.asArray();
        assertEquals(4, tags1array.length);
        assertEquals(tags1array[0], "bingo");
        assertEquals(tags1array[1], "bongo");
        assertEquals(tags1array[2], "flower");
        assertEquals(tags1array[3], "kinokuniya");

        assertEquals(tags1.asString(), "bingo, bongo, flower, kinokuniya");

        org.wahlzeit.model.Tags tags2 = new org.wahlzeit.model.Tags(" @ 2a hum5ug ; yah!, ohmpf ,,,");
        String[] tags2array = tags2.asArray();
        assertEquals(2, tags2array.length);
        assertEquals(tags2array[0], "2ahum5ugyah");
        assertEquals(tags2array[1], "ohmpf");

        // Testing special chars
        org.wahlzeit.model.Tags tags3 = new org.wahlzeit.model.Tags("  $%&/(,     $%&/()");
        String[] tags3array = tags3.asArray();
        assertEquals(tags3array.length, 0);
    }

    /**
     *
     */
    public void testGetTagListFromString() {
        Set<String> set = Tags.asTagSetFromString("x-y--z@!;,b---ni hao", '-');

        assertNotNull(set);
        assertEquals(4, set.size());
        assertTrue(set.contains("x"));
        assertTrue(set.contains("y"));
        assertTrue(set.contains("zb"));
        assertTrue(set.contains("nihao"));
    }
}
