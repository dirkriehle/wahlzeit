package org.wahlzeit.model;

public class Coordinate {

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
        return coordinate.x == this.x && coordinate.y == this.y && coordinate.z == this.z;
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
}
