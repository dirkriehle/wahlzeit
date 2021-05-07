/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.io.File;

/**
 * A class to manage directories.
 */
public class Directory {
	
	/**
	 * 
	 */
	protected String rootDir = "";
	protected String relativeDir = "";
	
	/**
	 * 
	 */
	public Directory(String newRootDir, String newRelativeDir) {
		rootDir = newRootDir;
		relativeDir = newRelativeDir;
	}
	
	/**
	 * 
	 */
	public String asString() {
		return rootDir + File.separator + relativeDir;
	}
	
	/**
	 * 
	 */
	public String getRootDir() {
		return rootDir;
	}
	
	/**
	 * 
	 */
	public String getRelativeDir() {
		return relativeDir;
	}

}
