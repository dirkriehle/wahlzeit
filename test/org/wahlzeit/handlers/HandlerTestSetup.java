package org.wahlzeit.handlers;
import junit.framework.*;
import junit.extensions.TestSetup;
import org.wahlzeit.main.*;
import org.wahlzeit.model.*;
import org.wahlzeit.services.*;


public class HandlerTestSetup extends TestSetup {
	
	public HandlerTestSetup (Test test) {
		super(test);
	}
	
	static protected UserSession session;
	static protected WebFormHandler handler;
	
	protected void setUp() throws Exception {
		ModelMain.configureWebPartTemplateServer();

		Wahlzeit.configurePartHandlers();
		Wahlzeit.configureLanguageModels();

		session = new UserSession("testContext");
		session.setConfiguration(LanguageConfigs.get(Language.ENGLISH));
		ContextManager.setThreadLocalContext(session);

		handler = WebPartHandlerManager.getWebFormHandler(PartUtil.TELL_FRIEND_FORM_NAME);
	}
	
	public static UserSession getSession() {
		return session;
	}
	
	public static WebFormHandler getHandler() {
		return handler;
	}
	
}
