package org.wahlzeit.services;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.wahlzeit.model.Administrator;
import org.wahlzeit.model.Client;
import org.wahlzeit.model.Globals;
import org.wahlzeit.model.Guest;
import org.wahlzeit.model.Moderator;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoCase;
import org.wahlzeit.model.Tag;
import org.wahlzeit.model.User;

/**
 * Created by Lukas Hahmann on 30.03.15.
 */
public class OfyService {

    /**
     * Register all entities at startup
     */
    static {
        factory().register(Photo.class);
        factory().register(Globals.class);
        factory().register(Tag.class);
        factory().register(User.class);
        factory().register(Administrator.class);
        factory().register(Moderator.class);
        factory().register(Client.class);
        factory().register(Guest.class);
        factory().register(PhotoCase.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
