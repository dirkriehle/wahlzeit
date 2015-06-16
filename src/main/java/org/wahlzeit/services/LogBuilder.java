package org.wahlzeit.services;

import org.wahlzeit.model.Client;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.model.UserSession;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Builder class for log messages, that ensures that log messages are all formatted equally.
 * <p/>
 * Google App Engine as a PaaS has certain restrictions depending their services, hence a default Java logging
 * <code>Formatter</code> could not be used
 * <p/>
 * (see https://stackoverflow.com/questions/30345665/how-to-customize-logging-for-google-app-engine-java).
 * <p/>
 * Created by Lukas Hahmann on 26.05.15.
 */
public class LogBuilder {

    protected static final String LEVEL = "level";
    protected static final String USER_LEVEL = "ul";
    protected static final String SYSTEM_LEVEL = "sl";
    protected static final String SESSION = "session";
    protected static final String CLIENT = "client";
    protected static final String MESSAGE = "message";
    protected static final String ACTION = "action";
    protected static final String NAME_VALUE_SEPARATOR = "=";
    protected static final String INFO_SEPARATOR = ", ";
    protected static final String EXCEPTION_REASON = "exception reason";
    protected static final String STACKTRACE = "stacktrace";

    protected StringBuilder logMessage;


    protected LogBuilder() {
        logMessage = new StringBuilder();
    }


    // create-methods --------------------------------------------------------------------------------------------------

    /**
     * @methodtype factory
     * <p/>
     * Creates a LogBuilder Object and adds the Level (user level), the current <code>HttpSession</code>, and the
     * clients name.
     */
    public static LogBuilder createUserMessage() {
        return doCreateMessage(USER_LEVEL);
    }

    /**
     * @methodtype factory
     * <p/>
     * Primitive Method that creates a LogBuilder Object and adds the Level, the current <code>HttpSession</code>, and
     * the clients name.
     */
    protected static LogBuilder doCreateMessage(String level) {
        LogBuilder result = new LogBuilder();
        Session session = SessionManager.getThreadLocalSession();
        String sessionName;
        String clientName;
        if (session != null) {
            sessionName = session.getName();
            Client client = UserManager.getInstance().getClientById(session.getClientId());
            if (client != null) {
                clientName = client.getNickName();
            } else {
                clientName = UserSession.ANONYMOUS_CLIENT;
            }
        } else {
            sessionName = Session.NO_SESSION;
            clientName = UserSession.ANONYMOUS_CLIENT;
        }

        result.add(LEVEL + NAME_VALUE_SEPARATOR + level);
        result.add(SESSION + NAME_VALUE_SEPARATOR + sessionName);
        result.add(CLIENT + NAME_VALUE_SEPARATOR + clientName);

        return result;
    }

    /**
     * @methodtype set
     */
    protected void add(String logMessagePart) {

        assert logMessage != null;

        if (logMessage.length() == 0) {
            logMessage.append(logMessagePart);
        } else {
            logMessage.append(INFO_SEPARATOR);
            logMessage.append(logMessagePart);
        }
    }


    // add-methods -----------------------------------------------------------------------------------------------------

    /**
     * @methodtype factory
     * <p/>
     * Creates a LogBuilder Object and adds the Level (system level), the current <code>HttpSession</code>, and the
     * clients name.
     */
    public static LogBuilder createSystemMessage() {
        return doCreateMessage(SYSTEM_LEVEL);
    }

    /**
     * @methodtype mutate
     * <p/>
     * Adds the following to the LogMessage: ", <name>=<value>".
     */
    public LogBuilder addParameter(String name, int value) {
        add(name + NAME_VALUE_SEPARATOR + String.valueOf(value));
        return this;
    }

    /**
     * @methodtype mutate
     * <p/>
     * Adds the following to the LogMessage: ", <name>=<value>".
     */
    public LogBuilder addParameter(String name, boolean value) {
        add(name + NAME_VALUE_SEPARATOR + String.valueOf(value));
        return this;
    }

    /**
     * @methodtype mutate
     * <p/>
     * Adds the following to the LogMessage: ", <name>=<value>".
     */
    public LogBuilder addParameter(String name, String value) {
        add(name + NAME_VALUE_SEPARATOR + value);
        return this;
    }

    /**
     * @methodtype mutate
     * <p/>
     * Adds the following to the LogMessage: ", <name>=<value>.toString()".
     */
    public LogBuilder addParameter(String name, Object value) {
        add(name + NAME_VALUE_SEPARATOR + value.toString());
        return this;
    }

    /**
     * @methodtype mutate
     * <p/>
     * Adds the message to the LogMessage: ", <message>".
     */
    public LogBuilder addMessage(String message) {
        add(message);
        return this;
    }

    /**
     * @methodtype mutate
     * <p/>
     * Adds the stacktrace and the <code>exceptionMessage</code> to the log message.
     */
    public LogBuilder addException(String exceptionMessage, Throwable throwable) {
        add(EXCEPTION_REASON + NAME_VALUE_SEPARATOR + exceptionMessage);
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        add(STACKTRACE + NAME_VALUE_SEPARATOR + sw.toString());
        return this;
    }


    // build-method ----------------------------------------------------------------------------------------------------

    /**
     * @methodtype mutate
     * <p/>
     * Adds the info that the action is performed the log message: "action=<action>".
     */
    public LogBuilder addAction(String action) {
        add(ACTION + NAME_VALUE_SEPARATOR + action);
        return this;
    }


    // hidden setter and getter methods --------------------------------------------------------------------------------

    /**
     * @methodtype conversion
     * <p/>
     * Puts everything together that has been added to the LogMessage before.
     */
    @Override
    public String toString() {

        assert logMessage != null;
        assert logMessage.length() > 0;

        return logMessage.toString();
    }
}
