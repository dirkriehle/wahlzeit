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

import org.wahlzeit.services.*;

/**
 * A Client uses the system. It is an abstract superclass. This package defines
 * guest, user, moderator, and administrator clients.
 * 
 * @author dirkriehle
 * 
 */
public abstract class Client {

	/**
	 * 
	 */
	protected Client() {
		// do nothing
	}
	
	/**
	 * @methodtype initialization
	 */
	protected abstract void initialize(AccessRights myRights, EmailAddress myEmailAddress);
	
	/**
	 * @methodtype initialization
	 */
	protected abstract void initialize(AccessRights myRights, String myEmailAddress);
	
	/**
	 * @methodtype get
	 */
	public abstract AccessRights getRights();
	
	/**
	 * @methodtype set
	 */
	public abstract void setRights(AccessRights newRights);
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public abstract boolean hasRights(AccessRights otherRights);
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public abstract boolean hasGuestRights();
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public abstract boolean hasUserRights();
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public abstract boolean hasModeratorRights();
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public abstract boolean hasAdministratorRights();
	
	/**
	 * 
	 * @methodtype get
	 */
	public abstract EmailAddress getEmailAddress();
	
	/**
	 * 
	 * @methodtype set
	 */
	public abstract void setEmailAddress(EmailAddress newEmailAddress);

	
	/**
	 * role management:
	 */

	/**
	 * 
	 */
	public abstract boolean addRole(Class<? extends ClientRole> role);

	/**
	 * 
	 */
	public abstract boolean removeRole(Class<? extends ClientRole> role);

	/**
	 * 
	 */
	public abstract boolean hasRole(Class<? extends ClientRole> role);
	
	/**
	 * 
	 */
	public abstract ClientRole getRole(Class<? extends ClientRole> c);

	
	/**
	 * client creator:
	 * creates a new core client 
	 * and adds the client role given as parameter
	 * @param <T>
	 */
	public static <T extends ClientRole> T createClient(Class<T> c) throws IllegalArgumentException{
		
		ClientCore cc = new ClientCore();
		
		cc.addRole(c);
		
		ClientRole res = cc.getRole(c);
		
		return (T)res;
	}

}
