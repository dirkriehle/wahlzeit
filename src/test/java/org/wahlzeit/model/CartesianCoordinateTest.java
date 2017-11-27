package org.wahlzeit.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CartesianCoordinateTest {
	
	CartesianCoordinate cartCord1 = new CartesianCoordinate(1,2,3);
	CartesianCoordinate cartCord2 = new CartesianCoordinate(0,0,0);
	CartesianCoordinate cartCord3 = new CartesianCoordinate(-12,15,0);
	CartesianCoordinate cartCord4 = new CartesianCoordinate(10,15,20);

	
	@Test
	
	//reference to calculation result 
	//https://www.calculatorsoup.com/calculators/geometry-solids/distance-two-points.php
	public void testGetCartesianDistance() {
		 double result  = cartCord1.getCartesianDistance(cartCord2);
		 assertEquals(3.741657, result, 0.0001);
	}

	@Test
	//reference to calculation result 
	//http://keisan.casio.com/exec/system/1359533867
	public void testAsSphericCoordinate() {
		SphericCoordinate spherCord = cartCord4.asSphericCoordinate();
		double latitude = spherCord.getLatitude();
		double longitude = spherCord.getLongitude();
		double radius = spherCord.getradius();
		boolean conversionResult = spherCord.isEqualOfDouble(latitude,0.98279372324733)
				&&spherCord.isEqualOfDouble(longitude,0.73358132364008)
				&&spherCord.isEqualOfDouble(radius,26.925824035673);
		assertTrue(conversionResult);			
	}

	@Test
	public void testIsEqual() {
		assertFalse(cartCord3.isEqual(cartCord4));
	}
	@Test
	public void testGetDistance() {
		
		double result  = cartCord1.getDistance(cartCord2);
		assertEquals(3.741657, result, 0.0001);
		
	}

}
