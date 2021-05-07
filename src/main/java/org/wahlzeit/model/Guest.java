/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.services.*;

/**
 * A Guest is a client that is not logged in.
 */
public class Guest extends Client {

	/**
	 * 
	 */
	public Guest() {
		initialize(AccessRights.GUEST, EmailAddress.EMPTY);
	}

}
