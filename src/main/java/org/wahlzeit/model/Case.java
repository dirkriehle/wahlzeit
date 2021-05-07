/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.services.*;

/**
 * A case is a user complaint, most notably about an inappropriate photo.
 * Subclasses capture the specifics of different types of cases; here only an id is provided.
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
