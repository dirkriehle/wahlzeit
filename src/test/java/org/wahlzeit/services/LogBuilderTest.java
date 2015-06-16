package org.wahlzeit.services;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.wahlzeit.model.CaseId;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.RegisteredOfyEnvironmentProvider;
import org.wahlzeit.testEnvironmentProvider.SysConfigProvider;
import org.wahlzeit.testEnvironmentProvider.UserServiceProvider;
import org.wahlzeit.testEnvironmentProvider.UserSessionProvider;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.wahlzeit.services.LogBuilder.ACTION;
import static org.wahlzeit.services.LogBuilder.CLIENT;
import static org.wahlzeit.services.LogBuilder.EXCEPTION_REASON;
import static org.wahlzeit.services.LogBuilder.INFO_SEPARATOR;
import static org.wahlzeit.services.LogBuilder.LEVEL;
import static org.wahlzeit.services.LogBuilder.NAME_VALUE_SEPARATOR;
import static org.wahlzeit.services.LogBuilder.SESSION;
import static org.wahlzeit.services.LogBuilder.STACKTRACE;
import static org.wahlzeit.services.LogBuilder.SYSTEM_LEVEL;
import static org.wahlzeit.services.LogBuilder.USER_LEVEL;

/**
 * Test class for {@link LogBuilder}.
 * <p/>
 * Created by Lukas Hahmann on 26.05.15.
 */
public class LogBuilderTest {



    @ClassRule
    public static RuleChain ruleChain = RuleChain.
            outerRule(new LocalDatastoreServiceTestConfigProvider()).
            around(new RegisteredOfyEnvironmentProvider()).
            around(new SysConfigProvider()).
            around(new UserServiceProvider()).
            around(new UserSessionProvider());

    @Test
    public void testCreateUserMessage() {
        String logMessage = LogBuilder.createUserMessage().toString();
        assertNotNull(logMessage);
        assertEquals(getExpectedUserMessage(), logMessage);

        // make sure even when there is no thread local session that LogBuilder wont throw an exception
        Session session = SessionManager.getThreadLocalSession();
        SessionManager.dropThreadLocalSession();
        LogBuilder.createUserMessage().toString();
        SessionManager.setThreadLocalSession(session);
    }

    protected String getExpectedUserMessage() {
        return LEVEL + NAME_VALUE_SEPARATOR + USER_LEVEL + INFO_SEPARATOR +
                SESSION + NAME_VALUE_SEPARATOR + UserSessionProvider.USER_SESSION_NAME + INFO_SEPARATOR +
                CLIENT + NAME_VALUE_SEPARATOR + SessionManager.getThreadLocalSession().getClientId();
    }

    @Test
    public void testCreateSystemMessage() {
        String logMessage = LogBuilder.createSystemMessage().toString();
        assertNotNull(logMessage);
        assertEquals(getExpectedSystemMessage(), logMessage);
    }

    protected String getExpectedSystemMessage() {
        return LEVEL + NAME_VALUE_SEPARATOR + SYSTEM_LEVEL + INFO_SEPARATOR +
                SESSION + NAME_VALUE_SEPARATOR + UserSessionProvider.USER_SESSION_NAME + INFO_SEPARATOR +
                CLIENT + NAME_VALUE_SEPARATOR + SessionManager.getThreadLocalSession().getClientId();
    }

    @Test
    public void testAddParameterInt() {
        LogBuilder logBuilder = LogBuilder.createUserMessage().addParameter("Yoda", 1337);
        String logMessage = logBuilder.toString();
        assertNotNull(logMessage);
        String expectedYodaMessage = INFO_SEPARATOR + "Yoda" + NAME_VALUE_SEPARATOR + "1337";
        assertTrue(logMessage.contains(expectedYodaMessage));

        logBuilder.addParameter(null, -1);
        logMessage = logBuilder.toString();
        String expectedNullMessage = INFO_SEPARATOR + null + NAME_VALUE_SEPARATOR + "-1";
        assertTrue(logMessage.contains(expectedYodaMessage + expectedNullMessage));
    }

