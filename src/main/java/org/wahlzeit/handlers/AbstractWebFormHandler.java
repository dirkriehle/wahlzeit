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

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.webparts.*;


/**
 * 
 * @author dirkriehle
 *
 */
public abstract class AbstractWebFormHandler extends AbstractWebPartHandler implements WebFormHandler {

	/**
	 * 
	 */
	protected void initialize(String partTmplName, AccessRights neededRights) {
		super.initialize(partTmplName, neededRights);
	}
	
	/**
	 * 
	 */
	public WebPart makeWebPart(UserSession us) {
		WebPart result = createWebPart(us);
		doMakeWebPart(us, result);
		return result;
	}
	
	/**
	 * 
	 */
	protected abstract void doMakeWebPart(UserSession us, WebPart part);

	/**
	 * @methodtype boolean-query
	 */
	protected boolean isWellFormedPost(UserSession us, Map args) {
		return true;
	}

	/**
	 * 
	 */
	public final String handlePost(UserSession us, Map args) {
		if (!hasAccessRights(us, args)) {
			SysLog.logSysInfo("insufficient rights for POST from: " + us.getEmailAddressAsString());
			return getIllegalAccessErrorPage(us);
		}
		
		if (!isWellFormedPost(us, args)) {
			SysLog.logSysInfo("received ill-formed POST from: " + us.getEmailAddressAsString());
			return getIllegalArgumentErrorPage(us);
		}
		
		try {
			// may throw Exception
			return doHandlePost(us, args);
		} catch (Throwable t) {
			SysLog.logThrowable(t);
			return getInternalProcessingErrorPage(us);
		}
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession us, Map args) {
		return PartUtil.DEFAULT_PAGE_NAME;
	}
	
}
