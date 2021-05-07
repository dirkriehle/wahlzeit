/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.servlets;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.wahlzeit.handlers.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;
import org.wahlzeit.webparts.*;



/**
 * The main servlet class for handling incoming and outgoing requests.
 */
@MultipartConfig // Servlet 3.0 support for file upload
public class MainServlet extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 42L; // any one does; class never serialized

	/**
	 * 
	 */
	public void myGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		UserSession us = ensureUserSession(request);
		
		String link = request.getRequestURI();
		int linkStart = link.lastIndexOf("/") + 1;
		int linkEnd = link.indexOf(".html");
		if (linkEnd == -1) {
			linkEnd = link.length();
		}
		
		link = link.substring(linkStart, linkEnd);
		UserLog.logUserInfo("requested", link);

		WebPageHandler handler = WebPartHandlerManager.getWebPageHandler(link);
		String newLink = PartUtil.DEFAULT_PAGE_NAME;
		if (handler != null) {
			Map args = getRequestArgs(request);
			SysLog.logSysInfo("GET arguments: " + getRequestArgsAsString(us, args));
			newLink = handler.handleGet(us, link, args);
		}

		if (newLink.equals(link)) { // no redirect necessary
			WebPart result = handler.makeWebPart(us);
			us.addProcessingTime(System.currentTimeMillis() - startTime);
			configureResponse(us, response, result);
			us.clearSavedArgs(); // saved args go from post to next get
			us.resetProcessingTime();
		} else {
			SysLog.logSysInfo("redirect", newLink);
			redirectRequest(response, newLink);
			us.addProcessingTime(System.currentTimeMillis() - startTime);
		}
	}
	
	/**
	 * 
	 */
	public void myPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		UserSession us = ensureUserSession(request);
		
		String link = request.getRequestURI();
		int linkStart = link.lastIndexOf("/") + 1;
		int linkEnd = link.indexOf(".form");
		if (linkEnd != -1) {
			link = link.substring(linkStart, linkEnd);
		} else {
			link = PartUtil.NULL_FORM_NAME;
		}
		UserLog.logUserInfo("postedto", link);
			
		Map args = getRequestArgs(request);
		SysLog.logSysInfo("POST arguments: " + getRequestArgsAsString(us, args));
		
		WebFormHandler formHandler = WebPartHandlerManager.getWebFormHandler(link);
		link = PartUtil.DEFAULT_PAGE_NAME;
		if (formHandler != null) {
			link = formHandler.handlePost(us, args);
		}

		redirectRequest(response, link);
		us.addProcessingTime(System.currentTimeMillis() - startTime);
	}

	/**
	 * 
	 */
	protected Map getRequestArgs(HttpServletRequest request) throws IOException, ServletException {
        String contentType = request.getContentType();
        if ((contentType != null) && contentType.startsWith("multipart/form-data")) {
			return getMultiPartRequestArgs(request);
		} else {
			return request.getParameterMap();
		}
	}

	/**
	 * 
	 */
	protected Map getMultiPartRequestArgs(HttpServletRequest request) throws IOException, ServletException {
		Map<String, String> result = new HashMap<String, String>();

		Collection<Part> parts = request.getParts();
		for (Iterator<Part> i = parts.iterator(); i.hasNext(); ) {
			Part part = i.next();

			String key = part.getName();
			if (key.equals("file")) {
				String tempFileName = SysConfig.getTempDir().asString() + Thread.currentThread().getId();
				saveImgToDirectory(tempFileName, part.getInputStream());
				result.put("fileName", tempFileName);
			} else {
				result.put(key, request.getParameter(key));
			}			
		}
		
		return result;
	}

	private void saveImgToDirectory(String filePath, InputStream imgStream) throws IOException {
		File targetFile = new File(filePath);
		if(targetFile.exists()) {
			targetFile.delete();
		}
		Files.copy(imgStream, Paths.get(filePath));
	}

}
