/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

/**
 * A SysSession is a context for system threads i.e. not user sessions.
 */
public class SysSession extends Session {
	
	/**
	 * 
	 */
	public SysSession(String myName) {
		initialize(myName);	
	}

}
