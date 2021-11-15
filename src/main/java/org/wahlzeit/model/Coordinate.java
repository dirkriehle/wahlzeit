package org.wahlzeit.model;

import java.util.Objects;

public class Coordinate {

    /**
     * threshold for comparing coordinate values
     */
    private final double EPSILON = 0.001;

    /**
     * The x, y, z components of the coordinate
     */
    private double x;
    private double y;
    private double z;

    /**
     * 
     * @methodtype constructor
     */
    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
	 * 
	 * @methodtype get
	 */
    public double getX() {
        return x;
    }

    /**
	 * 
	 * @methodtype get
	 */
    public double getY() {
        return y;
    }

    /**
	 * 
	 * @methodtype get
	 */
    public double getZ() {
        return z;
    }

    /**
     * Calculates the cartesian distance between this and
     * another coordinate.
     * 
     * @param coordinate
     * @return double
     */
    protected double getDistance(Coordinate coordinate) {
        double xDistSq = Math.pow(this.x - coordinate.x, 2);
        double yDistSq = Math.pow(this.y - coordinate.y, 2);
        double zDistSq = Math.pow(this.z - coordinate.z, 2);
        return Math.sqrt(xDistSq + yDistSq + zDistSq);
    }

    /**
     * Evaluates if this is equal to another coordinate by
     * comparing their x, y and z values.
     * 
     * @param coordinate
     * @return boolean
     */
    protected boolean isEqual(Coordinate coordinate) {
        if(coordinate == null) {
            return false;
        }
        boolean xEqual = Math.abs(coordinate.x - this.x) <= EPSILON;
        boolean yEqual = Math.abs(coordinate.y - this.y) <= EPSILON;
        boolean zEqual = Math.abs(coordinate.z - this.z) <= EPSILON;
        return xEqual && yEqual && zEqual;
    }
    /**
     * Returns true if the parameter is an object of the class coordinate
     * and this and the paramer have the same x, y and z values.
     * 
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if(object == null || object.getClass() != Coordinate.class) {
            return false;
        }
        return this.isEqual((Coordinate) object);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
