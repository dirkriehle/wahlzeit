package org.wahlzeit.model;

import static org.junit.Assert.assertNotNull;

public class Location  {

public Coordinate cord;


	Location()
	{
		
	}
	
	/**
	
	@Pre validate that val1 and val2 are not null
	*/
	
	Location (double lat, double lang)
	{
		assertNotNull(lat);
		assertNotNull(lang);
		SphericCoordinate cord = new SphericCoordinate(lat,lang);
	    this.cord = cord;
		
	}
	
// throw exception f the Spheric coordinate is null
	Location (SphericCoordinate newCord)
	{
		if(newCord == null) throw new IllegalArgumentException("The Spheric coordinate is null");
		this.cord = newCord;
	}
	/**
	
	@Pre validate that x , y and z are not null
	*/
	Location (double x, double y,double z)
	{
		assertNotNull(x);
		assertNotNull(y);
		assertNotNull(z);
		CartesianCoordinate cord = new CartesianCoordinate(x,y,z);
	    this.cord = cord;
		
	}
	// throw exception f the Caretsian coordinate is null
	Location (CartesianCoordinate newCord)
	{
		if(newCord == null) throw new IllegalArgumentException("The Cartesian coordinate is null");
		this.cord = newCord;
	}
	
	
}