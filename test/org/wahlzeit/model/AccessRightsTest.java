package org.wahlzeit.model;

import com.mchange.util.AssertException;

import junit.framework.TestCase;

public class AccessRightsTest extends TestCase {
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TagsTest.class);
	}
	
	
	protected void setUp() throws Exception {
		super.setUp();
		
	}
	
	public void testGetFromInt(){
		assertSame( AccessRights.NONE , AccessRights.getFromInt(0) );
		assertSame( AccessRights.GUEST , AccessRights.getFromInt(1) );
		assertSame( AccessRights.USER , AccessRights.getFromInt(2) );
		assertSame( AccessRights.MODERATOR , AccessRights.getFromInt(3) );
		assertSame( AccessRights.ADMINISTRATOR , AccessRights.getFromInt(4) );
		
		try{
			 AccessRights.getFromInt(-1);
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		
		try{
			 AccessRights.getFromInt(5);
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}


	}
	
	public void testGetFromString(){
		assertSame( AccessRights.NONE , AccessRights.getFromString("none") );
		assertSame( AccessRights.GUEST , AccessRights.getFromString("guest") );
		assertSame( AccessRights.USER , AccessRights.getFromString("user") );
		assertSame( AccessRights.MODERATOR , AccessRights.getFromString("moderator") );
		assertSame( AccessRights.ADMINISTRATOR , AccessRights.getFromString("administrator") );
		
		try{
			 AccessRights.getFromString(null);
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		
		// We specify that the rights description has to be passed as a lower-case string
		// TODO change to a separate @Test(expected=IllegalArgumentException.class) when migrating to JUnit 4 for readability purpose
		try{
			 AccessRights.getFromString("None");
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		
		try{
			 AccessRights.getFromString("Guest");
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		
		try{
			 AccessRights.getFromString("User");
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		
		try{
			 AccessRights.getFromString("Moderator");
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		
		try{
			 AccessRights.getFromString("Administrator");
			 fail ("getFromString() method did not throw IllegalArgumentException");
		} catch (Throwable ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}

	}
	
	public void testHasRights(){
		assertTrue(AccessRights.hasRights(AccessRights.ADMINISTRATOR, AccessRights.MODERATOR));
		assertTrue(AccessRights.hasRights(AccessRights.MODERATOR, AccessRights.USER));
		assertTrue(AccessRights.hasRights(AccessRights.USER, AccessRights.GUEST));
		assertTrue(AccessRights.hasRights(AccessRights.GUEST, AccessRights.NONE));
		assertTrue(AccessRights.hasRights(AccessRights.NONE, AccessRights.NONE));
		// hasRights() implements a transitive relation -> all cases covered
		assertFalse(AccessRights.hasRights(AccessRights.NONE, AccessRights.GUEST));
		assertFalse(AccessRights.hasRights(AccessRights.GUEST, AccessRights.USER));
		assertFalse(AccessRights.hasRights(AccessRights.USER, AccessRights.MODERATOR));
		assertFalse(AccessRights.hasRights(AccessRights.MODERATOR, AccessRights.ADMINISTRATOR));
		
	}
	

}