    @Test
    public void testAddParameterString() {
        LogBuilder logBuilder = LogBuilder.createUserMessage().addParameter("Yoda", "Frank Oz");
        String logMessage = logBuilder.toString();
        assertNotNull(logMessage);
        String expectedYodaMessage = INFO_SEPARATOR + "Yoda" + NAME_VALUE_SEPARATOR + "Frank Oz";
        assertTrue(logMessage.contains(expectedYodaMessage));

        logBuilder.addParameter("Han Solo", "42");
        logMessage = logBuilder.toString();
        String expectedHanMessage = INFO_SEPARATOR + "Han Solo" + NAME_VALUE_SEPARATOR + "42";
        assertTrue(logMessage.contains(expectedYodaMessage + expectedHanMessage));

        logBuilder.addParameter(null, null);
        logMessage = logBuilder.toString();
        String expectedNullMessage = INFO_SEPARATOR + null + NAME_VALUE_SEPARATOR + null;
        assertTrue(logMessage.contains(expectedYodaMessage + expectedHanMessage + expectedNullMessage));
    }

    @Test
    public void testAddParameterObject() {
        CaseId caseId1 = new CaseId(1108);
        LogBuilder logBuilder = LogBuilder.createUserMessage().addParameter("Didi", caseId1);
        String logMessage = logBuilder.toString();
        assertNotNull(logMessage);
        String expectedDidiMessage = INFO_SEPARATOR + "Didi" + NAME_VALUE_SEPARATOR + "1108";
        assertTrue(logMessage.contains(expectedDidiMessage));

        CaseId caseId2 = new CaseId(512);
        logBuilder.addParameter("Elli", caseId2);
        logMessage = logBuilder.toString();
        String expectedElliMessage = INFO_SEPARATOR + "Elli" + NAME_VALUE_SEPARATOR + "512";
        assertTrue(logMessage.contains(expectedDidiMessage + expectedElliMessage));

        logBuilder.addParameter(null, null);
        logMessage = logBuilder.toString();
        String expectedNullMessage = INFO_SEPARATOR + null + NAME_VALUE_SEPARATOR + null;
        assertTrue(logMessage.contains(expectedDidiMessage + expectedElliMessage + expectedNullMessage));
    }

    @Test
    public void testAddMessage() {
        LogBuilder logBuilder = LogBuilder.createSystemMessage().addMessage("");
        String logMessage = logBuilder.toString();
        String expectedLogMessage = getExpectedSystemMessage() + INFO_SEPARATOR;
        assertEquals(expectedLogMessage, logMessage);

        logBuilder.addMessage(null);
        expectedLogMessage = expectedLogMessage + INFO_SEPARATOR + null;
        logMessage = logBuilder.toString();
        assertEquals(expectedLogMessage, logMessage);

        logBuilder.addMessage("Darth Vader, Yoda, Boba Fett, Han Solo");
        expectedLogMessage = expectedLogMessage + INFO_SEPARATOR + "Darth Vader, Yoda, Boba Fett, Han Solo";
        logMessage = logBuilder.toString();
        assertEquals(expectedLogMessage, logMessage);
    }

    @Test
    public void testAddException() {
        Exception exception = new NullPointerException("sorry");
        LogBuilder logBuilder = LogBuilder.createSystemMessage().addException("because I can", exception);
        String logMessage = logBuilder.toString();
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String expectedLogMessage = getExpectedSystemMessage() + INFO_SEPARATOR + EXCEPTION_REASON + NAME_VALUE_SEPARATOR
                + "because I can" + INFO_SEPARATOR + STACKTRACE + NAME_VALUE_SEPARATOR + sw.toString();
        assertEquals(expectedLogMessage, logMessage);
    }

    @Test
    public void testAddPerformedAction() {
        String action = "Build your own lightsaber";
        LogBuilder logBuilder = LogBuilder.createUserMessage().addAction(action);
        String logMessage = logBuilder.toString();
        String expectedLogMessage = getExpectedUserMessage() + INFO_SEPARATOR + ACTION + NAME_VALUE_SEPARATOR + action;
        assertEquals(expectedLogMessage, logMessage);

        logBuilder.addAction(null);
        logMessage = logBuilder.toString();
        expectedLogMessage = expectedLogMessage + INFO_SEPARATOR + ACTION + NAME_VALUE_SEPARATOR + null;
        assertEquals(expectedLogMessage, logMessage);
    }
}
