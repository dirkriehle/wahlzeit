/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.tools;

import org.wahlzeit.utils.*;
import org.wahlzeit.main.*;

/**
 * A script to create users.
 */
public class CreateUser extends ScriptMain {
	
	/**
	 * 
	 */
	public static void main(String[] argv) {
		new CreateUser().run(argv);
	}
	
	/**
	 * 
	 */
	protected String userName = "testuser";
	protected String password = "testuser";
	protected String emailAddress = "info@wahlzeit.org";
	protected String photoDir = "config/photos";
	
	/**
	 * 
	 */
	protected void handleArgv(String argv[]) {
		super.handleArgv(argv);
		
		if (StringUtil.isNullOrEmptyString(password)) {
			password = userName;
		}
	}
	
	/**
	 * 
	 */
	protected int handleArg(String arg, int i, String[] argv) {
		if (arg.equals("--password")) {
			password = argv[++i];
		} else if (arg.equals("--username")) {
			userName = argv[++i];
		} else if (arg.equals("--emailaddress")) {
			emailAddress = argv[++i];
		} else if (arg.equals("--photodir")) {
			photoDir = argv[++i];
		}
		
		return i;
	}
	
	/**
	 * 
	 */
	protected void execute() throws Exception {
		createUser(userName, password, emailAddress, photoDir);
	}
	
}
