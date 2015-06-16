package org.wahlzeit.model;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.RegisteredOfyEnvironmentProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link Guest}.
 * <p/>
 * Created by Lukas Hahmann on 29.05.15.
 */
public class GuestTest {

    @ClassRule
    public static RuleChain ruleChain = RuleChain.
            outerRule(new LocalDatastoreServiceTestConfigProvider()).
            around(new RegisteredOfyEnvironmentProvider());


    @Test
    public void testNameGeneration() {
        int clientId = ClientManager.lastClientId.intValue();
        assertNewGuestHasId(++clientId);
        assertNewGuestHasId(++clientId);
        // creation of user should not consume a next id
        ObjectifyService.run(new Work<Void>() {
            @Override
            public Void run() {
                new User("1337", "han", "star@wa.rs");
                return null;
            }
        });
        assertNewGuestHasId(++clientId);

        clientId -= 3;
        // test if GUEST_PREFIX == guest#
        testGetGuestFromUserManager("guest#" + ++clientId);
        testGetGuestFromUserManager(Guest.GUEST_PREFIX + ++clientId);
        testGetGuestFromUserManager(Guest.GUEST_PREFIX + ++clientId);
    }

    protected void assertNewGuestHasId(int id) {
        Guest testGuest = ObjectifyService.run(new Work<Guest>() {
            @Override
            public Guest run() {
                return new Guest();
            }
        });
        String userName = testGuest.getId();
        String expectedUserName = Guest.GUEST_PREFIX + id;
        assertEquals(expectedUserName, userName);
    }

    protected void testGetGuestFromUserManager(String name) {
        assertNotNull(UserManager.getInstance().getClientById(name));
    }
}
