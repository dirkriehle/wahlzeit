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

import java.util.ArrayList;
import java.util.Map;

import junit.framework.TestCase;

/**
 * 
 * @author pwa
 * 
 */
public class PhotoCaseManagerTest extends TestCase {

	public static void main(final String[] args) {
		junit.textui.TestRunner.run(PhotoCaseManagerTest.class);
	}

	public PhotoCaseManagerTest(final String name) {
		super(name);
	}
	
	public void testSqlAnnotation() {
		PersistentType pt = new PersistentType(PhotoCase.class);
		@SuppressWarnings("rawtypes")
		Map<String, Class> m = pt.getSqlAttributeTypeMap();
		assertTrue(m.get("wasDecided").equals(boolean.class));
		assertTrue(m.get("decisionTime").equals(long.class));
		assertTrue(m.get("creationTime").equals(long.class));
		assertTrue(m.get("explanation").equals(String.class));
		assertTrue(m.get("flagger").equals(String.class));

		assertTrue(m.size() == 5);
	}
	
	public void testLoadCases() {
		PhotoCaseManager pcm = PhotoCaseManager.getInstance();
		pcm.initialize();
		ArrayList<PhotoCase> cases = new ArrayList<PhotoCase>();
		pcm.loadOpenPhotoCases(cases);
		if(cases.size() == 0) return;
		
		for(PhotoCase c : cases){
			assertTrue(c.getId().asInt() > 0);
		}
	}	
}
