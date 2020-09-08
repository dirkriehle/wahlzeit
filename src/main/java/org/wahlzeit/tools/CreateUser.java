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

package org.wahlzeit.tools;

import java.io.*;

import org.wahlzeit.utils.*;
import org.wahlzeit.main.*;
import org.wahlzeit.model.*;

/**
 * 
 * @author dirkriehle
 *
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
