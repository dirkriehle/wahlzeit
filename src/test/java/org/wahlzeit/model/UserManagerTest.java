package org.wahlzeit.model;

import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.UserSessionProvider;

/**
 * Test class for {@link UserManager}.
 * <p/>
 * Created by Lukas Hahmann on 29.05.15.
 */
public class UserManagerTest {

    public UserSessionProvider userSessionProvider = new UserSessionProvider();

    public LocalDatastoreServiceTestConfigProvider localDatastoreServiceTestConfigProvider = new LocalDatastoreServiceTestConfigProvider();

    @Rule
    public TestRule chain = RuleChain
            .outerRule(localDatastoreServiceTestConfigProvider)
            .around(userSessionProvider);

}
