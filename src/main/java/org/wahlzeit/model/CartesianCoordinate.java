
package org.wahlzeit.model;


/**
@Inv validate that CartesianCoordinate  is not null by assertClassInvariants
*/

public class CartesianCoordinate extends AbstractCoordinate{
	
	
	
	private double x =0;
	private double y=0;
	private double z=0;
	
	CartesianCoordinate()
	{
		
	}	
	CartesianCoordinate(double x, double y , double z)
	{
	     this.x=x;	
	     this.y=y;
	     this.z=z;
	}
	 
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	/**
	return Cartesian coordinate 
	@post validate that Cartesian coordinate  is not null; include x , y and z 
	*/
	@Override
	public CartesianCoordinate asCartesianCoordinate() {
		assertClassInvariants();	
		assert(this instanceof CartesianCoordinate);	
		assertClassInvariants();	
		return this;
	}
	/**
	return the distance between two Cartesian 
	@post validate that the output is double
	@Pre validate the cord is not null
	*/
	@Override
	public double getCartesianDistance(Coordinate cord) {
		assertClassInvariants();		
		CartesianCoordinate cartCord = cord.asCartesianCoordinate();
		assert (cartCord.x>=0 && cartCord.y>=0 && cartCord.z>=0);
		double result  = Math.sqrt(Math.pow(this.x - cartCord.getX(), 2) 
				+ Math.pow(this.y - cartCord.getY(), 2) 
				+ Math.pow(this.z- cartCord.getZ(), 2));
		assert(result>=0);
		assertClassInvariants();	
		return result;
		
		
	}
	/**
	return Cartesian coordinate 
	@Pre validate the cord is not null
	@post validate that Spheric coordinate  is not null; include latitude , longitude and radius 
	*/
	@Override
	public SphericCoordinate asSphericCoordinate() {
		 assertClassInvariants();		
		 double radius = Math.sqrt(x * x + y * y + z * z);
		 double latitude = Math.toDegrees(Math.acos(z / radius));
		 double longitude = Math.toDegrees(Math.atan2(y, x));
		 SphericCoordinate result  = new SphericCoordinate(latitude, longitude, radius);
		 assert (result.getLatitude()>=0 && result.getLongitude()>=0 && result.getradius()>=0);
	     assertClassInvariants();	
		 return result;
	}
	/**
	check if the two coordinates are equal or not 
	@post check that return type is boolean
	@Pre validate the cord is not nulls
	*/
	@Override
	public boolean isEqual(Coordinate cord) {
		 assertClassInvariants();		
         CartesianCoordinate cartCord = cord.asCartesianCoordinate();
         assert (cartCord.x>=0 && cartCord.y>=0 && cartCord.z>=0);
         boolean result =isEqualOfDouble(this.x,cartCord.getX())
				 &&isEqualOfDouble(this.y,cartCord.getY())
				 && isEqualOfDouble(this.z,cartCord.getZ()); 
         assert(result ==false || result ==true);
	     assertClassInvariants();	
		 return result;
		 
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		else return this.isEqual((CartesianCoordinate) obj);
	}
	/**
	check that the coordinate is always not null
	 * @return 
	@Inv
	*/
	public void assertClassInvariants()
	{
	    assert(this.x>=0);
	    assert(this.y>=0);
	    assert(this.z>=0);	    
		
	}

}
