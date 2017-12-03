/**
 * 
 */
package org.wahlzeit.model;

/**
 *
 */
public interface Coordinate {
	/**
	return Cartesian coordinate 
	@post validate that Cartesian coordinate  is not null; include x , y and z 
	@Inv validate that  coordinates are not null by assertClassInvariants
	*/
	public CartesianCoordinate asCartesianCoordinate();
	
	/**
	return the distance between two Cartesian 
	@post validate that the output is double
	@Pre validate the cord is not null
	@Inv validate that coordinates are not null by assertClassInvariants
	*/
	public double getCartesianDistance(Coordinate cord);
	
	/**
	return Cartesian coordinate 
	@Pre validate the cord is not null
	@post validate that Spheric coordinate  is not null; include latitude , longitude and radius 
	@Inv validate that  coordinates are not null by assertClassInvariants
	*/
	
	public SphericCoordinate asSphericCoordinate();
	/**
	return the distance between two Spheric coordinate
	@post validate that the output is double
	@Inv validate that  coordinates are not null by assertClassInvariants
	*/
	public double getSphericDistance(Coordinate cord);

	/**
    the contract post,pre,inv included in the getSphericDistance and getCartesianDistance
	*/
	public double getDistance(Coordinate cord);
	
	/**
	check if the two coordinates are equal or not 
	@post check that return type is boolean
	@Pre validate the cord is not null
	@Inv validate that  coordinates are not null by assertClassInvariants
	*/
	public boolean isEqual(Coordinate cord);

}
