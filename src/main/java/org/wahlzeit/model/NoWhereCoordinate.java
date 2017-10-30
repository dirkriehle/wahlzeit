package org.wahlzeit.model;

import static java.lang.System.identityHashCode;

/**
 * Represents a null-coordinate with defined "do-nothing-behavior" when interacting with it
 */
public class NoWhereCoordinate extends Coordinate {

    /**
     * @return 0
     */
    @Override
    public double getY() {
        return 0;
    }

    /**
     * @return 0
     */
    @Override
    public double getX() {
        return 0;
    }

    /**
     * @return 0
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
     * Determines the distance between something and nothing
     * @return -1
     */
    @Override
    public double getDistance(Coordinate otherCoord) {
        return -1;
    }

    /**
     * Determines the equality between something and nothing
     * @return false
     */
    @Override
    public boolean isEqual(Coordinate otherCoord) {
        return false;
    }

    /**
     * @return unique ID in dependency of the object reference
     */
    @Override
    public int hashCode() {
        return identityHashCode(this);
    }
}
