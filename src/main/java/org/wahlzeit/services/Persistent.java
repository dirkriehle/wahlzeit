/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services;

import java.sql.*;

/**
 * A Persistent object is an object that can be read from and written to some storage.
 * Also, it has a write count, which serves as a dirty flag.
 */
public interface Persistent {
	
	/**
	 * 
	 */
	public boolean isDirty();
	
	/**
	 * 
	 */
	public void incWriteCount();
	
	/**
	 * 
	 */
	public void resetWriteCount();

	/**
	 * 
	 */
	public String getIdAsString();
	
	/**
	 * 
	 */
	public void readFrom(ResultSet rset) throws SQLException;
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException;
	
	/**
	 * 
	 */
	public void writeId(PreparedStatement stmt, int pos) throws SQLException;
	
}
