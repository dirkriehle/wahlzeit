package org.wahlzeit.handlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.SessionManager;
import org.wahlzeit.testEnvironmentProvider.*;
import org.wahlzeit.webparts.WebPart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Acceptance tests for the TellFriend feature.
 */
public class TellFriendTest {

	private UserSession session;
	private WebFormHandler handler;

	public static TestRule dependencyInjections = new DependencyInjectionRule();
	public static SysConfigProvider sysConfigProvider = new SysConfigProvider();
	public static WebFormHandlerProvider webFormHandlerProvider = new WebFormHandlerProvider();

	@ClassRule
	public static TestRule chain = RuleChain.
			outerRule(sysConfigProvider).
			around(new LocalDatastoreServiceTestConfigProvider()).
			around(new RegisteredOfyEnvironmentProvider()).
			around(new UserSessionProvider()).
			around(dependencyInjections).
			around(webFormHandlerProvider);

	@Before
	public void setUp() {
		session = (UserSession) SessionManager.getThreadLocalSession();
		handler = webFormHandlerProvider.getWebFormHandler();
	}


	/**
	 *
	 */
	@Test
	public void testTellFriendMakeWebPart() {
		WebPart part = handler.makeWebPart(session);
		// no failure is good behavior

		EmailAddress to = EmailAddress.getFromString("engel@himmel.de");
		Map<String, String> args = new HashMap<>();
		args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
		String expectedSubject = "Oh well...";
		args.put(TellFriendFormHandler.EMAIL_SUBJECT, expectedSubject);
		handler.handlePost(session, args);

		part = handler.makeWebPart(session);

		String expectedRecipient = to.asString();
		String recipient = part.getValue(TellFriendFormHandler.EMAIL_TO).toString();
		assertTrue("Recipient not as expected, instead: " + recipient, recipient.equals(expectedRecipient));

		String subject = part.getValue(TellFriendFormHandler.EMAIL_SUBJECT).toString();
		assertTrue("Subject not as expected, instead: " + subject, expectedSubject.equals(subject));
	}

	/**
	 *
	 */
	@Test
	public void testTellFriendPost() {
		EmailAddress from = EmailAddress.getFromString("info@wahlzeit.org");
		EmailAddress to = EmailAddress.getFromString("fan@yahoo.com");
		EmailAddress bcc = session.getClient().getLanguageConfiguration().getAuditEmailAddress();
		String subject = "Coolest website ever!";
		String body = "You've got to check this out!";

		Map<String, String> args = new HashMap<>();
		args.put(TellFriendFormHandler.EMAIL_FROM, from.asString());
		args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
		args.put(TellFriendFormHandler.EMAIL_SUBJECT, subject);
		args.put(TellFriendFormHandler.EMAIL_BODY, body);

		handler.handlePost(session, args);

		handler.handlePost(session, Collections.EMPTY_MAP); // will fail if email is sent
	}
}
