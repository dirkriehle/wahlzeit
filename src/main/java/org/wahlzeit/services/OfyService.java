package org.wahlzeit.services;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.wahlzeit.model.*;
import org.wahlzeit.model.gurkenDomain.GurkenPhoto;
import org.wahlzeit.model.persistence.DatastoreAdapter.ImageWrapper;

/**
 * A badly named class, to be renamed to ObjectifyService first, something better later.
 * @review
 */
public class OfyService {

    /**
     * Register all entities at startup
     */
    static {
        factory().register(GurkenPhoto.class);
        factory().register(Photo.class);
        factory().register(Globals.class);
        factory().register(Tag.class);
        factory().register(User.class);
        factory().register(Administrator.class);
        factory().register(Moderator.class);
        factory().register(Client.class);
        factory().register(Guest.class);
        factory().register(PhotoCase.class);
        factory().register(ImageWrapper.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
