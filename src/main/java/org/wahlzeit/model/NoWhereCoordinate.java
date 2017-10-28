package org.wahlzeit.model;

/**
 * Represents a null-coordinate with defined "do-nothing-behavior" when interacting with it
 */
public class NoWhereCoordinate extends Coordinate {

    /**
     * @return 0, always
     */
    @Override
    public double getY() {
        return 0;
    }

    /**
     * @return 0, always
     */
    @Override
    public double getX() {
        return 0;
    }

    /**
     * @return 0, always
     */
    @Override
    public double getZ() {
        return 0;
    }

    /**
     * Using this will do nothing
     */
    @Override
    public void setX(double x) {
    }

    /**
     * Using this will do nothing
     */
    @Override
    public void setY(double y) {
    }

    /**
     * Using this will do nothing
     */
    @Override
    public void setZ(double z) {
    }

    /**
     * Determines the equality between something and nothing
     * @param other
     * @return false, always
     */
    @Override
    public boolean isEqual(Coordinate other) {
        return false;
    }

    /**
     * Determines the distance between something and nothing
     * @param otherCoordinate
     * @return -1, always
     */
    @Override
    public double getDistance(Coordinate otherCoordinate) {
        return -1;
    }
}
