
package org.wahlzeit.model;

public  class SphericCoordinate implements Coordinate{
	
	
	
	private double latitude =0;
	private double longitude=0;
    private final double radious = 6371; // Radius of the earth
	
	SphericCoordinate()
	{
		
	}
	
	SphericCoordinate(double latitude, double longitude )
	{
	     this.latitude=latitude;	
	     this.longitude=longitude;
	}
	 
	public double getlatitude()
	{
		return latitude;
	}
	
	public double getlongitude()
	{
		return longitude;
	}
	
	public double getradious()
	{
		return radious;
	}
	


	@Override
	public SphericCoordinate asSphericCoordinate() {
		return this;
	}

	@Override
	public double getSphericDistacnce(Coordinate cord) {

		 SphericCoordinate sphercord = cord.asSphericCoordinate();
		 double lat1 = Math.toRadians(this.latitude);
	     double lat2 = Math.toRadians(sphercord.getlatitude());
	     double latdist = Math.toRadians(this.latitude - sphercord.getlatitude());
	     double longdist = Math.toRadians(this.longitude - sphercord.getlongitude());

	     double a = Math.sin(latdist/2)*Math.sin(latdist/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin(longdist/2)* Math.sin(longdist/2);
		
	     double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
	     return radious*c;
	}

// Radius of earth is constant so it is not in the comparison condition
	@Override
	public boolean isEqual(Coordinate cord) {
		
		SphericCoordinate sphercord = cord.asSphericCoordinate();
			
		return (this.latitude == sphercord.getlatitude() && this.longitude == sphercord.getlongitude() );
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCartesianDistacnce(Coordinate cord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDistacnce(Coordinate cord) {
		// TODO Auto-generated method stub
		return 0;
	}	

}
