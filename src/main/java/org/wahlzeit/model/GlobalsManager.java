package org.wahlzeit.model;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.servlets.AbstractServlet;

import java.util.logging.Logger;

import static org.wahlzeit.services.OfyService.ofy;

/**
 * Manager that cares about the global variables.
 * It is used from the outside by the following two methods:
 * @see #loadGlobals()
 * @see #saveGlobals()
 *
 * Created by Lukas Hahmann on 08.04.15.
 */
public class GlobalsManager extends ObjectManager {

    private static final Logger log = Logger.getLogger(GlobalsManager.class.getName());
    /**
     *
     */
    private static GlobalsManager instance = new GlobalsManager();

    /**
     * @methodtype command
     * Loads all global variables and stores them in their corresponding classes.
     */
    public void loadGlobals() {
        initGlobals();
        Globals globals = ObjectifyService.run(new Work<Globals>() {
            @Override
            public Globals run() {
                return readObject(Globals.class, Globals.DEAULT_ID);
            }
        });
        log.info(globals.asString());

        UserManager.getInstance().setLastClientId(globals.getLastUserId());
        PhotoId.setCurrentIdFromInt(globals.getLastPhotoId());
        Case.setLastCaseId(new CaseId(globals.getLastCaseId()));
        AbstractServlet.setLastSessionId(globals.getLastSessionId());
    }

    /**
     * @methodtype wrapper
     */
    private void initGlobals() {
        if (!GlobalsManager.getInstance().hasGlobals()) {
            createDefaultGlobals();
        }
    }

    /**
     * @methodtype boolean querry
     */
    private boolean hasGlobals() {
        return ObjectifyService.run(new Work<Boolean>() {
            @Override
            public Boolean run() {
                return ofy().load().type(Globals.class).first().now() != null;
            }
        });
    }

    /**
     * @methodtype get
     */
    public static GlobalsManager getInstance() {
        return instance;
    }

    /**
     * @methodtype command
     */
    private void createDefaultGlobals() {
        ObjectifyService.run(new Work<Boolean>() {
            @Override
            public Boolean run() {
                Globals globals = new Globals();
                globals.setLastUserId(Globals.DEAULT_ID);
                globals.setLastPhotoId(0);
                globals.setLastCaseId(0);
                globals.setLastSessionId(0);
                ofy().save().entity(globals).now();
                return null;
            }
        });
    }

    /**
     * @methodtype command
     * Saves all global variables.
     */
    public synchronized void saveGlobals() {
        final Globals globals = new Globals();
        globals.setLastUserId(UserManager.getInstance().getLastClientId());
        globals.setLastPhotoId(PhotoId.getCurrentIdAsInt());
        globals.setLastCaseId(Case.getLastCaseId().asInt());
        globals.setLastSessionId(AbstractServlet.getLastSessionId());
        log.info(globals.asString());

        ObjectifyService.run(new Work<Void>() {
            @Override
            public Void run() {
                writeObject(globals);
                return null;
            }
        });
    }
}
