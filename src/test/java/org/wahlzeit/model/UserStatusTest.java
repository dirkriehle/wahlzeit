package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserStatusTest {

    /**
     *
     */
    @Test
    public void testGetFromInt() {
        assertSame(UserStatus.getFromInt(UserStatus.CREATED.asInt()), UserStatus.CREATED);
        assertSame(UserStatus.getFromInt(UserStatus.CONFIRMED.asInt()), UserStatus.CONFIRMED);
        assertSame(UserStatus.getFromInt(UserStatus.DISABLED.asInt()), UserStatus.DISABLED);
        assertSame(UserStatus.getFromInt(UserStatus.DISABLED2.asInt()), UserStatus.DISABLED2);
    }

    /**
     *
     */
    @Test
    public void testGetFromString() {
        assertEquals(UserStatus.getFromString(UserStatus.CREATED.asString()), UserStatus.CREATED);
        assertEquals(UserStatus.getFromString(UserStatus.CONFIRMED.asString()), UserStatus.CONFIRMED);
        assertEquals(UserStatus.getFromString(UserStatus.DISABLED.asString()), UserStatus.DISABLED);

        // DISABLED2.asString() -> DISABLED.asString()
        assertEquals(UserStatus.getFromString(UserStatus.DISABLED2.asString()), UserStatus.DISABLED);
    }

    /**
     *
     */
    @Test
    public void testIsConfirmed() {
        assertFalse(UserStatus.CREATED.isConfirmed());
        assertTrue(UserStatus.CONFIRMED.isConfirmed());
        assertFalse(UserStatus.DISABLED.isConfirmed());
        assertTrue(UserStatus.DISABLED2.isConfirmed());
    }

    /**
     *
     */
    @Test
    public void testIsDisabled() {
        assertFalse(UserStatus.CREATED.isDisabled());
        assertFalse(UserStatus.CONFIRMED.isDisabled());
        assertTrue(UserStatus.DISABLED.isDisabled());
        assertTrue(UserStatus.DISABLED2.isDisabled());
    }

    /**
     *
     */
    @Test
    public void testAsDisabled() {
        assertTrue(UserStatus.CREATED.asDisabled().isDisabled());
        assertTrue(UserStatus.CONFIRMED.asDisabled().isDisabled());
        assertTrue(UserStatus.DISABLED.asDisabled().isDisabled());
        assertTrue(UserStatus.DISABLED2.asDisabled().isDisabled());

        assertFalse(UserStatus.CREATED.asDisabled().isConfirmed());
        assertTrue(UserStatus.CONFIRMED.asDisabled().isConfirmed());
        assertFalse(UserStatus.DISABLED.asDisabled().isConfirmed());
        assertTrue(UserStatus.DISABLED2.asDisabled().isConfirmed());
    }

    /**
     *
     */
    @Test
    public void testAsConfirmed() {
        assertTrue(UserStatus.CREATED.asConfirmed().isConfirmed());
        assertTrue(UserStatus.CONFIRMED.asConfirmed().isConfirmed());
        assertTrue(UserStatus.DISABLED.asConfirmed().isConfirmed());
        assertTrue(UserStatus.DISABLED2.asConfirmed().isConfirmed());

        assertFalse(UserStatus.CREATED.asConfirmed().isDisabled());
        assertFalse(UserStatus.CONFIRMED.asConfirmed().isDisabled());
        assertTrue(UserStatus.DISABLED.asConfirmed().isDisabled());
        assertTrue(UserStatus.DISABLED2.asConfirmed().isDisabled());
    }

    /**
     *
     */
    @Test
    public void testAsEnabled() {
        assertFalse(UserStatus.CREATED.asEnabled().isDisabled());
        assertFalse(UserStatus.CONFIRMED.asEnabled().isDisabled());
        assertFalse(UserStatus.DISABLED.asEnabled().isDisabled());
        assertFalse(UserStatus.DISABLED2.asEnabled().isDisabled());

        assertFalse(UserStatus.DISABLED.asEnabled().asEnabled().isDisabled());

        assertFalse(UserStatus.CREATED.asEnabled().isConfirmed());
        assertTrue(UserStatus.CONFIRMED.asEnabled().isConfirmed());
        assertFalse(UserStatus.DISABLED.asEnabled().isConfirmed());
        assertTrue(UserStatus.DISABLED2.asEnabled().isConfirmed());
    }

}
