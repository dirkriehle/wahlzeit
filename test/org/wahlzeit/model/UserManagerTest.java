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
import org.wahlzeit.utils.StringUtil;
import junit.framework.TestCase;

/**
 * 
 * @author pwa
 * 
 */
public class UserManagerTest extends TestCase {

	public static void main(final String[] args) {
		junit.textui.TestRunner.run(UserManagerTest.class);
	}

	public UserManagerTest(final String name) {
		super(name);
	}
	
	public void testSetAttribute() {
		User u2 = Client.createClient(User.class);
		assertTrue(u2.setAttributeValue("id", 23));
		int i = (Integer)u2.getAttributeValue("id");
		assertTrue(i == 23);
	}
	
	public void testLoadUsers() {
		UserManager um = UserManager.getInstance();
		ArrayList<User> users = new ArrayList<User>(); 
		um.loadUsers(users);
		
		User u = um.getUserByName("testuser");
		assertTrue(u != null);
		assertTrue(users.contains(u));
		
		assertTrue(u.getName().equals("testuser"));
		assertTrue(StringUtil.isValidStrictEmailAddress(u.getEmailAddress().asString()));
	}
	
	public void testReadWriteUser(){
		UserManager um = UserManager.getInstance();
		User u = um.getUserByName("testuser");
		
		boolean originalNotifyAboutPraise = u.getNotifyAboutPraise();
		boolean changedNotifyAboutPraise = !originalNotifyAboutPraise;
		
		assertTrue(u.setAttributeValue("notifyAboutPraise", changedNotifyAboutPraise));
		assertTrue(u.getNotifyAboutPraise() == changedNotifyAboutPraise);
		assertTrue(u.getAttributeValue("notifyAboutPraise").equals((Boolean)changedNotifyAboutPraise));
		um.saveUser(u);
		u = um.getUserByName("testuser");
		assertTrue(u.getNotifyAboutPraise() == changedNotifyAboutPraise);
		assertTrue(u.getAttributeValue("notifyAboutPraise").equals((Boolean)changedNotifyAboutPraise));
		assertTrue(u.setAttributeValue("notifyAboutPraise", originalNotifyAboutPraise));
		um.saveUser(u);
		
		// original state?
		u = um.getUserByName("testuser");
		assertTrue(u.getNotifyAboutPraise() == originalNotifyAboutPraise);
	}
		
	public void testSqlAnnotation() {
		PersistentType pt = new PersistentType(User.class);
		@SuppressWarnings("rawtypes")
		Map<String, Class> m = pt.getSqlAttributeTypeMap();
		assertTrue(m.get("id").equals(int.class));
		assertTrue(m.get("name").equals(String.class));
		assertTrue(m.get("nameAsTag").equals(String.class));
		assertTrue(m.get("password").equals(String.class));
		assertTrue(m.get("notifyAboutPraise").equals(boolean.class));
		assertTrue(m.get("confirmationCode").equals(long.class));
		assertTrue(m.get("creationTime").equals(long.class));

		assertTrue(m.size() == 7);
	}
	
}
