/*
 *  Copyright
 *
 *  Author: Tango1266
 *
 *  Version: 05.11.17 21:39
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

package org.wahlzeit.model;

/**
 * Represents a Cartesian coordinate of a three dimensional Cartesian coordinate system
 */
public class Coordinate {
    private double y;
    private double x;
    private double z;
    public static final Double MAX_VALUE = Double.MAX_VALUE - 1E292;

    public Coordinate() {
    }

    public Coordinate(double y, double x, double z) {
        setY(y);
        setX(x);
        setZ(z);
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    /**
     * Sets ordinate Value within the range [-Coordinate.MAX_VALUE:Coordinate.MAX_VALUE]
     */
    public void setX(double x) {
        throwExceptionIfNotInRange(x);
        this.x = x;
    }

    /**
     * Sets ordinate Value within the range [-Coordinate.MAX_VALUE:Coordinate.MAX_VALUE]
     */
    public void setY(double y) {
        throwExceptionIfNotInRange(y);
        this.y = y;
    }

    /**
     * Sets ordinate Value within the range [-Coordinate.MAX_VALUE:Coordinate.MAX_VALUE]
     */
    public void setZ(double z) {
        throwExceptionIfNotInRange(z);
        this.z = z;
    }

    /**
     * @return -1, if otherCood is null or NoWhereCoordinate. The direct distance, otherwise.
     */
    public double getDistance(Coordinate otherCoord) {
        if (otherCoord instanceof NoWhereCoordinate || otherCoord == null) {
            return -1;
        }
        double xDifference = getX() - otherCoord.getX();
        double yDifference = getY() - otherCoord.getY();
        double zDifference = getZ() - otherCoord.getZ();
        double radicand = square(xDifference) + square(yDifference) + square(zDifference);
        return Math.sqrt(radicand);
    }

    /**
     * @return true, if all ordinates of the compared Coordinates are equal.
     */
    public boolean isEqual(Coordinate otherCoord) {
        if (otherCoord == this) {
            return true;
        }
        if (otherCoord == null) {
            return false;
        }
        boolean xOrdinatesAreEqual = doublesAreEqual(getX(), otherCoord.getX());
        boolean yOrdinatesAreEqual = doublesAreEqual(getY(), otherCoord.getY());
        boolean zOrdinatesAreEqual = doublesAreEqual(getZ(), otherCoord.getZ());
        return xOrdinatesAreEqual && yOrdinatesAreEqual && zOrdinatesAreEqual;
    }

    /**
     * @return hashes calculated from all ordinates
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(y);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * {@link #isEqual(Coordinate) isEqual(Coordinate)}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            return isEqual((Coordinate) obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + ")";
    }

    private boolean doublesAreEqual(double firstDouble, double secondDouble) {
        return Double.compare(firstDouble, secondDouble) == 0;
    }

    private double square(double value) {
        return Math.pow(value, 2);
    }

    private void throwExceptionIfNotInRange(double value) {
        if (Math.abs(value) > MAX_VALUE) {
            throw new IllegalArgumentException("The input value should be smaller than " + MAX_VALUE);
        }
    }
}
