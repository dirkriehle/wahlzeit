/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.services.EmailAddress;


/**
 * An Administrator is a moderator with administration privileges.
 */
public class Administrator extends Moderator {

	/**
	 * 
	 */
	public Administrator(String myName, String myPassword, String myEmailAddress, long vc) {
		this(myName, myPassword, EmailAddress.getFromString(myEmailAddress), vc);
	}
	
	/**
	 * 
	 */
	public Administrator(String myName, String myPassword, EmailAddress myEmailAddress, long vc) {
		initialize(AccessRights.ADMINISTRATOR, myEmailAddress, myName, myPassword, vc);
	}
	
	/**
	 * 
	 */
	protected Administrator() {
		// do nothing
	}
		
}
