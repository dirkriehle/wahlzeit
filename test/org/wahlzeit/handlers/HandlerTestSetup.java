/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit rating application.
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

import java.util.HashMap;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;

public class HandlerTestSetup extends TestSetup {
	private UserSession session;

	public HandlerTestSetup() {
		super(null);
	}
	public HandlerTestSetup(final Test test) {
		super(test);
	}
	public HandlerTestSetup(final Class<? extends Test> testClass) {
		super(new TestSuite(testClass));
	}


	private static HandlerTestSetup current;

	@Override
	public void setUp() {
		init();
		current = this;
	}

	@Override
	public void tearDown(){
		current = null;
	}


	private void init() {
		ModelMain.configureWebPartTemplateServer();

		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);
		
	}

	
	private static Map<Class<?>, HandlerTestSetup> classes = new HashMap<Class<?>, HandlerTestSetup>();
	
	public static HandlerTestSetup getCurrent(Class<?> cls) {
		if (null == current) {
			// Simulating a class wide decorating in not decorated mode
			if (classes.containsKey(cls)){
				return classes.get(cls);
			}
			HandlerTestSetup setup = new HandlerTestSetup();
			setup.init();
			classes.put(cls, setup);
			return setup;
		}
		return current;
	}

	public UserSession getSession() {
		return session;
	}
}
