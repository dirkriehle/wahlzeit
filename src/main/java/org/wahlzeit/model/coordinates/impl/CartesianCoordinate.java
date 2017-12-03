/*
 *  Copyright
 *
 *  Classname: CartesianCoordinate
 *  Author: Tango1266
 *  Version: 16.11.17 15:24
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

package org.wahlzeit.model.coordinates.impl;

import org.wahlzeit.model.coordinates.Coordinate;
import org.wahlzeit.utils.Assert;
import org.wahlzeit.utils.MathUtils;

import static org.wahlzeit.utils.MathUtils.square;

/**
 * Represents a Cartesian coordinate of a three dimensional Cartesian coordinate system
 */
public class CartesianCoordinate extends AbstractCoordinate {
    private double y;
    private double x;
    private double z;
    public static final Double MAX_VALUE = Double.MAX_VALUE - 1E292;

    public CartesianCoordinate() {
    }

    public CartesianCoordinate(double x, double y, double z) {
        Assert.areValidDoubles(x, y, z);
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
        assertOrdinate(x);
        this.x = x;
    }

    /**
     * Sets ordinate Value within the range [-Coordinate.MAX_VALUE:Coordinate.MAX_VALUE]
     */
    public void setY(double y) {
        assertOrdinate(y);
        this.y = y;
    }

    /**
     * Sets ordinate Value within the range [-Coordinate.MAX_VALUE:Coordinate.MAX_VALUE]
     */
    public void setZ(double z) {
        assertOrdinate(z);
        this.z = z;
    }

    /**
     * <a href=http://www.learningaboutelectronics.com/Articles/Cartesian-rectangular-to-spherical-coordinate-converter-calculator.php#answer>source</a>
     * @return array of doubles in format: [ latitude , longitude , radius ]
     */
    public static double[] toSphericalOrdinates(double x, double y, double z) {
        Assert.areValidDoubles(x, y, z);
        double radius = MathUtils.sqrtOfSum(square(x), square(y), square(z));
        double longitude = radius == 0 ? 0 : Math.toDegrees(Math.acos(z / radius));
        double latitude = x == 0 ? 0 : Math.toDegrees(Math.atan(y / x));
        return new double[]{latitude, longitude, radius};
    }

    @Override
    protected double doCalculateDistance(Coordinate otherCoord) {
        CartesianCoordinate otherCartCoord = otherCoord.asCartesianCoordinate();
        double xDifference = getX() - otherCartCoord.getX();
        double yDifference = getY() - otherCartCoord.getY();
        double zDifference = getZ() - otherCartCoord.getZ();
        double radicand = square(xDifference) + square(yDifference) + square(zDifference);
        return Math.sqrt(radicand);
    }

    @Override
    protected void assertClassInvariants() {
        assertOrdinate(getX());
        assertOrdinate(getY());
        assertOrdinate(getZ());
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    /**
     * <a href=https://de.wikipedia.org/wiki/Kugelkoordinaten section "Andere Konventionen">source</a>
     */
    @Override
    public SphericCoordinate asSphericCoordinate() {
        assertClassInvariants();
        double[] sphericOrdinates = toSphericalOrdinates(getX(), getY(), getZ());
        return new SphericCoordinate(sphericOrdinates[0], sphericOrdinates[1], sphericOrdinates[2]);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    private void assertOrdinate(double ordinate) {
        Assert.inRangeMax(ordinate, MAX_VALUE);
    }
}
