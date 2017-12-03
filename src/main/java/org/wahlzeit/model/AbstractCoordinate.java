package org.wahlzeit.model;

public abstract class AbstractCoordinate implements Coordinate{
	
  
	public abstract CartesianCoordinate asCartesianCoordinate();
	public abstract SphericCoordinate asSphericCoordinate();
	public abstract boolean isEqual(Coordinate cord);
	
	
	@Override
	public double getDistance(Coordinate cord) {	
	  if(cord == null) {
	     return -Double.MAX_VALUE;
	}
	  if(cord instanceof CartesianCoordinate) {
	     return getCartesianDistance(cord);
	}
	  return getSphericDistance(cord);
		
	}
	/**
	compare between two double with tolerance 0.0001
	@post validate that the output is not null 
	@Pre validate that val1 and val2 are not null
	*/
	public boolean isEqualOfDouble(double val1, double val2) {
		assert(val1>=0 && val2>=0);
		boolean result = val1 == val2 ? true : Math.abs(val1 - val2) <= 0.0001;
		assert(result ==false || result ==true);
		return result;
		
	}
	@Override
	public double getSphericDistance(Coordinate cord) {
		
		return cord.getDistance(cord.asSphericCoordinate());
	}
	@Override
	public double getCartesianDistance(Coordinate cord) {
		
		return cord.getDistance(cord.asCartesianCoordinate());
	}
	

}
