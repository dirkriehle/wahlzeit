/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.services.*;

/**
 * A Moderator is a system user with moderator privileges.
 */
public class Moderator extends User {

	/**
	 * 
	 */
	public Moderator(String myName, String myPassword, String myEmailAddress, long vc) {
		this(myName, myPassword, EmailAddress.getFromString(myEmailAddress), vc);
	}
	
	/**
	 * 
	 */
	public Moderator(String myName, String myPassword, EmailAddress myEmailAddress, long vc) {
		initialize(AccessRights.MODERATOR, myEmailAddress, myName, myPassword, vc);
	}
	
	/**
	 * 
	 */
	protected Moderator() {
		// do nothing
	}
		
}
