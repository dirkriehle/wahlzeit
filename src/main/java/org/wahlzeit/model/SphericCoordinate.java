package org.wahlzeit.model;

public class SphericCoordinate implements Coordinate {

    private double phi;
    private double theta;
    private double radius;

    /**
     * @methodtype constructor
     * 
     * @param phi
     * @param theta
     * @param radius
     */
    public SphericCoordinate(double phi, double theta, double radius) {
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
    }

    /**
     * @methodtype get
     * 
     * @return double
     */
    public double getPhi() {
        return phi;
    }

    /**
     * @methodtype get
     * 
     * @return double
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @methodtype get
     * 
     * @return double
     */
    public double getRadius() {
        return radius;
    }

    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "spheric coordinate: phi=" + phi + ", theta=" + theta + ", radius=" + radius;
    }

    /**
     * 
     * @return SphericCoordinate
     */
    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    /**
     * 
     * @return
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        double x = radius * Math.sin(phi) * Math.cos(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(phi);
        return new CartesianCoordinate(x, y, z);
    }

    /**
     * To avoid deviations compare cartesian coordinates
     * @param coordinate
     * @return boolean
     */
    @Override
    public boolean isEqual(Coordinate coordinate) {
        if(coordinate == null) {
            return false;
        }

        CartesianCoordinate cartesianCoordinate = this.asCartesianCoordinate();
        return cartesianCoordinate.isEqual(coordinate);
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

    /**
     * 
     */
    @Override
    public int hashCode() {
        CartesianCoordinate cartesianCoordinate = asCartesianCoordinate();
        return cartesianCoordinate.hashCode();
    }

    /**
     * 
     * @param Coordinate
     * @return CartesianCoordinate
     */
    @Override
    public double getCartesianDistance(Coordinate coordinate) {
        CartesianCoordinate cartesianCoordinate = asCartesianCoordinate();
        return cartesianCoordinate.getCartesianDistance(coordinate);
    }

    /**
     * 
     * @param Coordinate
     * @return double
     */
    @Override
    public double getCentralAngle(Coordinate coordinate) {
        SphericCoordinate sphericCoordinate = coordinate.asSphericCoordinate();
        
        double deltaTheta = Math.abs(this.theta - sphericCoordinate.theta);
        double phi1 = this.phi;
        double phi2 = sphericCoordinate.phi;

        double centralAngle = Math.acos(Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(deltaTheta));

        return centralAngle;
    }
    
}
