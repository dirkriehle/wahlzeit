/**
 * 
 */
package org.wahlzeit.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mai
 *
 */
public class LocationTest {

	Coordinate cord = null;
	Location l1,l2,l3,l4,l5 = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	     cord = new Coordinate(10, 15, 20);	
	      l1 = new Location (1,2,3);
	      l2 = new Location (0,0,0);
	      l3 = new Location (-12,15);
	      l4 = new Location ();
	      l5 = new Location (cord);
	
	
				
	}

	/**
	 * 	 * Test method for constructed location with no coordinate
	 */
	@Test
	public void testNoCordinateLocation() {
		
	assert(l4.cord.getx()==0);
	assert(l4.cord.gety()==0); 
	assert(l4.cord.getz()==0);
	}

	/**
	 * Test method for constructed location with x and y
	 */
	@Test
	public void testXYCordinateLocation() {
		assert(l3.cord.getx()==-12);
		assert(l3.cord.gety()==15); 
		assert(l3.cord.getz()==0);
		
	}

	/**
	 * Test method for constructed location with x,y and z
	 */
	@Test
	public void testXYZCordinateLocation() {
		assert(l3.cord.getx()==1);
		assert(l3.cord.gety()==2); 
		assert(l3.cord.getz()==3);
		
	}

	/**
	 * Test method for constructed location with coordinate
	 */
	@Test
	public void testLocationCoordinate() {
		
		assert(l3.cord.getx()==1);
		assert(l3.cord.gety()==2); 
		assert(l3.cord.getz()==3);
	}
	
	/**
	 * Test method for get distance between the same location
	 */
	
	@Test
	public void TestGetDistanceForTheSameLocation() {
		
		assert(l1.cord.getDitacnce(cord) == 0);

	}
	
	/**
	 * Test method for get distance between two different locations
	 */
	
	@Test
	public void TestGetDistanceForDifferentLocation() {
		
		assert(l1.cord.getDitacnce(l5.cord)==23.216374);

	}
	
	/**
	 * Test method for get distance between location in zero origin and location in minus coordinate
	 */
	
	@Test
	public void TestGetDistanceForZeroAndMinusLocation() {
		
		assert(l3.cord.getDitacnce(l4.cord)==19.209373);

	}
	
	
	
	/**
	 * Test method if the two locations are equal
	 */
	
	@Test
	public void TestIsEqual() {
		
		assert(l3.cord.isEqual(l3.cord)==true);

	}
	
	
	
	/**
	 * Test method if the two locations are not Equal
	 */
	
	@Test
	public void TestIsNotEqual() {
		
		assert(l3.cord.isEqual(l5.cord)==false);

	}
	
	/**
	 * Test method if the two locations are equal
	 */
	
	@Test
	public void TestEquals() {
		
		assert(l3.cord.isEqual(l3.cord)==true);

	}
	
	
	
	/**
	 * Test method if the two locations are not Equal
	 */
	
	@Test
	public void TestNotEquals() {
		
		assert(l3.cord.isEqual(l5.cord)==false);

	}



}