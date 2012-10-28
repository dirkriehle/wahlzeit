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

import junit.framework.*;
import junit.extensions.TestSetup;

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.model.*;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;

/**
 * 
 * @author dirkriehle
 * 
 */
public class AllTests extends TestSuite {

	protected static UserSession session;
	protected static WebFormHandler handler;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TellFriendTest.class);
		TestSetup wrapper = new TestSetup(suite) {
			protected void setUp() {
				ModelMain.configureWebPartTemplateServer();
				
				Wahlzeit.configurePartHandlers();
				Wahlzeit.configureLanguageModels();

				session = new UserSession("testContext");
				session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
				ContextManager.setThreadLocalContext(session);
				
				handler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
			}
		};
		return wrapper;
	}
}
