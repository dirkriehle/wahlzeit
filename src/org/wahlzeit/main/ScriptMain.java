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

package org.wahlzeit.main;

import java.io.*;
import java.sql.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;

/**
 * A Main class that runs a Wahlzeit web server.
 * 
 * @author dirkriehle
 *
 */
public abstract class ScriptMain extends ModelMain {
	
	/**
	 * 
	 */
	protected boolean isToCreateTables = false;
	protected boolean isToDropTables = false;
	protected boolean isToRunScript = false;
	protected String scriptName = null;
	
	/**
	 * 
	 */
	public void run(String[] argv) {
		handleArgv(argv);
		
		try {
			startUp("web");
			execute();
		} catch(Exception ex) {
			SysLog.logThrowable(ex);
		}

		try {
			shutDown();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
	} 
	
	/**
	 * 
	 */
	protected void handleArgv(String argv[]) {
		for (int i = 0; i < argv.length; i++) {
			String arg = argv[i];

			if (arg.equals("-S") || arg.equals("--setup")) {
				isToCreateTables = true;
			} else if (arg.equals("-T") || arg.equals("--teardown")) {
				isToDropTables = true;
			} else if (arg.equals("--script") && (i++ < argv.length)) {
				isToRunScript = true;
				scriptName = argv[i];
			}
		}
	}

	/**
	 * 
	 */
	protected void execute() throws Exception {
		if (isToCreateTables) {
			createTables();
		} else if (isToDropTables) {
			dropTables();
		} else if (isToRunScript) {
			runScript(scriptName);
		}
	}
		
}
