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

import java.util.Map;

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.utils.StringUtil;

import junit.framework.TestCase;

/**
 * 
 * @author pwa
 * 
 */
public class PersistentTest extends TestCase {

	public static void main(final String[] args) {
		junit.textui.TestRunner.run(PersistentTest.class);
	}

	public PersistentTest(final String name) {
		super(name);
	}
	
	public void testSetGetAttribute() {
		User u = Client.createClient(User.class);
		assertTrue(u.setAttributeValue("id", 23));
		int i = (Integer)u.getAttributeValue("id");
		assertTrue(i == 23);
	}
	
}
