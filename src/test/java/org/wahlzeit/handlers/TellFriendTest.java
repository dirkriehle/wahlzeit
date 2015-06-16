package org.wahlzeit.handlers;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.wahlzeit.model.UserSession;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.SessionManager;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.RegisteredOfyEnvironmentProvider;
import org.wahlzeit.testEnvironmentProvider.SysConfigProvider;
import org.wahlzeit.testEnvironmentProvider.UserSessionProvider;
import org.wahlzeit.testEnvironmentProvider.WebFormHandlerProvider;
import org.wahlzeit.webparts.WebPart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by Lukas Hahmann on 21.05.15.
 */
public class TellFriendTest {

    @ClassRule
    public static SysConfigProvider sysConfigProvider = new SysConfigProvider();
    public WebFormHandlerProvider webFormHandlerProvider = new WebFormHandlerProvider();
    @Rule
    public TestRule chain = RuleChain.
            outerRule(new LocalDatastoreServiceTestConfigProvider()).
            around(new RegisteredOfyEnvironmentProvider()).
            around(new UserSessionProvider()).
            around(webFormHandlerProvider);
    private UserSession session;
    private WebFormHandler handler;


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
        Map<String, String> args = new HashMap<String, String>();
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
        EmailAddress bcc = session.getConfiguration().getAuditEmailAddress();
        String subject = "Coolest website ever!";
        String body = "You've got to check this out!";

        Map<String, String> args = new HashMap<String, String>();
        args.put(TellFriendFormHandler.EMAIL_FROM, from.asString());
        args.put(TellFriendFormHandler.EMAIL_TO, to.asString());
        args.put(TellFriendFormHandler.EMAIL_SUBJECT, subject);
        args.put(TellFriendFormHandler.EMAIL_BODY, body);

        handler.handlePost(session, args);

        handler.handlePost(session, Collections.EMPTY_MAP); // will fail if email is sent
    }
}
