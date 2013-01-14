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

import java.util.Set;

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.services.Language;

import com.sun.org.apache.bcel.internal.generic.InstructionConstants.Clinit;

import junit.framework.TestCase;

/**
 * 
 * @author dirkriehle
 * 
 */
public class ClientRoleTest extends TestCase {

	public static void main(final String[] args) {
		junit.textui.TestRunner.run(ClientRoleTest.class);
	}

	public ClientRoleTest(final String name) {
		super(name);
	}

	public void test_Guest_hasRights() {
		Guest g = Client.createClient(Guest.class);
		assertTrue(g.hasGuestRights());
		assertFalse(g.hasAdministratorRights());
		assertFalse(g.hasModeratorRights());
		assertFalse(g.hasUserRights());
	}
	
	public void test_Administrator_hasRights() {
		Administrator a = Client.createClient(Administrator.class);
		assertTrue(a.hasGuestRights());
		assertTrue(a.hasAdministratorRights());
		assertTrue(a.hasModeratorRights());
		assertTrue(a.hasUserRights());
	}

}
