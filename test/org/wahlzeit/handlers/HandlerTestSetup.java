package org.wahlzeit.handlers;

import org.wahlzeit.model.UserSession;


import junit.extensions.TestSetup;
import junit.framework.*;

public class HandlerTestSetup extends TestSetup {

	
	public static UserSession session;
	
	public HandlerTestSetup(Test test) {
		super(test);

	}
	
	protected void setUp() {
		session = new UserSession("testContext");
	}
	
	protected void tearDown() {
		
	}
	
}
