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

import java.util.*;

import junit.framework.*;

/**
 * 
 * @author student
 * 
 */
public class FlagReasonTest extends TestCase {
	
	/**
	 * 
	 */
	private Map<FlagReason, TestEntry> entries;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FlagReasonTest.class);
	}
	
	/**
	 * 
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		entries = new HashMap<FlagReason, TestEntry>();
		
		addEntry(FlagReason.MISMATCH, "mismatch", 0);
		addEntry(FlagReason.OFFENSIVE, "offensive", 1);
		addEntry(FlagReason.COPYRIGHT, "copyright", 2);
		addEntry(FlagReason.OTHER, "other", 3);
	}
	
	/**
	 * 
	 * @param reason
	 * @param str
	 * @param i
	 */
	protected void addEntry(FlagReason reason, String str, int i)	{
		entries.put(reason, new TestEntry(str, i));
	}
	
	/**
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void expectException(Class<? extends Exception> expected, Exception actual)	{
		assertTrue(actual != null && actual.getClass().equals(expected));
	}
	
	/**
	 * 
	 * @param i
	 */
	protected void failGetFromInt(int i)	{
		Exception ex = null;
		
		try	{
			FlagReason.getFromInt(i);
		} catch (Exception caught)	{
			ex = caught;
		}
		
		expectException(IllegalArgumentException.class, ex);
	}
	
	/**
	 * 
	 * @param str
	 */
	protected void failGetFromString(String str)	{
		Exception ex = null;
		
		try	{
			FlagReason.getFromString(str);
		} catch (Exception caught)	{
			ex = caught;
		}
		
		expectException(IllegalArgumentException.class, ex);
	}
	
	/**
	 * 
	 */
	public void testInvalidGetFromInt()	{
		failGetFromInt(-1);
		failGetFromInt(FlagReason.MAX_VALUE + 1);
	}
	
	/**
	 * 
	 */
	public void testInvalidGetFromString()	{
		failGetFromString(null);
		failGetFromString("");
		failGetFromString("some_random_string");
		failGetFromString("MiSmAtCh");
	}
	
	/**
	 * 
	 */
	public void testGetFromInt()	{
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getKey(), FlagReason.getFromInt(entry.getValue().getInt()));
		}
	}

	/**
	 * 
	 */
	public void testGetFromString()	{
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getKey(), FlagReason.getFromString(entry.getValue().getString()));
		}
	}
	
	/**
	 * 
	 */
	public void testAsInt()	{
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getValue().getInt(), entry.getKey().asInt());
		}
	}

	/**
	 * 
	 */
	public void testAsString()	{
		for (Map.Entry<FlagReason, TestEntry> entry : entries.entrySet()) {
			assertEquals(entry.getValue().getString(), entry.getKey().asString());
		}
	}
	
	/**
	 * 
	 */
	public void testGetValues()	{
		List<FlagReason> values = Arrays.asList(FlagReason.values());
		
		assertNotNull(values);
		assertEquals(FlagReason.MAX_VALUE + 1, values.size());
		
		for (FlagReason flagReason : entries.keySet()) {
			assertTrue(values.contains(flagReason));
		}
	}
	
	/**
	 * 
	 */
	private class TestEntry	{
		private String str;
		private int i;
		
		public TestEntry(String str, int i)	{
			this.str = str;
			this.i = i;
		}
		
		public String getString()	{
			return str;
		}
		
		public int getInt()	{
			return i;
		}
	}
}
