/**
 * 
 */
package org.wahlzeit.model;

/**
 *
 */
public interface Coordinate {
	
	public CartesianCoordinate asCartesianCoordinate();
	public double getCartesianDistacnce(Coordinate cord);
	public SphericCoordinate asSphericCoordinate();
	public double getSphericDistacnce(Coordinate cord);
	public double getDistacnce(Coordinate cord);
	public boolean isEqual(Coordinate cord);
	
	

	

}
