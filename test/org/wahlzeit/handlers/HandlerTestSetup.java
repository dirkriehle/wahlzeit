/*
 * Copyright (c) 2006-2012 by Dirk Riehle, http://dirkriehle.com
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

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.ServerMain;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;

public class HandlerTestSetup extends TestSetup {

	/**
	 * 
	 */
	protected static UserSession session;
	protected static WebFormHandler handler;

	/**
	 * 
	 */
	public HandlerTestSetup(Test arg0) {
		super(arg0);
	}

	/**
	 * 
	 */
	@Override
	public void setUp() {
		ModelMain.configureWebPartTemplateServer();

		ServerMain.configurePartHandlers();
		ServerMain.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);

		handler = WebPartHandlerManager
				.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}

	/**
	 * 
	 */
	public static UserSession getSession() {
		return session;
	}

	/**
	 * 
	 */
	public static WebFormHandler getHandler() {
		return handler;
	}

}
