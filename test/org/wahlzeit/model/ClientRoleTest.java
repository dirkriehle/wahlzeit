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

import org.wahlzeit.services.EmailAddress;

import junit.framework.TestCase;

/**
 * 
 * @author pwa
 * 
 */
public class ClientRoleTest extends TestCase {

	public static void main(final String[] args) {
		junit.textui.TestRunner.run(ClientRoleTest.class);
	}

	public ClientRoleTest(final String name) {
		super(name);
	}

	public void test_Guest_hasRights1() {
		Guest g = Client.createClient(Guest.class);
		assertTrue(g.hasGuestRights());
		
		assertFalse(g.hasUserRights());
		assertFalse(g.hasModeratorRights());
		assertFalse(g.hasAdministratorRights());
	}
	
	public void test_Guest_hasRights2() {
		Guest g = Client.createClient(Guest.class);
		assertTrue(g.hasRights(AccessRights.GUEST));
		
		assertFalse(g.hasRights(AccessRights.USER));
		assertFalse(g.hasRights(AccessRights.MODERATOR));
		assertFalse(g.hasRights(AccessRights.ADMINISTRATOR));
	}
	
	public void test_User_hasRights() {
		User g = Client.createClient(User.class);
		assertTrue(g.hasGuestRights());
		assertTrue(g.hasUserRights());

		assertFalse(g.hasModeratorRights());
		assertFalse(g.hasAdministratorRights());
	}
	
	public void test_Moderator_hasRights() {
		Moderator a = Client.createClient(Moderator.class);
		assertTrue(a.hasGuestRights());
		assertTrue(a.hasUserRights());
		assertTrue(a.hasModeratorRights());
		
		assertFalse(a.hasAdministratorRights());
	}
	
	public void test_Administrator_hasRights() {
		Administrator a = Client.createClient(Administrator.class);
		assertTrue(a.hasGuestRights());
		assertTrue(a.hasAdministratorRights());
		assertTrue(a.hasModeratorRights());
		assertTrue(a.hasUserRights());
	}
	
	public void test_getRights_byRoleChange() {
		Guest g = Client.createClient(Guest.class);
		assertTrue(g.hasGuestRights());
		assertFalse(g.hasUserRights());
		assertFalse(g.hasModeratorRights());
		assertFalse(g.hasAdministratorRights());
		
		g.addRole(User.class);
		assertTrue(g.hasGuestRights());
		assertTrue(g.hasUserRights());
		assertFalse(g.hasModeratorRights());
		assertFalse(g.hasAdministratorRights());
		
		g.addRole(Moderator.class);
		assertTrue(g.hasGuestRights());
		assertTrue(g.hasUserRights());
		assertTrue(g.hasModeratorRights());
		assertFalse(g.hasAdministratorRights());
		
		g.addRole(Administrator.class);
		assertTrue(g.hasGuestRights());
		assertTrue(g.hasUserRights());
		assertTrue(g.hasModeratorRights());
		assertTrue(g.hasAdministratorRights());
	}
	
	public void test_initialize1() {
		Administrator a = Client.createClient(Administrator.class);
		String e1 = "banana.joe@example.com";
		a.initialize(AccessRights.ADMINISTRATOR, e1);
		assertTrue(a.hasGuestRights());
		assertTrue(a.hasAdministratorRights());
		assertTrue(a.hasModeratorRights());
		assertTrue(a.hasUserRights());
		String e2 = a.getEmailAddress().asString();
		assertTrue(e1.equals(e2));
	}

	public void test_initialize2() {
		Administrator a = Client.createClient(Administrator.class);
		String e1 = "banana.joe@example.com";
		EmailAddress email1= EmailAddress.getFromString(e1);
		a.initialize(AccessRights.ADMINISTRATOR, email1);
		String e2 = a.getEmailAddress().asString();
		assertTrue(e1.equals(e2));
	}
	
	/**
	 * role management:
	 */
	
	public void test_addRole_hasRole(){
		Guest g = Client.createClient(Guest.class);
		assertTrue(g.hasRole(Guest.class));
		assertFalse(g.hasRole(User.class));
		assertFalse(g.hasRole(Moderator.class));
		assertFalse(g.hasRole(Administrator.class));
		
		// the role already exists:
		assertFalse(g.addRole(Guest.class));
		
		assertTrue(g.addRole(User.class));
		assertTrue(g.hasRole(Guest.class));
		assertTrue(g.hasRole(User.class));
		assertFalse(g.hasRole(Moderator.class));
		assertFalse(g.hasRole(Administrator.class));
		
		assertFalse(g.addRole(User.class));
		
		assertTrue(g.addRole(Moderator.class));
		assertTrue(g.hasRole(Guest.class));
		assertTrue(g.hasRole(User.class));
		assertTrue(g.hasRole(Moderator.class));
		assertFalse(g.hasRole(Administrator.class));
		
		assertFalse(g.addRole(Moderator.class));
		
		assertTrue(g.addRole(Administrator.class));
		assertTrue(g.hasRole(Guest.class));
		assertTrue(g.hasRole(User.class));
		assertTrue(g.hasRole(Moderator.class));
		assertTrue(g.hasRole(Administrator.class));
		
		assertFalse(g.addRole(Administrator.class));
	}
	
	public void test_removeRole(){
		Guest g = Client.createClient(Guest.class);
		assertTrue(g.hasRole(Guest.class));
		assertFalse(g.hasRole(User.class));
		assertFalse(g.hasRole(Moderator.class));
		assertFalse(g.hasRole(Administrator.class));
		
		// remove the role:
		assertTrue(g.removeRole(Guest.class));
		assertFalse(g.hasRole(Guest.class));
		assertFalse(g.hasRole(User.class));
		assertFalse(g.hasRole(Moderator.class));
		assertFalse(g.hasRole(Administrator.class));
		
		// the role cannot be removed again:
		assertFalse(g.removeRole(Guest.class));
		
		assertTrue(g.addRole(Administrator.class));
		assertFalse(g.addRole(Administrator.class));
		assertTrue(g.hasRole(Administrator.class));
	}
	
	public void test_getRole(){
		Guest g = Client.createClient(Guest.class);
		ClientRole gRole = g.getRole(Guest.class);
		
		// valid type?
		assertTrue(gRole instanceof Guest);
		
		// same memory location?
		assertTrue(g == gRole);
		
		// can't get another role:
		assertTrue(g.getRole(User.class) == null);
		assertTrue(g.getRole(Moderator.class) == null);
		assertTrue(g.getRole(Administrator.class) == null);
	}
	
	public void test_sameCore(){
		Guest g = Client.createClient(Guest.class);
		g.initialize(AccessRights.GUEST, "donald.duck@entenhausen.de");
		assertTrue(g.addRole(User.class));
		assertTrue(g.addRole(Moderator.class));
		assertTrue(g.addRole(Administrator.class));
		
		User gAsUser = (User)g.getRole(User.class);
		EmailAddress e1 = g.getEmailAddress();
		EmailAddress e2 = gAsUser.getEmailAddress();
		
		// same memory location?
		assertTrue(e1 == e2);

		Moderator gAsModerator = (Moderator)g.getRole(Moderator.class);
		e2 = gAsModerator.getEmailAddress();
		assertTrue(e1 == e2);

		Administrator gAsAdministrator = (Administrator)g.getRole(Administrator.class);
		e2 = gAsAdministrator.getEmailAddress();
		assertTrue(e1 == e2);
	}
	
}
