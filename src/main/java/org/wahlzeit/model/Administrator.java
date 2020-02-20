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


/**
 * An Administrator is a moderator with administration privileges.
 * 
 * @author dirkriehle
 *
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
