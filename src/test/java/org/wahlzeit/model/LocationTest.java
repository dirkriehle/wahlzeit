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
		
	assert(cartlo4.cartCord.getx()==0);
	assert(cartlo4.cartCord.gety()==0); 
	assert(cartlo4.cartCord.getz()==0);
	}

	/**
	 * test method for constructed location with x and y
	 */
	@Test
	public void testXYCordinateLocation() {
		assert(cartlo3.cartCord.getx()==-12);
		assert(cartlo3.cartCord.gety()==15); 
		assert(cartlo3.cartCord.getz()==0);
		
	}

	/**
	 * test method for constructed location with x,y and z
	 */
	@Test
	public void testXYZCordinateLocation() {
		assert(cartlo1.cartCord.getx()==1);
		assert(cartlo1.cartCord.gety()==2); 
		assert(cartlo1.cartCord.getz()==3);
		
	}

	/**
	 * test method for constructed location with coordinate
	 */
	@Test
	public void testLocationCoordinate() {
		
		assert(cartlo5.cartCord.getx()==10);
		assert(cartlo5.cartCord.gety()==15); 
		assert(cartlo5.cartCord.getz()==20);
	}
	
	/**
	 * test method for get distance between the same location
	 */
	
	@Test
	public void testGetDistanceForTheSameLocation() {
		
		assert(cartlo1.cartCord.getCartesianDistacnce(cartlo1.cartCord) == 0);

	}
	
	/**
	 * test method for get distance between two different locations
	 */
	
	@Test
	public void testGetDistanceForDifferentLocation() {

		assertEquals(23.2163, cartlo1.cartCord.getCartesianDistacnce (cartlo5.cartCord), 0.0001);

	}
	
	/**
	 * test method for get distance between location in zero origin and location in minus coordinate
	 */
	
	@Test
	public void testGetDistanceForZeroAndMinusLocation() {

		assertEquals(19.2094, cartlo3.cartCord.getCartesianDistacnce(cartlo4.cartCord), 0.0001);

	}
	
	
	
	/**
	 * test method if the two locations are equal
	 */
	
	@Test
	public void testIsEqual() {
		
		assert(cartlo3.cartCord.isEqual(cartlo3.cartCord)==true);

	}
	
	
	
	/**
	 * test method if the two locations are not Equal
	 */
	
	@Test
	public void testIsNotEqual() {
		
		assert(cartlo3.cartCord.isEqual(cartlo5.cartCord)==false);

	}
	
	/**
	 * test method if the two locations are equal
	 */
	
	@Test
	public void testEquals() {
		
		assert(cartlo3.cartCord.isEqual(cartlo3.cartCord)==true);

	}
	/**
	test get  distance between two spherical location 
	 */
	
	@Test
	public void testGetSphericDistance() {
		
		assertEquals(3655,spherloc1.spherCord.getSphericDistacnce(spherloc2.spherCord),0001);

	}
	
	/**
	test is equal between two spherical locations
	 */
	
	@Test
	public void testIsEqualSpheric() {
		
		assert(spherloc1.spherCord.isEqual(spherloc2.spherCord)==false);

	}	
	
}

	


