package org.wahlzeit.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import org.wahlzeit.model.Client;
import org.wahlzeit.model.Guest;
import org.wahlzeit.model.UserManager;
import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.OfyService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

/**
 * This servlet is run to cleanup expired sessions.
 */
public class SessionCleanupServlet extends HttpServlet {

    private static final String SESSION_ENTITY_TYPE = "_ah_SESSION";
    private static final String EXPIRES_PROP = "_expires";

    private static final Logger log = Logger.getLogger(SessionCleanupServlet.class.getName());

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        if ("clear".equals(request.getQueryString())) {
            clearAll(response);
        }
    }

    /**
     * Clears all {@link HttpSession}s in the datastore that are expired and if they are guest sessions, deletes the
     * corresponding {@link Guest} object, too.
     */
    private void clearAll(HttpServletResponse response) {

        List<Object> killList = OfyService.ofy().load().
                kind(SESSION_ENTITY_TYPE).
                filter(EXPIRES_PROP + " <", System.currentTimeMillis()).list();

        log.config(LogBuilder.createSystemMessage().
                addParameter("number of old sessions to delete from datastore", killList.size()).toString());

        try {
            for (Object o : killList) {
                Entity httpSessionEntity = (Entity) o;
                Key key = httpSessionEntity.getKey();
                // GAE does not use session id as key name, instead "_ahs<sessionId>"
                String sessionId = key.getName().substring(4);
                log.config(LogBuilder.createSystemMessage().
                        addAction("delete session").
                        addParameter("session id", sessionId).toString());
                Client client = UserManager.getInstance().getClientByHttpSessionId(sessionId);
                if (client != null && client instanceof Guest) {
                    UserManager.getInstance().deleteClient(client);
                }
                OfyService.ofy().delete().entity(httpSessionEntity).now();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.config(LogBuilder.createSystemMessage().
                    addException("problem when deleting session and guest", e).toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
