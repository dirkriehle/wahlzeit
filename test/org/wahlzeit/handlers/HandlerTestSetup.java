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

import org.wahlzeit.main.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;

import junit.framework.*;
import junit.extensions.*;

public class HandlerTestSetup extends TestSetup {
	
	/**
	 * 
	 */
	public UserSession session;	
	
	/**
	 * 
	 */	
	public HandlerTestSetup(Test test) {
		super(test);
	}
	
	/**
	 * 
	 */	
	protected void setUp() throws Exception {
		super.setUp();
		
		session = createUserSession();
		SessionManager.setThreadLocalSession(session);
		
		if (fTest instanceof HandlerTest) {
			HandlerTest test = (HandlerTest) fTest;
			test.setUserSession(session);
		}
	}
	
	/**
	 * 
	 */
	protected UserSession createUserSession() {
		UserSession result = null;
		
		ServerMain serverMain = (ServerMain) ModelMain.getInstance();
		
		// FIXME set templates dir to template-server from SysConfig.getTemplatesDir
		// ModelMain.configureWebPartTemplateServer();
		

		serverMain.configurePartHandlers();
		serverMain.configureLanguageModels();

		result = new UserSession("testContext");
		result.setConfiguration(LanguageConfigs.get(Language.ENGLISH));

		return result;
	}
	
}
