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

/**
 * error codes from the sql standard:
 * http://www.postgresql.org/docs/current/static/errcodes-appendix.html
 *
 */
public enum SQLErrorCodes {
	
	/**
	 * 
	 */
	/// TODO add the missing error codes
	undefined(-1), invalid_statement(1000), connection_exception(8000), connection_does_not_exist(8003), connection_failure(8006);
	
	/**
	 * 
	 */
	private int value;
	
	/**
	 * 
	 */
	private SQLErrorCodes(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 */
	public int getCode() {
		return value;
	}
	
}
