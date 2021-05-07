/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import org.wahlzeit.utils.*;

/**
 * The PhotoStatus of a Photo captures its state in the system.
 * A photo may be visible or invisible, it may have been flagged, and it may have been deleted.
 * These states are not mutually exclusive, hence the bitset simulation in this class.
 */
public enum PhotoStatus implements EnumValue {

	/**
	 * 
	 */
	VISIBLE(0), // no bit set
	INVISIBLE(1), // only invisible bit set
	FLAGGED(2), FLAGGED2(3), // flagged without or with invisible bit set
	MODERATED(4), MODERATED2(5), MODERATED3(6), MODERATED4(7),
	DELETED(8), DELETED2(9), DELETED3(10), DELETED4(11),
	DELETED5(12), DELETED6(13),DELETED7(14), DELETED8(15);
	
	/**
	 * 
	 */
	private static final int INVISIBLE_BIT = 0;
	private static final int FLAGGED_BIT = 1;
	private static final int MODERATED_BIT = 2;
	private static final int DELETED_BIT = 3;
	
	/**
	 * All possible states of PhotoStatus
	 */
	private static PhotoStatus[] allValues = {
		VISIBLE, INVISIBLE, FLAGGED, FLAGGED2,
		MODERATED, MODERATED2, MODERATED3, MODERATED4,
		DELETED, DELETED2, DELETED3, DELETED4,
		DELETED5, DELETED6, DELETED7, DELETED8
	};
	
	/**
	 * 
	 */
	public static PhotoStatus getFromInt(int myValue) throws IllegalArgumentException {
		if ((myValue >= 0) && (myValue <= 15)) {
			return allValues[myValue];
		}
		
		throw new IllegalArgumentException("invalid PhotoStatus int: " + myValue);
	}
	
	/**
	 * 
	 */
	private static String[] valueNames = {
		"visible", "invisible", "flagged", "flagged",
		"moderated", "moderated", "moderated", "moderated",
		"deleted", "deleted", "deleted", "deleted",
		"deleted", "deleted", "deleted", "deleted"
	};
	
	/**
	 * 
	 */
	public static PhotoStatus getFromString(String myStatus) throws IllegalArgumentException {
		for (PhotoStatus status: PhotoStatus.values()) {
			if (valueNames[status.asInt()].equals(myStatus)) {
				return status;
			}
		}
		
		throw new IllegalArgumentException("invalid PhotoStatus string: " + myStatus);
	}
	
	/**
	 * 
	 */
	private int value = 0;
	
	/**
	 * 
	 */
	private PhotoStatus(int myValue) {
		value = myValue;
	}
	
	/**
	 * 
	 */
	public int asInt() {
		return value;
	}
	
	/**
	 * 
	 */
	public String asString() {
		return valueNames[value];
	}
	
	/**
	 * 
	 */
	public PhotoStatus[] getAllValues() {
		return allValues;
	}

	/**
	 * 
	 */
	public String getTypeName() {
		return "PhotoStatus";
	}
	
	/**
	 * 
	 */
	public boolean isDisplayable() {
		return !isInvisible() && !isFlagged() && !isModerated() && !isDeleted();
	}
	
	/**
	 * 
	 */
	public boolean isInvisible() {
		return (value & (1 << INVISIBLE_BIT)) != 0;
	}
	
	/**
	 * 
	 */
	public PhotoStatus asInvisible(boolean yes) {
		return yes ? flag(INVISIBLE_BIT) : unflag(INVISIBLE_BIT);
	}
	
	/**
	 * 
	 */
	public boolean isFlagged() {
		return (value & (1 << FLAGGED_BIT)) != 0;
	}
	
	/**
	 * 
	 */
	public PhotoStatus asFlagged(boolean yes) {
		return yes ? flag(FLAGGED_BIT) : unflag(FLAGGED_BIT);
	}
	
	/**
	 * 
	 */
	public boolean isModerated() {
		return (value & (1 << MODERATED_BIT)) != 0;
	}
	
	/**
	 * 
	 */
	public PhotoStatus asModerated(boolean yes) {
		return yes ? flag(MODERATED_BIT) : unflag(MODERATED_BIT);
	}
	
	/**
	 * 
	 */
	public boolean isDeleted() {
		return (value & (1 << DELETED_BIT)) != 0;
	}
	
	/**
	 * 
	 */
	public PhotoStatus asDeleted(boolean yes) {
		return yes ? flag(DELETED_BIT) : unflag(DELETED_BIT);
	}
	
	/**
	 * 
	 */
	protected PhotoStatus flag(int statusBit) {
		return allValues[value | (1 << statusBit)];
	}
	
	/**
	 * 
	 */
	protected PhotoStatus unflag(int statusBit) {
		return allValues[value & (-1 - (1 << statusBit))];
	}

}
