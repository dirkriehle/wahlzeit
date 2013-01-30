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

import java.util.HashMap;

import org.wahlzeit.services.*;

/**
 * A Client uses the system. It is an abstract superclass.
 * This package defines guest, user, moderator, and administrator clients.
 * 
 * @author pwa
 *
 */
public class ClientCore extends Client{
	
	
	/**
	 * 
	 */
	protected AccessRights rights = AccessRights.NONE;
	
	/**
	 * 
	 */
	protected EmailAddress emailAddress = EmailAddress.EMPTY;
	
	
	/**
	 * 
	 */
	protected ClientCore() {
		// do nothing
	}
	
	/**
	 * @methodtype initialization
	 */
	protected void initialize(AccessRights myRights, EmailAddress myEmailAddress) {
		rights = myRights;
		setEmailAddress(myEmailAddress);
	}
	
	/**
	 * @methodtype initialization
	 */
	protected void initialize(AccessRights myRights, String myEmailAddress) {
		rights = myRights;
		setEmailAddress(EmailAddress.getFromString(myEmailAddress));
	}

	/**
	 * @methodtype get
	 */
	public AccessRights getRights() {
		return rights;
	}
	
	/**
	 * @methodtype set
	 */
	public void setRights(AccessRights newRights) {
		rights = newRights;
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasRights(AccessRights otherRights) {
		return AccessRights.hasRights(rights, otherRights);
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasGuestRights() {
		return hasRights(AccessRights.GUEST);
	}
	
	/**
	 * 
	 */
	public boolean hasUserRights() {
		return hasRights(AccessRights.USER);
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasModeratorRights
	() {
		return hasRights(AccessRights.MODERATOR);
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasAdministratorRights() {
		return hasRights(AccessRights.ADMINISTRATOR);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public EmailAddress getEmailAddress() {
		return emailAddress;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setEmailAddress(EmailAddress newEmailAddress) {
		emailAddress = newEmailAddress;
	}

	
	/**
	 * role manangement:
	 */

	/**
	 * 
	 */
	protected HashMap<Class<? extends ClientRole>, ClientRole> roles = new HashMap<Class<? extends ClientRole>, ClientRole>();
	
	/**
	 * returns false if the role does not exist
	 */
	@Override
	public boolean removeRole(Class<? extends ClientRole> r) {
		if(!hasRole(r)){
			return false;
		}else{
			roles.remove(r);
		}
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasRole(Class<? extends ClientRole> r) {
		return roles.containsKey(r);
	}

	/**
	 * returns false if the role already exists
	 */
	@Override
	public boolean addRole(Class<? extends ClientRole> r) {
		if(hasRole(r)){
			return false;
		}else{
			ClientRole client = null;
			if(r == Guest.class){
				client = new Guest(this);
			}else if(r == User.class){
				client = new User(this);
			}else if(r == Moderator.class){
				client = new Moderator(this);
			}else if(r == Administrator.class){
				client = new Administrator(this);
			}else{
				throw new IllegalArgumentException();
			}
			
			roles.put(r, client);
		}
		return true;
	}

	/**
	 * return the role if it exists,
	 * otherwise null
	 */
	@Override
	public ClientRole getRole(Class<? extends ClientRole> r) {
		if(!hasRole(r)){
			return null;
		}else{
			return roles.get(r);
		}
	}
	
}
