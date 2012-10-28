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
		
	static UserSession session;
	static WebFormHandler handler;
	
	public HandlerTestSetup(Test test)
	{
		super(test);
	}
	
	@Override
	public void setUp() {
		ModelMain.configureWebPartTemplateServer();
		
		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);
		
		handler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}

	public static void getValues(TellFriendTest tellFriendTest) {
		tellFriendTest.setHandler(handler);
		tellFriendTest.setSession(session);		
	}
	
	

}
