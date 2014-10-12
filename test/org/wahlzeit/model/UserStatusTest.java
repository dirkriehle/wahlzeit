package org.wahlzeit.model;

import junit.framework.*;

public class UserStatusTest extends TestCase {

	/**
	 * 
	 * @param name
	 */
	public UserStatusTest(String name) {
		super(name);
	}

	/**
	 * 
	 */
	public void testGetFromInt() {
		assertTrue(UserStatus.getFromInt(UserStatus.CREATED.asInt()) == UserStatus.CREATED);
		assertTrue(UserStatus.getFromInt(UserStatus.CONFIRMED.asInt()) == UserStatus.CONFIRMED);
		assertTrue(UserStatus.getFromInt(UserStatus.DISABLED.asInt()) == UserStatus.DISABLED);
		assertTrue(UserStatus.getFromInt(UserStatus.DISABLED2.asInt()) == UserStatus.DISABLED2);
	}

	/**
	 * 
	 */
	public void testGetFromString() {
		assertTrue(UserStatus.getFromString(UserStatus.CREATED.asString()).equals(UserStatus.CREATED));
		assertTrue(UserStatus.getFromString(UserStatus.CONFIRMED.asString()).equals(UserStatus.CONFIRMED));
		assertTrue(UserStatus.getFromString(UserStatus.DISABLED.asString()).equals(UserStatus.DISABLED));

		// DISABLED2.asString() -> DISABLED.asString()
		assertTrue(UserStatus.getFromString(UserStatus.DISABLED2.asString()).equals(UserStatus.DISABLED));
	}

	/**
	 * 
	 */
	public void testIsConfirmed() {
		assertFalse(UserStatus.CREATED.isConfirmed());
		assertTrue(UserStatus.CONFIRMED.isConfirmed());
		assertFalse(UserStatus.DISABLED.isConfirmed());
		assertTrue(UserStatus.DISABLED2.isConfirmed());
	}

	/**
	 * 
	 */
	public void testIsDisabled() {
		assertFalse(UserStatus.CREATED.isDisabled());
		assertFalse(UserStatus.CONFIRMED.isDisabled());
		assertTrue(UserStatus.DISABLED.isDisabled());
		assertTrue(UserStatus.DISABLED2.isDisabled());
	}

	/**
	 * 
	 */
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
