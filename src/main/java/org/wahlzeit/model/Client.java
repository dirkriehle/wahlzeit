/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.services.*;

/**
 * A Client uses the system. It is an abstract superclass. This package defines guest, user, moderator, and
 * administrator clients.
 */
public abstract class Client {
	
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
	protected Client() {
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
	
}
