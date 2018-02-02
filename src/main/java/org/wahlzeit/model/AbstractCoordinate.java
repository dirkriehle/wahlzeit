package org.wahlzeit.model;


//import static org.junit.Assert.assertNotNull;

public abstract class AbstractCoordinate implements Coordinate{
	
  
	public abstract CartesianCoordinate asCartesianCoordinate();
	public abstract SphericCoordinate asSphericCoordinate();
	public abstract boolean isEqual(Coordinate cord);
	
	
	@Override
	public double getDistance(Coordinate cord) {	 

      if(cord == null) throw new IllegalArgumentException("The coordinate is null");			  

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
	protected static  boolean isEqualOfDouble(double val1, double val2) {
		assert(!Double.isNaN(val1) && !Double.isNaN(val2) );
		Boolean result = val1 == val2 ? true : Math.abs(val1 - val2) <= 0.0001;
		assert(result!=null);
		return result;	
	}
	@Override
	public double getSphericDistance(Coordinate cord) {
		double result = cord.getDistance(cord.asSphericCoordinate());
		assert(result>=0);
		return result;
	}
	@Override
	public double getCartesianDistance(Coordinate cord) {
		double result = cord.getDistance(cord.asCartesianCoordinate());
		assert(result>=0);
		return result;
	}
	
   @Override
	public void assertDoubleNotNull(double val1, double val2, double val3) 
	{
	    assert(!Double.isNaN(val1));
	    assert(!Double.isNaN(val2));
	    assert(!Double.isNaN(val3));
    
		
	}
	

}
