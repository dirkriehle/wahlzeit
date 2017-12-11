/**
 * 
 */
package org.wahlzeit.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class LocationTest {

	CartesianCoordinate cartCord = null;
	Location cartlo1,cartlo2,cartlo3,cartlo4,cartlo5, spherloc1, spherloc2= null;
    SphericCoordinate spherCord = null;	
   
	
	
	
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		// Add testing data for the Cartesian coordinate
		cartCord = new CartesianCoordinate(10, 15, 20);	
	    cartlo1 = new Location (1,2,3);
	    cartlo2 = new Location (0,0,0);
	    cartlo3 = new Location (-12,15,0);
	    cartlo4 = new Location ();
	    cartlo5 = new Location (cartCord);
	
	   // add testing data for the spheric coordinate
	    
	    spherCord = new SphericCoordinate (100,200);
	    spherloc1 = new Location (50,60);
	    spherloc2 = new Location (spherCord);
				
	}

	/**
	 * 	 * test method for constructed location with no coordinate
	 */
	@Test
	public void testNoCordinateLocation() {
		
	assert(((CartesianCoordinate) cartlo4.cord).getX()==0);
	assert(((CartesianCoordinate) cartlo4.cord).getY()==0); 
	assert(((CartesianCoordinate) cartlo4.cord).getZ()==0);
	}

	/**
	 * test method for constructed location with x and y
	 */
	@Test
	public void testXYCordinateLocation() {
		assert(((CartesianCoordinate) cartlo3.cord).getX()==-12);
		assert(((CartesianCoordinate) cartlo3.cord).getY()==15); 
		assert(((CartesianCoordinate) cartlo3.cord).getZ()==0);
		
	}

	/**
	 * test method for constructed location with x,y and z
	 */
	@Test
	public void testXYZCordinateLocation() {
		assert(((CartesianCoordinate) cartlo1.cord).getX()==1);
		assert(((CartesianCoordinate) cartlo1.cord).getY()==2); 
		assert(((CartesianCoordinate) cartlo1.cord).getZ()==3);
		
	}

	/**
	 * test method for constructed location with coordinate
	 */
	@Test
	public void testLocationCoordinate() {
		
		assert(((CartesianCoordinate) cartlo5.cord).getX()==10);
		assert(((CartesianCoordinate) cartlo5.cord).getY()==15); 
		assert(((CartesianCoordinate) cartlo5.cord).getZ()==20);
	}
	
	@Test
	public void testLocationSphericalCoordinate() {
		
		assert(((SphericCoordinate) spherloc2.cord).getLatitude() ==100);
		assert(((SphericCoordinate) spherloc2.cord).getLongitude()==200); 
		assert(((SphericCoordinate) spherloc2.cord).getradius()==6371);
	}
	
	/**
	 * test method for get distance between the same location
	 */
	
	@Test
	public void testGetDistanceForTheSameLocation() {
		
		assert(cartlo1.cord.getCartesianDistance(cartlo1.cord) == 0);

	}
	
	/**
	 * test method for get distance between two different locations
	 */
	
	@Test
	public void testGetDistanceForDifferentLocation() {

		assertEquals(23.2163, cartlo1.cord.getCartesianDistance (cartlo5.cord), 0.0001);

	}
	
	/**
	 * test method for get distance between location in zero origin and location in minus coordinate
	 */
	
	@Test
	public void testGetDistanceForZeroAndMinusLocation() {

		assertEquals(19.2094, cartlo3.cord.getCartesianDistance(cartlo4.cord), 0.0001);

	}
	
	
	
	/**
	 * test method if the two locations are equal
	 */
	
	@Test
	public void testIsEqual() {
		
		assert(cartlo3.cord.isEqual(cartlo3.cord)==true);

	}
	
	
	
	/**
	 * test method if the two locations are not Equal
	 */
	
	@Test
	public void testIsNotEqual() {
		
		assert(cartlo3.cord.isEqual(cartlo5.cord)==false);

	}
	
	/**
	 * test method if the two locations are equal
	 */
	
	@Test
	public void testEquals() {
		
		assert(cartlo3.cord.isEqual(cartlo3.cord)==true);

	}
	/**
	test get  distance between two spherical location 
	 */
	
	@Test
	public void testGetSphericDistance() {
		
		assertEquals(3655,spherloc1.cord.getSphericDistance(spherloc2.cord),0001);

	}
	
	/**
	test is equal between two spherical locations
	 */
	
	@Test
	public void testIsEqualSpheric() {
		
		assert(spherloc1.cord.isEqual(spherloc2.cord)==false);

	}	
	
}

	


