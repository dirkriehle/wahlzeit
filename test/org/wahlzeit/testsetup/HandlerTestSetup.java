package org.wahlzeit.testsetup;

import org.wahlzeit.handlers.PartUtil;
import org.wahlzeit.handlers.WebFormHandler;
import org.wahlzeit.handlers.WebPartHandlerManager;
import org.wahlzeit.main.ModelMain;
import org.wahlzeit.main.Wahlzeit;
import org.wahlzeit.model.LanguageConfigs;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.ContextManager;
import org.wahlzeit.services.Language;

import junit.extensions.TestSetup;
import junit.framework.Test;

public class HandlerTestSetup extends TestSetup {
	public static UserSession session;
	public static WebFormHandler handler;
	
	public HandlerTestSetup(Test test)	{
    	super(test);
    }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		ModelMain.configureWebPartTemplateServer();
		
		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);
		
		handler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		session = null;
		handler = null;
	}
}
