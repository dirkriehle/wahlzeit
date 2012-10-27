package org.wahlzeit.handlers;

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;

import junit.extensions.TestSetup;
import junit.framework.Test;

public class HandlerTestSetup extends TestSetup {

	public HandlerTestSetup(Test test) {
		super(test);
		// TODO Auto-generated constructor stub
	}

	UserSession session;
	WebFormHandler handler;

	protected void setUp() {
		ModelMain.configureWebPartTemplateServer();

		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);

		handler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}

	protected void tearDown() {
		session = null;
		handler = null;
	}

}
