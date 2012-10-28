package org.wahlzeit.handlers;

import javax.mail.Session;

import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;

import junit.extensions.TestSetup;
import junit.framework.Test;

public class HandlerTestSetup extends TestSetup {

	public HandlerTestSetup(Test name) {
		super(name);
	}

	/**
	 * 
	 */
	private static UserSession session;
	public static UserSession getUserSession(){
		return session;
	}
	
	private static WebFormHandler handler;
	public static WebFormHandler getWebFromHandler(){
		return handler;
	}
	
	
	public void setUp() {
		ModelMain.configureWebPartTemplateServer();

		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);

		handler = WebPartHandlerManager
				.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}
}
