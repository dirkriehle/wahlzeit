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

import java.io.File;
import java.io.FileFilter;
import java.sql.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.servlets.AbstractServlet;
import org.wahlzeit.webparts.*;

/**
 * A single-threaded Main class with database connection.
 * Can be used by tools that don't want to start a server.
 * 
 * @author dirkriehle
 *
 */
public abstract class ModelMain extends AbstractMain {
	
	/**
	 * 
	 */
	protected void startUp(String rootDir) throws Exception {
		super.startUp(rootDir);

		if (!hasGlobals()) {
			tearDownDatabase();
			setUpDatabase();
		}
		
 		loadGlobals();

		PhotoFactory.initialize();
	}
	
	/**
	 * 
	 */
	protected boolean hasGlobals() throws SQLException {
		DatabaseConnection dbc = mainSession.ensureDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();
		DatabaseMetaData dbm = conn.getMetaData();
		ResultSet tables = dbm.getTables(null, null, "globals", null);
		return tables.next();
	}
	
	/**
	 * 
	 */
	protected void shutDown() throws Exception {
		saveAll();

		super.shutDown();
	}

	/**
	 * 
	 */
	public void setUpDatabase() throws SQLException {
		runScript("CreateTables.sql");
	}
	
	/**
	 * 
	 */
	public void tearDownDatabase() throws SQLException {
		runScript("DropTables.sql");
	}
	
	/**
	 * 
	 */
	protected void createUser(String userName, String password, String emailAddress, String photoDir) throws Exception {
		UserManager userManager = UserManager.getInstance();
		long confirmationCode = userManager.createConfirmationCode();
		User user = new User(userName, password, emailAddress, confirmationCode);
		userManager.addUser(user);
		
		PhotoManager photoManager = PhotoManager.getInstance();
		File photoDirFile = new File(photoDir);
		FileFilter photoFileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.getName().endsWith(".jpg");
			}
		};

		File[] photoFiles = photoDirFile.listFiles(photoFileFilter);
		for (int i = 0; i < photoFiles.length; i++) {
			Photo newPhoto = photoManager.createPhoto(photoFiles[i]);
			user.addPhoto(newPhoto);
		}
	}

	
	/**
	 * 
	 */
	public void loadGlobals() throws SQLException {
		DatabaseConnection dbc = mainSession.ensureDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();

		String query = "SELECT * FROM globals";
		SysLog.logQuery(query);

		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		if (result.next()) {
			int lastUserId = result.getInt("last_user_id");
			User.setLastUserId(lastUserId);
			SysLog.logSysInfo("loaded global variable lastUserId: " + lastUserId);
			int lastPhotoId = result.getInt("last_photo_id");
			PhotoId.setCurrentIdFromInt(lastPhotoId);
			SysLog.logSysInfo("loaded global variable lastPhotoId: " + lastPhotoId);
			int lastCaseId = result.getInt("last_case_id");
			Case.setLastCaseId(new CaseId(lastCaseId));
			SysLog.logSysInfo("loaded global variable lastCaseId: " + lastCaseId);
			int lastSessionId = result.getInt("last_session_id");
			AbstractServlet.setLastSessionId(lastSessionId);		
			SysLog.logSysInfo("loaded global variable lastSessionId: " + lastSessionId);
		} else {
			SysLog.logSysError("Could not load globals!");
		}
		
		stmt.close();
	}

	/**
	 *
	 */
	public synchronized void saveGlobals() throws SQLException {
		DatabaseConnection dbc = SessionManager.getDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();

		String query = "SELECT * FROM globals";
		SysLog.logQuery(query);

		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		ResultSet rset = stmt.executeQuery(query);
		if (rset.next()) {
			int lastUserId = User.getLastUserId();
			rset.updateInt("last_user_id", lastUserId);
			SysLog.logSysInfo("saved global variable lastUserId: " + lastUserId);
			int lastPhotoId = PhotoId.getCurrentIdAsInt();
			rset.updateInt("last_photo_id", lastPhotoId);
			SysLog.logSysInfo("saved global variable lastPhotoId: " + lastPhotoId);
			int lastCaseId = Case.getLastCaseId().asInt();
			rset.updateInt("last_case_id", lastCaseId);
			SysLog.logSysInfo("saved global variable lastCaseId: " + lastCaseId);
			int lastSessionId = AbstractServlet.getLastSessionId();
			rset.updateInt("last_session_id", lastSessionId);
			SysLog.logSysInfo("saved global variable lastSessionId: " + lastSessionId);
			rset.updateRow();
		} else {
			SysLog.logSysError("Could not save globals!");
		}
		
		stmt.close();
	}
	
	/**
	 * 
	 */
	public void saveAll() throws SQLException {
		PhotoCaseManager.getInstance().savePhotoCases();
		PhotoManager.getInstance().savePhotos();			
		UserManager.getInstance().saveUsers();

		saveGlobals();
	}
	
	/**
	 * 
	 */
	protected void runScript(String scriptName) throws SQLException {
		DatabaseConnection dbc = SessionManager.getDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();
		
		ConfigDir scriptsDir = SysConfig.getScriptsDir();
		
		if(scriptsDir.hasDefaultFile(scriptName)) {
			String defaultScriptFileName = scriptsDir.getAbsoluteDefaultConfigFileName(scriptName);
			runScript(conn, defaultScriptFileName);
		}
			
		if(scriptsDir.hasCustomFile(scriptName)) {
			String customConfigFileName = scriptsDir.getAbsoluteCustomConfigFileName(scriptName);
			runScript(conn, customConfigFileName);
		}
	}

	/**
	 * 
	 */
	protected void runScript(Connection conn, String fullFileName) throws SQLException {
		String query = FileUtil.safelyReadFileAsString(fullFileName);
		SysLog.logQuery(query);

		Statement stmt = conn.createStatement();
		stmt.execute(query);
	}
	
}
