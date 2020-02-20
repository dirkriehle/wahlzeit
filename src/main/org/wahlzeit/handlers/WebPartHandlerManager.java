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


/**
 * 
 * @author dirkriehle
 *
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
