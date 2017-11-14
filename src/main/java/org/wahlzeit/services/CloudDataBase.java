package org.wahlzeit.services;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.wahlzeit.model.*;
import org.wahlzeit.model.config.DomainCfg;
import org.wahlzeit.model.persistence.DatastoreAdapter.ImageWrapper;

/**
 * A badly named class, to be renamed to ObjectifyService first, something better later. => Done
 * @review
 */
public class CloudDataBase {

    /**
     * Register all entities at startup
     */
    static {
        DomainCfg.registerDomainObjects();
        getMgmtActions().register(Photo.class);
        getMgmtActions().register(Globals.class);
        getMgmtActions().register(Tag.class);
        getMgmtActions().register(User.class);
        getMgmtActions().register(Administrator.class);
        getMgmtActions().register(Moderator.class);
        getMgmtActions().register(Client.class);
        getMgmtActions().register(Guest.class);
        getMgmtActions().register(PhotoCase.class);
        getMgmtActions().register(ImageWrapper.class);
    }

    /**
     * @return Service to perform operational Actions on files (to be) stored in the DB e.g. read/write
     */
    public static Objectify getOpActions() {
        return ObjectifyService.ofy();
    }

    /**
     * @return Service to perform management or meta Actions on files (to be) stored in the DB e.g. register/getMetaData
     */
    public static ObjectifyFactory getMgmtActions() {
        return ObjectifyService.factory();
    }
}
