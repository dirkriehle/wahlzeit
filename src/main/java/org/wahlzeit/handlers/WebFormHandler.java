/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.UserSession;

/**
 * The general interface for web forms
 */
public interface WebFormHandler extends WebPartHandler {
	
	/**
	 * @return suggest name of page to render
	 */
	public String handlePost(UserSession us, Map args);
	
}
