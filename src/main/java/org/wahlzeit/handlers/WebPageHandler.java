/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.webparts.*;


/**
 * The general interface for web pages.
 */
public interface WebPageHandler extends WebPartHandler {

	/**
	 * 
	 */
	public WebPart makeWebPart(UserSession us);
	
	
}
