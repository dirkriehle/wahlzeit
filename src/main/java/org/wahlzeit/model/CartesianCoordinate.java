
package org.wahlzeit.model;

public class CartesianCoordinate  implements Coordinate{
	
	
	
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
	
	@Override
	public CartesianCoordinate asCartesianCoordinate() {
		return this;
	}

	@Override
	public double getCartesianDistacnce(Coordinate cord) {
		CartesianCoordinate cartCord = cord.asCartesianCoordinate();
		return Math.sqrt(Math.pow(this.x - cartCord.getx(), 2) + Math.pow(this.y - cartCord.gety(), 2) + Math.pow(this.z- cartCord.getz(), 2));

	}

	@Override
	public SphericCoordinate asSphericCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getSphericDistacnce(Coordinate cord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDistacnce(Coordinate cord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEqual(Coordinate cord) {

		CartesianCoordinate cartCord = cord.asCartesianCoordinate();
		 if (this.x==cartCord.getx() && this.y==cartCord.gety() && this.z==cartCord.getz()) return true;
		 else return false;
		
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
