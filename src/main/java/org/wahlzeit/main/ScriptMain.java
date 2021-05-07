/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.main;

import org.wahlzeit.services.*;

/**
 * A Main class that serves to run scripts (and exit afterwards).
 */
public abstract class ScriptMain extends ModelMain {
	
	/**
	 * 
	 */
	protected boolean isToSetUpDatabase = false;
	protected boolean isToTearDownDatabase = false;
	
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
			i = handleArg(argv[i], i, argv);
		}
	}
	
	/**
	 * 
	 */
	protected int handleArg(String arg, int i, String argv[]) {
		if (arg.equals("-S") || arg.equals("--setup")) {
			isToSetUpDatabase = true;
		} else if (arg.equals("-T") || arg.equals("--teardown")) {
			isToTearDownDatabase = true;
		} 
		
		return i;
	}

	/**
	 * 
	 */
	protected void execute() throws Exception {
		if (isToSetUpDatabase) {
			setUpDatabase();
		} else if (isToTearDownDatabase) {
			tearDownDatabase();
		}
	}
		
}
