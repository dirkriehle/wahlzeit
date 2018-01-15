
package org.wahlzeit.model;


/**
@Inv validate that SphericCoordinates is not null by assertClassInvariants
*/

public final class  SphericCoordinate extends AbstractCoordinate{
	
	
	private final double earthradius = 6371; // radius of the earth
	protected double latitude =0;
	protected double longitude=0;
	protected double radius = earthradius;
	
    
	
	SphericCoordinate()
	{
		
	}
	protected SphericCoordinate(double latitude, double longitude )
	{
	     this.latitude=latitude;	
	     this.longitude=longitude;
	}
	public SphericCoordinate(double latitude, double longitude,double radius )
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
	
	public double getRadius()
	{
		return radius;
	}
	
	/**
	return Cartesian coordinate 
	@Pre validate the cord is not null
	@post validate that Spheric coordinate  is not null; include latitude , longitude and radius 
	*/
	@Override
	public SphericCoordinate asSphericCoordinate() {
		assertClassInvariants();	
		assert(this != null);	
		assertClassInvariants();
		SphericCoordinate spherCord = this;
		return spherCord;
	}
	/**
	return the distance between two Spheric coordinate
	@post validate that the output is double
	*/
	@Override
	public double getSphericDistance(Coordinate cord) {
	     assertClassInvariants();	
		 SphericCoordinate sphercord = cord.asSphericCoordinate();
		 assertDoubleNotNull(sphercord.latitude, sphercord.longitude,sphercord.radius  );
		 double lat1 = Math.toRadians(this.latitude);
	     double lat2 = Math.toRadians(sphercord.getLatitude());
	     double latdist = Math.toRadians(this.latitude - sphercord.getLatitude());
	     double longdist = Math.toRadians(this.longitude - sphercord.getLongitude());
	     double a = Math.sin(latdist/2)*Math.sin(latdist/2) 
	    		 + Math.cos(lat1)*Math.cos(lat2)
	    		 *Math.sin(longdist/2)* Math.sin(longdist/2);
		
	     double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
	     double result= radius*c;
	     assert(result>=0);
	     assertClassInvariants();	
	     return result;
	}

	/**
	check if the two coordinates are equal or not 
	@post check that return type is boolean
	@Pre validate the cord is not null
	*/
	@Override
	public boolean isEqual(Coordinate cord) {
		
		 SphericCoordinate sphercord = cord.asSphericCoordinate();
	     assertClassInvariants();	
	     assertDoubleNotNull(sphercord.latitude,sphercord.longitude,sphercord.radius  );
    	 Boolean result =   isEqualOfDouble(this.latitude ,sphercord.getLatitude())
 			            	&& isEqualOfDouble(this.longitude ,sphercord.getLongitude())
 			            	&&isEqualOfDouble(this.radius ,sphercord.getRadius());
    	 assert(result!=null);
         assertClassInvariants();	        
		 return result;
				
		 
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
		return this.isEqual((SphericCoordinate) obj);
			
	}
	/**
	return Cartesian coordinate 
	@post validate that Cartesian coordinate  is not null; include x , y and z 
	@Inv validate that  coordinates are not null by assertClassInvariants
	*/
	@Override
	public CartesianCoordinate asCartesianCoordinate() {
        assertClassInvariants();	  
		double x = radius * Math.sin(Math.toRadians(longitude)) * Math.cos(Math.toRadians(latitude));
		double y = radius * Math.sin(Math.toRadians(longitude)) * Math.sin(Math.toRadians(latitude));
		double z = radius * Math.cos(Math.toRadians(longitude));
		CartesianCoordinate cartCord = new CartesianCoordinate(x, y, z);
        assertDoubleNotNull (cartCord.getX(),cartCord.getY(), cartCord.getZ());
        assertClassInvariants();	  
		return cartCord;

}
	
	/**
	check that the coordinate is always not null
	 * @return 
	@Inv
	*/
	
	public void assertClassInvariants()
	{
		 assert(!Double.isNaN(this.longitude));
		 assert(!Double.isNaN(this.latitude));
		 assert(!Double.isNaN(this.radius));	    
		
	}
}
