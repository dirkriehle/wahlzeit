/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.tools;

import org.wahlzeit.main.*;

/**
 * A superclass for script classes.
 */
public class RunScript extends ScriptMain {
	
	/**
	 * 
	 */
	public static void main(String[] argv) {
		new RunScript().run(argv);
	}
	
	/**
	 * 
	 */
	protected String scriptName = "";
	
	/**
	 * 
	 */
	protected int handleArg(String arg, int i, String argv[]) {
		scriptName = arg;
		
		return i;
	}
	
	/**
	 * 
	 */
	protected void execute() throws Exception {
		super.execute();
		
		runScript(scriptName);
	}
	
}
