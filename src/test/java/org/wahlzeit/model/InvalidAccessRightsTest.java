package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Check all the behavior of <code>AccessRights</code> in case of a failure.
 *
 * Created by Lukas Hahmann on 25.05.15.
 */
public class InvalidAccessRightsTest {

    @Test(expected=IllegalArgumentException.class)
    public void negativeIndexShouldThrowException() {
        AccessRights.getFromInt(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void tooBigIndexShouldThrowException() {
        AccessRights.getFromInt(5);
    }

    @Test
    public void invalidStringsShouldThrowException() {
        doInvalidStringShouldThrowException(null);
        doInvalidStringShouldThrowException("None");
        doInvalidStringShouldThrowException("Guest");
        doInvalidStringShouldThrowException("User");
        doInvalidStringShouldThrowException("Moderator");
        doInvalidStringShouldThrowException("Administrator");
    }

    protected void doInvalidStringShouldThrowException(String invalidString) {
        try {
            AccessRights.getFromString(invalidString);
            fail(".getFromString(" + invalidString + ") should throw IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
        }
        catch (Exception e) {
            fail(".getFromString(" + invalidString + ") should throw IllegalArgumentException not " + e.getClass().toString());
        }
    }

}
