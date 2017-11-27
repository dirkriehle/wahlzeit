package org.wahlzeit.model;

import org.wahlzeit.model.Coordinate;

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
	public boolean isEqualOfDouble(double val1, double val2) {
		return val1 == val2 ? true : Math.abs(val1 - val2) <= 0.0001;
		
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
