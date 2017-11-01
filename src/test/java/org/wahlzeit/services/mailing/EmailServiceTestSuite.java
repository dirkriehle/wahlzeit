package org.wahlzeit.services.mailing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EmailServiceTest.class,
        EmailAddressTest.class
})
public class EmailServiceTestSuite {
}
