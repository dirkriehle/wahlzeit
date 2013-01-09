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

import java.sql.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
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
	protected void startUp() throws Exception {
		super.startUp();

		configureWebPartTemplateServer();

		loadGlobals();
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
	public static void loadGlobals() throws SQLException {
		DatabaseConnection dbc = ContextManager.getDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();

		String query = "SELECT * FROM globals";
		SysLog.logQuery(query);

		Statement stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		if (result.next()) {
			int lastUserId = result.getInt("last_user_id");
			User.setLastUserId(lastUserId);
			SysLog.logInfo("loaded global variable lastUserId: " + lastUserId);
			int lastPhotoId = result.getInt("last_photo_id");
			PhotoId.setValue(lastPhotoId);
			SysLog.logInfo("loaded global variable lastPhotoId: " + lastPhotoId);
			int lastCaseId = result.getInt("last_case_id");
			Case.setLastCaseId(new CaseId(lastCaseId));
			SysLog.logInfo("loaded global variable lastCaseId: " + lastCaseId);
			int lastSessionId = result.getInt("last_session_id");
			AbstractServlet.setLastSessionId(lastSessionId);		
			SysLog.logInfo("loaded global variable lastSessionId: " + lastSessionId);
		} else {
			SysLog.logError("Could not load globals!");
		}
		
		stmt.close();
	}

	/**
	 *
	 */
	public static synchronized void saveGlobals() throws SQLException {
		DatabaseConnection dbc = ContextManager.getDatabaseConnection();
		Connection conn = dbc.getRdbmsConnection();

		String query = "SELECT * FROM globals";
		SysLog.logQuery(query);

		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		ResultSet rset = stmt.executeQuery(query);
		if (rset.next()) {
			int lastUserId = User.getLastUserId();
			rset.updateInt("last_user_id", lastUserId);
			SysLog.logInfo("saved global variable lastUserId: " + lastUserId);
			int lastPhotoId = PhotoId.getValue();
			rset.updateInt("last_photo_id", lastPhotoId);
			SysLog.logInfo("saved global variable lastPhotoId: " + lastPhotoId);
			int lastCaseId = Case.getLastCaseId().asInt();
			rset.updateInt("last_case_id", lastCaseId);
			SysLog.logInfo("saved global variable lastCaseId: " + lastCaseId);
			int lastSessionId = AbstractServlet.getLastSessionId();
			rset.updateInt("last_session_id", lastSessionId);
			SysLog.logInfo("saved global variable lastSessionId: " + lastSessionId);
			rset.updateRow();
		} else {
			SysLog.logError("Could not save globals!");
		}
		
		stmt.close();
	}
	
	/**
	 * 
	 */
	public static void saveAll() throws SQLException {
		PhotoCaseManager.getInstance().savePhotoCases();
		PhotoManager.getInstance().savePhotos();			
		UserManager.getInstance().saveUsers();

		saveGlobals();
	}

	/**
	 * 
	 */
	public static void configureWebPartTemplateServer() {
		ConfigDir templatesDir = SysConfig.getTemplatesDir();
		WebPartTemplateServer.getInstance().setTemplatesDir(templatesDir);
	}
	
}
