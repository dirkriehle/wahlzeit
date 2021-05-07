/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

/**
 * Simple value object for case ids.
 *
 */
public class CaseId {
	
	/**
	 * 
	 */
	public static final CaseId NULL_ID = new CaseId(0);
	
	/**
	 * 
	 */
	private final int id;
	
	/**
	 * 
	 */
	public CaseId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 */
	public int asInt() {
		return id;
	}
	
	/**
	 * 
	 */
	public CaseId getNextId() {
		return new CaseId(id + 1);
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaseId other = (CaseId) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
}
