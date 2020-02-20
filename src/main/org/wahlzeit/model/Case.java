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

import org.wahlzeit.services.*;

/**
 * A case is a user complaint, most notably about an inappropriate photo.
 * Subclasses capture the specifics of different types of cases; here only an id is provided.
 * 
 * @author dirkriehle
 *
 */
public abstract class Case extends DataObject {
	
	/**
	 * 0 is never returned, first value is 1
	 */
	protected static CaseId lastCaseId = CaseId.NULL_ID;
	
	/**
	 * @methodtype get
	 */
	public static synchronized CaseId getLastCaseId() {
		return lastCaseId;
	}
	
	/**
	 * @methodtype set
	 */
	public static synchronized void setLastCaseId(CaseId newId) {
		lastCaseId = newId;
	}
	
	/**
	 * @methodtype idiom
	 */
	public static synchronized CaseId getNextCaseId() {
		return lastCaseId = lastCaseId.getNextId();
	}

}
