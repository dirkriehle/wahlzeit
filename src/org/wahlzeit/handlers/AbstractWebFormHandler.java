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
 * @author driehle
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
	public WebPart makeWebPart(UserSession ctx) {
		WebPart result = createWebPart(ctx);
		doMakeWebPart(ctx, result);
		return result;
	}
	
	/**
	 * 
	 */
	protected abstract void doMakeWebPart(UserSession ctx, WebPart part);

	/**
	 * 
	 */
	protected boolean isWellFormedPost(UserSession ctx, Map args) {
		return true;
	}

	/**
	 * 
	 */
	public final String handlePost(UserSession ctx, Map args) {
		if (!hasAccessRights(ctx, args)) {
			SysLog.logInfo("insufficient rights for POST from: " + ctx.getEmailAddressAsString());
			return getIllegalAccessErrorPage(ctx);
		}
		
		if (!isWellFormedPost(ctx, args)) {
			SysLog.logInfo("received ill-formed POST from: " + ctx.getEmailAddressAsString());
			return getIllegalArgumentErrorPage(ctx);
		}
		
		try {
			// may throw Exception
			return doHandlePost(ctx, args);
		} catch (Throwable t) {
			SysLog.logThrowable(t);
			return getInternalProcessingErrorPage(ctx);
		}
	}
	
	/**
	 * 
	 */
	protected String doHandlePost(UserSession ctx, Map args) {
		return PartUtil.DEFAULT_PAGE_NAME;
	}
	
}
