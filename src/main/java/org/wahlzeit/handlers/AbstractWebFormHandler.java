/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;

import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.webparts.*;


/**
 * A superclass for handling forms.
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
