
package org.wahlzeit.model;

/**
@Inv validate that SphericCoordinates is not null by assertClassInvariants
*/

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
	/**
	return Cartesian coordinate 
	@Pre validate the cord is not null
	@post validate that Spheric coordinate  is not null; include latitude , longitude and radius 
	*/
	@Override
	public SphericCoordinate asSphericCoordinate() {
		assertClassInvariants();	
		assert(this instanceof SphericCoordinate);	
		assertClassInvariants();	
		return this;
	}
	/**
	return the distance between two Spheric coordinate
	@post validate that the output is double
	*/
	@Override
	public double getSphericDistance(Coordinate cord) {
	     assertClassInvariants();	
		 SphericCoordinate sphercord = cord.asSphericCoordinate();
		 assert(sphercord.latitude>=0 && sphercord.longitude>=0  && sphercord.radius>=0  );
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
		 assert(sphercord.latitude>=0 && sphercord.longitude>=0  && sphercord.radius>=0  );
    	 assert(sphercord.latitude>=0 && sphercord.longitude>=0  && sphercord.radius>=0  );	
    	 boolean result =   isEqualOfDouble(this.latitude ,sphercord.getLatitude())
 			            	&& isEqualOfDouble(this.longitude ,sphercord.getLongitude())
 			            	&&isEqualOfDouble(this.radius ,sphercord.getradius());
    	 assert(result ==false || result ==true);
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
		else return this.isEqual((SphericCoordinate) obj);
			
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
        assert (cartCord.getX()>=0 && cartCord.getY()>=0 && cartCord.getZ()>=0);
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
	    assert(this.longitude>=0);
	    assert(this.latitude>=0);
	    assert(this.radius>=0);	    
		
	}
}
