/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.handlers;

import java.util.*;


/**
 * A manager class for web parts.
 */
public class WebPartHandlerManager {
	
	/**
	 * 
	 */
	public static WebPageHandler getWebPageHandler(String name) {
		return instance.getWebPageHandlerFor(name);
	}
	
	/**
	 * 
	 */
	public static WebFormHandler getWebFormHandler(String name) {
		return instance.getWebFormHandlerFor(name);
	}
	
	/**
	 * 
	 */
	public static WebPartHandlerManager getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	protected static final WebPartHandlerManager instance = new WebPartHandlerManager();
	
	/**
	 * 
	 */
	protected Map<String, WebPartHandler> handler = new HashMap<String, WebPartHandler>();
	
	/**
	 * 
	 */
	protected WebPartHandlerManager() {
		// do nothing
	}

	/**
	 *
	 */
	public WebPageHandler getWebPageHandlerFor(String name) {
		WebPartHandler result = handler.get(name);
		if (result == null || !(result instanceof WebPageHandler)) {
			 result = getDefaultWebPageHandler();
		}
		
		return (WebPageHandler) result;
	}
		
	/**
	 *
	 */
	public WebFormHandler getWebFormHandlerFor(String name) {
		WebPartHandler result = handler.get(name);
		if (result == null || !(result instanceof WebFormHandler)) {
			 result = getDefaultWebPageHandler();
		}
		
		return (WebFormHandler) result;
	}
		
	/**
	 * 
	 */
	public WebPartHandler addWebPartHandler(String name, WebPartHandler myHandler) {
		handler.put(name, myHandler);
		return myHandler;
	}
	
	/**
	 * 
	 */
	public WebPartHandler getDefaultWebPageHandler() {
		return handler.get("index");
	}
		
	/**
	 * 
	 */
	public WebPartHandler getDefaultWebFormHandler() {
		return handler.get("null");
	}
	
}
