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

package org.wahlzeit.services;

import java.sql.*;
import java.util.*;

/**
 * An ObjectManager creates/reads/updates/deletes Persistent (objects) from a (relational) Database.
 * It is an abstract superclass that relies an inheritance interface and the Persistent interface.
 * Subclasses for specific types of object need to implement createObject and provide Statements.
 * 
 * @author dirkriehle
 *
 */
public abstract class ObjectManager {
	
	/**
	 * 
	 */
	public DatabaseConnection getDatabaseConnection() throws SQLException {
		return SessionManager.getDatabaseConnection();
	}
	    
	/**
	 * 
	 */
	protected PreparedStatement getReadingStatement(String stmt) throws SQLException {
    	DatabaseConnection dbc = getDatabaseConnection();
    	return dbc.getReadingStatement(stmt);
	}
	
	/**
	 * 
	 */
	protected PreparedStatement getUpdatingStatement(String stmt) throws SQLException {
    	DatabaseConnection dbc = getDatabaseConnection();
    	return dbc.getUpdatingStatement(stmt);
	}
	
	/**
	 * 
	 */
	protected Persistent readObject(PreparedStatement stmt, int value) throws SQLException {
		Persistent result = null;
		stmt.setInt(1, value);
		SysLog.logQuery(stmt);
		ResultSet rset = stmt.executeQuery();
		if (rset.next()) {
			result = createObject(rset);
		}

		return result;
	}
	
	/**
	 * 
	 */
	protected Persistent readObject(PreparedStatement stmt, String value) throws SQLException {
		Persistent result = null;
		stmt.setString(1, value);
		SysLog.logQuery(stmt);
		ResultSet rset = stmt.executeQuery();
		if (rset.next()) {
			result = createObject(rset);
		}

		return result;
	}
	
	/**
	 * 
	 */
	protected void readObjects(Collection result, PreparedStatement stmt) throws SQLException {
		SysLog.logQuery(stmt);
		ResultSet rset = stmt.executeQuery();
		while (rset.next()) {
			Persistent obj = createObject(rset);
			result.add(obj);
		}
	}
		
	/**
	 * 
	 */
	protected void readObjects(Collection result, PreparedStatement stmt, String value) throws SQLException {
		stmt.setString(1, value);
		SysLog.logQuery(stmt);
		ResultSet rset = stmt.executeQuery();
		while (rset.next()) {
			Persistent obj = createObject(rset);
			result.add(obj);
		}
	}
		
	/**
	 * 
	 */
	protected abstract Persistent createObject(ResultSet rset) throws SQLException;

	/**
	 * 
	 */
	protected void createObject(Persistent obj, PreparedStatement stmt, int value) throws SQLException {
		stmt.setInt(1, value);
		SysLog.logQuery(stmt);
		stmt.executeUpdate();
	}
	
	/**
	 * 
	 */
	protected void createObject(Persistent obj, PreparedStatement stmt, String value) throws SQLException {
		stmt.setString(1, value);
		SysLog.logQuery(stmt);
		stmt.executeUpdate();
	}
	
	/**
	 * 
	 */
	protected void updateObject(Persistent obj, PreparedStatement stmt) throws SQLException {
		if (obj.isDirty()) {
			obj.writeId(stmt, 1);
			SysLog.logQuery(stmt);
			ResultSet rset = stmt.executeQuery();
			if (rset.next()) {
				obj.writeOn(rset);
				rset.updateRow();
				updateDependents(obj);
				obj.resetWriteCount();
			} else {
				SysLog.logSysError("trying to update non-existent object: " + obj.getIdAsString() + "(" + obj.toString() + ")");
			}
		}
	}
	
	/**
	 * 
	 */
	protected void updateObjects(Collection coll, PreparedStatement stmt) throws SQLException {
		for (Iterator i = coll.iterator(); i.hasNext(); ) {
			Persistent obj = (Persistent) i.next();
			updateObject(obj, stmt);
		}
	}
	
	/**
	 * 
	 */
	protected void updateDependents(Persistent obj) throws SQLException {
		// do nothing
	}
	
	/**
	 * 
	 */
	protected void deleteObject(Persistent obj, PreparedStatement stmt) throws SQLException {
		obj.writeId(stmt, 1);
		SysLog.logQuery(stmt);
		stmt.executeUpdate();
	}

	/**
	 * 
	 */
	protected void assertIsNonNullArgument(Object arg) {
		assertIsNonNullArgument(arg, "anonymous");
	}
	
	/**
	 * 
	 */
	protected void assertIsNonNullArgument(Object arg, String label) {
		if (arg == null) {
			throw new IllegalArgumentException(label + " should not be null");
		}
	}

}
