/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.servlets;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * A null servlet.
 */
public class NullServlet extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 42L; // any one does; class never serialized

	/**
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		displayNullPage(request, response);
	}
	
	/**
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		displayNullPage(request, response);
	}

}
