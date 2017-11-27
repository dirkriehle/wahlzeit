
package org.wahlzeit.model;

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
	
	@Override
	public CartesianCoordinate asCartesianCoordinate() {
		return this;
	}
	@Override
	public double getCartesianDistance(Coordinate cord) {
		CartesianCoordinate cartCord = cord.asCartesianCoordinate();
		return Math.sqrt(Math.pow(this.x - cartCord.getX(), 2) 
				+ Math.pow(this.y - cartCord.getY(), 2) 
				+ Math.pow(this.z- cartCord.getZ(), 2));
	}

	@Override
	public SphericCoordinate asSphericCoordinate() {
		 double radius = Math.sqrt(x * x + y * y + z * z);
		 double latitude = Math.toDegrees(Math.acos(z / radius));
		 double longitude = Math.toDegrees(Math.atan2(y, x));
		 return new SphericCoordinate(latitude, longitude, radius);
	}
	@Override
	public boolean isEqual(Coordinate cord) {
		CartesianCoordinate cartCord = cord.asCartesianCoordinate();
		 return isEqualOfDouble(this.x,cartCord.getX())
				 &&isEqualOfDouble(this.y,cartCord.getY())
				 && isEqualOfDouble(this.z,cartCord.getZ()); 
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

}
