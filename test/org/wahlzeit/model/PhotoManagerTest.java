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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.utils.StringUtil;

import junit.framework.TestCase;

/**
 * 
 * @author pwa
 * 
 */
public class PhotoManagerTest extends TestCase {

	public static void main(final String[] args) {
		junit.textui.TestRunner.run(PhotoManagerTest.class);
	}

	public PhotoManagerTest(final String name) {
		super(name);
	}
	
	public void testSqlAnnotation() {
		PersistentType pt = new PersistentType(Photo.class);
		@SuppressWarnings("rawtypes")
		Map<String, Class> m = pt.getSqlAttributeTypeMap();
		assertTrue(m.get("ownerEmailAddress").equals(String.class));
		assertTrue(m.get("ownerId").equals(int.class));
		assertTrue(m.get("ownerName").equals(String.class));
		assertTrue(m.get("width").equals(int.class));
		assertTrue(m.get("height").equals(int.class));
		assertTrue(m.get("praiseSum").equals(int.class));
		assertTrue(m.get("noVotes").equals(int.class));
		assertTrue(m.get("creationTime").equals(long.class));
		assertTrue(m.get("ownerNotifyAboutPraise").equals(boolean.class));

		assertTrue(m.size() == 9);
	}
	
	public void testloadPhotos() {
		PhotoManager pm = PhotoManager.getInstance();
		
		ArrayList<Photo> photos = new ArrayList<Photo>();
		pm.loadPhotos(photos);
		
		// must access existing photos
		if (photos.size() == 0) return;
		
		for(Photo p : photos){
			if(p.getId() != PhotoId.NULL_ID){
				assertTrue(p.getWidth() > 0);
				assertTrue(p.getHeight() > 0);
			}
		}
	}
		
}
