package org.wahlzeit.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class SphericCoordinateTest  {
	
	SphericCoordinate spherAlx = new SphericCoordinate(31.2000924,29.9187387); 
	SphericCoordinate spherBer= new SphericCoordinate(52.5200066,13.404954);
	SphericCoordinate spherNur = new SphericCoordinate(49.4521018,11.0766654); 
	
	@Test	
	//reference to calculation result 
	//https://www.movable-type.co.uk/scripts/latlong.html
	public void testGetSphericDistance() {
	 double result  = spherAlx.getSphericDistance(spherBer);
	 assertEquals(2722.20466, result, 0.0001);		
	}
	
	@Test
	//reference to calculation result 
	//http://keisan.casio.com/exec/system/1359534351
	public void testAsCartesianCoordinate () {
		CartesianCoordinate carCord = spherNur.asCartesianCoordinate();
		double x = carCord.getX();
		double y = carCord.getY();
		double z = carCord.getZ();
		
		boolean conversionResult = carCord.isEqualOfDouble(x,795.7094)
				&&carCord.isEqualOfDouble(y,930.08054)
				&&carCord.isEqualOfDouble(z,6252.3145);
		assertTrue(conversionResult);				  
	}

	@Test
	public void testIsEqual() {
		assertFalse(spherAlx.isEqual(spherNur));		
         
	}

	@Test
	public void testGetDistance() {
		
		double result  = spherAlx.getDistance(spherBer);
		assertEquals(2722.2046,result, 0.0001);
		
	}


}
