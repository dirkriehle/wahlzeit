package org.wahlzeit.testEnvironmentProvider;

import org.junit.rules.ExternalResource;
import org.wahlzeit.services.OfyService;

/**
 * Created by Lukas Hahmann on 29.05.15.
 */
public class RegisteredOfyEnvironmentProvider extends ExternalResource {
    @Override
    protected void before() throws Throwable {
        // make sure the static register part is done
        OfyService.factory();
    }
}
