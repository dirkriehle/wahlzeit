
package org.wahlzeit.model;


/**
@Inv validate that CartesianCoordinate  is not null by assertClassInvariants
*/

public final class CartesianCoordinate extends AbstractCoordinate{
	
	
	
	private  double x =0;
	private  double y=0;
	private  double z=0;
	
	public CartesianCoordinate()
	{
		
	}	
	public CartesianCoordinate(double x, double y , double z)
	{
	     this.x =x;
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
	protected void setX(double x)
	{
		 this.x = x;
	}
	
	protected void setY(double y)
	{
		this.y =  y;
	}
	
	protected void setZ(double z)
	{
		this.z =  z;
	}
	/**
	return Cartesian coordinate 
	@post validate that Cartesian coordinate  is not null; include x , y and z 
	*/
	@Override
	public CartesianCoordinate asCartesianCoordinate() {
		assertClassInvariants();	
		assert(this != null);	
		assertClassInvariants();	
		CartesianCoordinate cord = this;
		return cord;
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
		assertDoubleNotNull (cartCord.x,cartCord.y, cartCord.z);
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
		 double latitude;
		 try{		
			 latitude = Math.toDegrees(Math.acos(z / radius));
		 }	 
		 catch (ArithmeticException e){
			 latitude = 0;
		 }
		 
		 double longitude = Math.toDegrees(Math.atan2(y, x));
		 SphericCoordinate result  = new SphericCoordinate(latitude, longitude, radius);
		 assertDoubleNotNull (result.getLatitude(), result.getLongitude(), result.getRadius());
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
         assertDoubleNotNull (cartCord.x,cartCord.y,cartCord.z);
         Boolean result =isEqualOfDouble(this.x,cartCord.getX())
				 &&isEqualOfDouble(this.y,cartCord.getY())
				 && isEqualOfDouble(this.z,cartCord.getZ()); 
         assert(result!=null);
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
		return this.isEqual((CartesianCoordinate) obj);
	}
	/**
	check that the coordinate is always not null
	 * @return 
	@Inv
	*/
	public void assertClassInvariants()
	{
	    assert(!Double.isNaN(this.x));
	    assert(!Double.isNaN(this.y));
	    assert(!Double.isNaN(this.z));

	}
	
	

}
