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

import java.sql.*;
import org.wahlzeit.services.*;
import org.wahlzeit.main.*;

/**
 * 
 * @author dirkriehle
 *
 */
public class RunScript extends AbstractMain {
	
	/**
	 * 
	 */
	public static void main(String[] argv) {
		instance = new RunScript();
		instance.run(argv);
	}
	
	/**
	 * 
	 */
	protected boolean isToRunScript = false;
	protected String scriptFileName = "dummy";

	/**
	 * 
	 */
	protected void handleArgv(String[] argv) {
		for (int i = 0; i < argv.length; i++) {
			String arg = argv[i];
			if (arg.equals("-S") || arg.equals("--setup")) {
				isToRunScript = true;
				scriptFileName = "CreateTables.sql";
			} else if (arg.equals("-T") || arg.equals("--teardown")) {
				isToRunScript = true;
				scriptFileName = "DropTables.sql";
			} else if (arg.equals("--script") && (i++ < argv.length)) {
				isToRunScript = true;
				scriptFileName = argv[i];
			}
		}
	}

	
	/**
	 * 
	 */
	protected void execute() throws Exception {
		DatabaseConnection dbc = ContextManager.getDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();
		
		ConfigDir scriptsDir = SysConfig.getScriptsDir();
		String defaultScriptFileName = scriptsDir.getDefaultConfigFileName(scriptFileName);
		runScript(conn, defaultScriptFileName);
			
		if(scriptsDir.hasCustomFile("CreateTables.sql")) {
			String customConfigFileName = scriptsDir.getCustomConfigFileName(scriptFileName);
			runScript(conn, customConfigFileName);
		}
	}

	/**
	 * 
	 */
	protected void runScript(Connection conn, String fullFileName) throws Exception {
		String query = FileUtil.safelyReadFileAsString(fullFileName);
		SysLog.logQuery(query);

		Statement stmt = conn.createStatement();
		stmt.execute(query);
	}
	
}
