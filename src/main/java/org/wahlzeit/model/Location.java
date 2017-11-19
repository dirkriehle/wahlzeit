package org.wahlzeit.model;

public class Location {

public SphericCoordinate spherCord = new SphericCoordinate();
public CartesianCoordinate cartCord = new CartesianCoordinate();

	Location()
	{
		
	}
	
	Location (double lat, double lang)
	{
		SphericCoordinate cord = new SphericCoordinate(lat,lang);
	    this.spherCord = cord;
		
	}

	Location (SphericCoordinate newCord)
	{
		this.spherCord = newCord;
	}
	
	Location (double x, double y,double z)
	{
		CartesianCoordinate cord = new CartesianCoordinate(x,y,z);
	    this.cartCord = cord;
		
	}
	Location (CartesianCoordinate newCord)
	{
		this.cartCord = newCord;
	}
}