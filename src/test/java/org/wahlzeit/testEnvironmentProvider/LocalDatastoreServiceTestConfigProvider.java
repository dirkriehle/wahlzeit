package org.wahlzeit.testEnvironmentProvider;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.rules.ExternalResource;

/**
 * Provider for the GAE datastore environment.
 * Necessary for each Unit test that wants to load or store stuff in the datastore.
 *
 * Created by Lukas Hahmann on 22.05.15.
 */
public class LocalDatastoreServiceTestConfigProvider extends ExternalResource {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Override
    protected void before() throws Throwable {
        helper.setUp();
    }

    @Override
    protected void after() {
        helper.tearDown();
    }
}
