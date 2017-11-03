package org.wahlzeit.model;

public class Location {

public Coordinate cord = new Coordinate();

	Location()
	{
		
	}
	
	Location (double x, double y)
	{
		Coordinate cord = new Coordinate(x,y);
	    this.cord = cord;
		
	}
	
	Location (double x, double y, double z)
	{
		Coordinate cord = new Coordinate(x,y,z);
		this.cord =cord;
	}
	
	Location (Coordinate newCord)
	{
		this.cord = newCord;
	}
}