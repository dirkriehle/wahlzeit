/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.webparts.*;


/**
 * The general interface for web partgs.
 */
public interface WebPartHandler {
	
	/**
	 * 
	 */
	public WebPart makeWebPart(UserSession us);
	
	/**
	 * 
	 */
	public String handleGet(UserSession us, String link, Map args);
	
}
