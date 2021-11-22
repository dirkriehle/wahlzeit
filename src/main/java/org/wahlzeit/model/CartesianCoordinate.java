package org.wahlzeit.model;

import java.util.Objects;

public class CartesianCoordinate implements Coordinate {

    /**
     * threshold for comparing CartesianCoordinate values
     */
    private final double EPSILON = 0.001;

    /**
     * The x, y, z components of the CartesianCoordinate
     */
    private double x;
    private double y;
    private double z;

    /**
     * 
     * @methodtype constructor
     */
    public CartesianCoordinate(double x, double y, double z) {
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
     * 
     */
    @Override
    public String toString() {
        return "cartesian coordinate: x=" + x + ", y=" + y + ", z=" + z;
    }

    /**
     * Calculates the cartesian distance between this and
     * another coordinate.
     * 
     * @param CartesianCoordinate
     * @return double
     */
    public double getCartesianDistance(Coordinate coordinate) {
        CartesianCoordinate cartesianCoordinate = coordinate.asCartesianCoordinate();

        double xDistSq = Math.pow(this.x - cartesianCoordinate.x, 2);
        double yDistSq = Math.pow(this.y - cartesianCoordinate.y, 2);
        double zDistSq = Math.pow(this.z - cartesianCoordinate.z, 2);
        return Math.sqrt(xDistSq + yDistSq + zDistSq);
    }

    /**
     * Evaluates if this is equal to another CartesianCoordinate by
     * comparing their x, y and z values.
     * 
     * @param CartesianCoordinate
     * @return boolean
     */
    @Override
    public boolean isEqual(Coordinate coordinate) {
        if(coordinate == null) {
            return false;
        }
        CartesianCoordinate cartesianCoordinate = coordinate.asCartesianCoordinate();

        boolean xEqual = Math.abs(cartesianCoordinate.x - this.x) <= EPSILON;
        boolean yEqual = Math.abs(cartesianCoordinate.y - this.y) <= EPSILON;
        boolean zEqual = Math.abs(cartesianCoordinate.z - this.z) <= EPSILON;
        return xEqual && yEqual && zEqual;
    }

    /**
     * 
     * @return
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    /**
     * S
     * @return
     */
    @Override
    public SphericCoordinate asSphericCoordinate() {
        double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        double theta = Math.acos(z / radius);
        double phi;
        if(x > 0) {
            phi = Math.atan(y / x);
        } else if(x < 0) {
            phi = Math.atan(y / x) + Math.PI;
        } else {
            phi = Math.PI / 2;
        }
        return new SphericCoordinate(phi, theta, radius);
    }

    /**
     * Returns true if the parameter is an object of the interface Coordinate
     * and this and the paramer have the same x, y and z values.
     * 
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if(object == null || !(object instanceof Coordinate)) {
            return false;
        }
        return this.isEqual((Coordinate) object);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public double getCentralAngle(Coordinate coordinate) {
        SphericCoordinate sphericCoordinate = this.asSphericCoordinate();
        return sphericCoordinate.getCartesianDistance(coordinate);
    }
}
