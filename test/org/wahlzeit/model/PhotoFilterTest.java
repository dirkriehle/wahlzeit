package org.wahlzeit.model;

import junit.framework.*;

public class PhotoFilterTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PhotoFilterTest.class);
	}

	public PhotoFilterTest(String name) {
		super(name);
	}
	
	public void testConstructor()
	{
		PhotoFilter pf = new PhotoFilter();
		assertNotNull(pf);
		
		//Check properties after creation
		assertEquals("", pf.userName);
		assertEquals(Tags.EMPTY_TAGS, pf.tags);
		
		assertEquals(0,pf.processedPhotoIds.size());
		assertEquals(0,pf.displayablePhotoIds.size());
	}
	
	
	public void testStaticProperties()
	{
		assertEquals("userName", PhotoFilter.USER_NAME);
		assertEquals("tags", PhotoFilter.TAGS);
	}
	
	public void testClear()
	{
		PhotoFilter pf = new PhotoFilter();
		pf.setUserName("test");
		assertEquals("test", pf.getUserName());
		
		pf.setTags(new Tags("test"));
		assertEquals("test", pf.getTags().asString());
		
		//pf.displayablePhotoIds.add(new PhotoId(3));
		//assertTrue(pf.displayablePhotoIds.size() == 1);
		
		pf.processedPhotoIds.add(new PhotoId(2));
		assertTrue(pf.processedPhotoIds.size() == 1);
		
		pf.clear();
		
		assertEquals("", pf.getUserName());
		assertEquals(Tags.EMPTY_TAGS, pf.getTags());
		assertTrue(pf.displayablePhotoIds.isEmpty());
		assertTrue(pf.processedPhotoIds.isEmpty());
	}
	
	
}
