package org.wahlzeit.testEnvironmentProvider;

import org.junit.rules.ExternalResource;
import org.wahlzeit.model.config.DomainCfg;
import org.wahlzeit.services.CloudDataBase;

/**
 * A test setup class.
 * @review
 */
public class RegisteredOfyEnvironmentProvider extends ExternalResource {
    @Override
    protected void before() throws Throwable {
        DomainCfg.initializeDomain();
        // accessing the class will initialize static areas, so make sure the static register part is done
        CloudDataBase.getMgmtActions();
    }
}
