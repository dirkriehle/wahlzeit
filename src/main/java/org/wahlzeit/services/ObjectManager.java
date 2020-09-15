/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle https://dirkriehle.com
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.sql.*;
import java.util.*;

/**
 * An ObjectManager creates/reads/updates/deletes Persistent (objects) from a database.
 * It is an abstract superclass that relies on an inheritance interface and the Persistent interface.
 */
public abstract class ObjectManager {
	
	/**
	 * All objects are now saved under this root key. In case of multi-tenancy this may change to several keys.
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
