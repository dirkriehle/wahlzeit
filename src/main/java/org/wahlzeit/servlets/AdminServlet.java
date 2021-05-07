/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.servlets;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.wahlzeit.handlers.PartUtil;
import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.model.UserLog;
import org.wahlzeit.services.SysLog;


/**
 * The servlet for managing administrative system functions.
 */
public class AdminServlet extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 42L; // any one does; class never serialized

	/**
	 * 
	 */
	public void myGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String link = request.getRequestURI();
		UserLog.logUserInfo("requested", link);
		if (isLocalHost(request)) {
			ServiceMain.getInstance().requestStop();
			displayNullPage(request, response);
		} else if (link.length() == "/admin".length()){
			SysLog.logSysInfo("redirect", PartUtil.DEFAULT_PAGE_NAME);
			redirectRequest(response, PartUtil.DEFAULT_PAGE_NAME);
		} else {
			SysLog.logSysInfo("redirect", "../" + PartUtil.DEFAULT_PAGE_NAME);
			redirectRequest(response, "../" + PartUtil.DEFAULT_PAGE_NAME);
		}
	}

}
