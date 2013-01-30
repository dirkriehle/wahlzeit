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

package org.wahlzeit.model;

/**
 * Tier 1 Exception (controller/business tier):
 * represents an exception on the layer communicating 
 * with the data tier (tier 0)
 */
public class ControllerException extends Exception {

	/**
	 * represents the error codes
	 *
	 */
	public enum ControllerErrorCode{
		USER_ALREADY_EXISTS, UNDEFINED;
	}
	
	/**
	 * 
	 */
	private ControllerErrorCode errorCode = null;
	
	/**
	 * 
	 */
	public ControllerException(ControllerErrorCode err){
		super();
		this.errorCode = err;
	}
	
	/**
	 * 
	 */
	public ControllerErrorCode getErrorCode(){
		return errorCode;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 734211012319202408L;
	
}
