package org.wahlzeit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.wahlzeit.handlers.HandlersTestSuite;
import org.wahlzeit.model.ModelTestSuite;
import org.wahlzeit.services.ServicesTestSuit;
import org.wahlzeit.utils.UtilsTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HandlersTestSuite.class,
        ModelTestSuite.class,
        ServicesTestSuit.class,
        UtilsTestSuite.class
})
public class WahlzeitTestSuite {
}