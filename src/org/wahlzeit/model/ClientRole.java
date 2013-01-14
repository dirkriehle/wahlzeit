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
 * the client role delegates all calls to the 
 * client core
 */
public abstract class ClientRole extends Client{
	
	/**
	 * 
	 */
	protected ClientCore core = null;
	
	/**
	 * 
	 */
	public ClientRole(ClientCore core) {
		this.core = core;
	}
	
	/**
	 * 
	 */
	@Override
	protected void initialize(AccessRights myRights, EmailAddress myEmailAddress){
		core.initialize(myRights, myEmailAddress);
	}
	
	/**
	 * 
	 */
	@Override
	public AccessRights getRights(){
		return core.getRights();
	}
	
	/**
	 * 
	 */
	@Override
	public void setRights(AccessRights newRights){
		core.setRights(newRights);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasRights(AccessRights otherRights){
		return core.hasRights(otherRights);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasGuestRights(){
		return core.hasGuestRights();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasUserRights(){
		return core.hasUserRights();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasModeratorRights(){
		return core.hasModeratorRights();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasAdministratorRights(){
		return core.hasAdministratorRights();
	}
	
	/**
	 * 
	 */
	@Override
	public EmailAddress getEmailAddress(){
		return core.getEmailAddress();
	}
	
	/**
	 * 
	 */
	@Override
	public void setEmailAddress(EmailAddress newEmailAddress){
		core.setEmailAddress(newEmailAddress);
	}

	/**
	 * 
	 */
	@Override
	public boolean addRole(Class<? extends ClientRole> role){
		return core.addRole(role);
	}

	/**
	 * 
	 */
	@Override
	public boolean removeRole(Class<? extends ClientRole> role){
		return core.removeRole(role);
	}

	/**
	 * 
	 */
	@Override
	public boolean hasRole(Class<? extends ClientRole> role){
		return core.hasRole(role);
	}
	
	/**
	 * 
	 */
	@Override
	public ClientRole getRole(Class<? extends ClientRole> c) {
		return core.getRole(c);
	}
	
}
