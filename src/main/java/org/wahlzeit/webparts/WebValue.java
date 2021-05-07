/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.webparts;

/**
 * A WebValue is representation of enum values providing HTML formatting instructions.
 * It supports drop-down boxes (SELECTED) and radio buttons (CHECKED).
 */
public class WebValue {
	
	/**
	 * 
	 */
	protected String checkedKey = "";
	protected String selectedKey = "";
	
	/**
	 * 
	 */
	public WebValue(String myCheckedKey, String mySelectedKey) {
		checkedKey = myCheckedKey;
		selectedKey = mySelectedKey;
	}
	
	/**
	 * 
	 */
	public String getCheckedKey() {
		return checkedKey;
	}
	
	/**
	 * 
	 */
	public String getSelectedKey() {
		return selectedKey;
	}

}
