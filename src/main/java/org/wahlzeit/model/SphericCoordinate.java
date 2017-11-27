
package org.wahlzeit.model;

public class SphericCoordinate extends AbstractCoordinate{
	
	
	
	private double latitude =0;
	private double longitude=0;
	private double radius = 0;
	
    private final double earthradius = 6371; // radius of the earth
	
	SphericCoordinate()
	{
		
	}
	SphericCoordinate(double latitude, double longitude )
	{
	     this.latitude=latitude;	
	     this.longitude=longitude;
	     this.radius = earthradius;
	}
	SphericCoordinate(double latitude, double longitude,double radius )
	{
	     this.latitude=latitude;	
	     this.longitude=longitude;
	     this.radius = radius;
	}
	 
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public double getradius()
	{
		return radius;
	}
	
	@Override
	public SphericCoordinate asSphericCoordinate() {
		return this;
	}

	@Override
	public double getSphericDistance(Coordinate cord) {

		 SphericCoordinate sphercord = cord.asSphericCoordinate();
		 double lat1 = Math.toRadians(this.latitude);
	     double lat2 = Math.toRadians(sphercord.getLatitude());
	     double latdist = Math.toRadians(this.latitude - sphercord.getLatitude());
	     double longdist = Math.toRadians(this.longitude - sphercord.getLongitude());

	     double a = Math.sin(latdist/2)*Math.sin(latdist/2) 
	    		 + Math.cos(lat1)*Math.cos(lat2)
	    		 *Math.sin(longdist/2)* Math.sin(longdist/2);
		
	     double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
	     return radius*c;
	}

	@Override
	public boolean isEqual(Coordinate cord) {
		
		SphericCoordinate sphercord = cord.asSphericCoordinate();
			
		return isEqualOfDouble(this.latitude ,sphercord.getLatitude())
				&& isEqualOfDouble(this.longitude ,sphercord.getLongitude())
				&&isEqualOfDouble(this.radius ,sphercord.getradius());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
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
		else return this.isEqual((SphericCoordinate) obj);
			
	}

	@Override
	public CartesianCoordinate asCartesianCoordinate() {
		double x = radius * Math.sin(Math.toRadians(longitude)) * Math.cos(Math.toRadians(latitude));
		double y = radius * Math.sin(Math.toRadians(longitude)) * Math.sin(Math.toRadians(latitude));
		double z = radius * Math.cos(Math.toRadians(longitude));
		return new CartesianCoordinate(x, y, z);

}

}
