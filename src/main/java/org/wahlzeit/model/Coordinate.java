
package org.wahlzeit.model;

public class Coordinate {
	
	
	
	private double x =0;
	private double y=0;
	private double z=0;
	
	 Coordinate()
	{
		
	}
	
	 Coordinate(double x, double y)
	{
		this.x=x;
		this.y=y;
	}
	 Coordinate(double x, double y , double z)
	{
	     this.x=x;	
	     this.y=y;
	     this.z=z;
	}
	 
	public double getx()
	{
		return x;
	}
	
	public double gety()
	{
		return y;
	}
	
	public double getz()
	{
		return z;
	}
	
	public double getDitacnce(Coordinate cord)
	{
	    return Math.sqrt(Math.pow(this.x - cord.getx(), 2) + Math.pow(this.y - cord.gety(), 2) + Math.pow(this.z- cord.getz(), 2));
		
	}

	
	public boolean isEqual(Coordinate cord)
	{
		 if (this.x==cord.getx() && this.y==cord.gety() && this.z==cord.getz()) return true;
		 else return false;
		
	}
	
	public boolean equals(Coordinate cord)
	{
		return isEqual(cord);
		
	}
	
	
	
	
	
	
	
	
	

}
