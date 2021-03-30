package org.wahlzeit_revisited;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.wahlzeit_revisited.main.DatabaseMain;
import org.wahlzeit_revisited.utils.SysConfig;

import java.time.Instant;

/**
 * Setups the database connections for tests
 */
public class BaseModelTest {

    private static DatabaseMain databaseMain;

    @BeforeClass
    public static void setup() throws Exception {
        databaseMain = new DatabaseMain(new SysConfig());
        databaseMain.startUp();
    }

    @AfterClass
    public static void tearDown() {
        databaseMain.shutDown();
    }

    /**
     * Builds an unique email, so tests can get repeated without resetting the database
     *
     * @param name eg the test name
     * @return an unique username, prefix with the current timestamp
     */
    protected String buildUniqueName(String name) {
        return "unique" + Long.toHexString(Instant.now().toEpochMilli()) + name;
    }

    /**
     * Builds an unique email, so tests can get repeated without resetting the database
     *
     * @param identifier eg the test name
     * @return an unique email, prefix with the current timestamp
     */
    protected String buildUniqueEmail(String identifier) {
        return "unique" + Long.toHexString(Instant.now().toEpochMilli()) + identifier + "@fau.de";
    }

}
