/**
 * 
 */
package org.wahlzeit.model;

/**
 *
 */
public interface Coordinate {
	
	public CartesianCoordinate asCartesianCoordinate();
	public double getCartesianDistance(Coordinate cord);
	public SphericCoordinate asSphericCoordinate();
	public double getSphericDistance(Coordinate cord);
	public double getDistance(Coordinate cord);
	public boolean isEqual(Coordinate cord);

}
