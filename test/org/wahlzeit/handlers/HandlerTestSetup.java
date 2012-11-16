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

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;
import org.wahlzeit.tools.CreateUser;

import junit.extensions.TestSetup;
import junit.framework.Test;


public class HandlerTestSetup extends TestSetup{
	
	public HandlerTestSetup(Test test) {
		super(test);
	}
	
	
	static protected UserSession session;
	
	@Override
	protected void tearDown() throws Exception{
		session = null;
	}
		
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		session = createUserSession();
		ContextManager.setThreadLocalContext(session);
		
		if(fTest instanceof HandlerTest) {
			HandlerTest test = (HandlerTest) fTest;
			test.setUserSession(session);
		}
	
	}

	public static UserSession getCurrentUserSession() {
		return session;
	}
	
	protected UserSession createUserSession() {
		UserSession result = null;
		
		ModelMain.configureWebPartTemplateServer();
		
		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();
		
		result = new UserSession("testContext");
		result.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		
		return result;
		
	}
}
