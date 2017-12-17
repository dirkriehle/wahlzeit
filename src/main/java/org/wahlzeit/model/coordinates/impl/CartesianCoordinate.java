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

import org.wahlzeit.model.config.DomainCfg;
import org.wahlzeit.model.coordinates.Coordinate;
import org.wahlzeit.utils.Assert;
import org.wahlzeit.utils.MathUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.wahlzeit.utils.MathUtils.square;

/**
 * Represents a Cartesian coordinate of a three dimensional Cartesian coordinate system
 */
public class CartesianCoordinate extends AbstractCoordinate {
    private final double y;
    private final double x;
    private final double z;
    protected final static Set<CartesianCoordinate> coordinates = Collections.synchronizedSet(new HashSet<CartesianCoordinate>());
    public static final Double MAX_VALUE = Double.MAX_VALUE - 1E292;

    private CartesianCoordinate(double x, double y, double z) {
        assertOrdinates(x, y, z);
        this.y = y;
        this.x = x;
        this.z = z;
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
     * <a href=http://www.learningaboutelectronics.com/Articles/Cartesian-rectangular-to-spherical-coordinate-converter-calculator.php#answer>source</a>
     * @return array of doubles in format: [ latitude , longitude , radius ]
     */
    public static double[] toSphericalOrdinates(double x, double y, double z) {
        assertOrdinates(x, y, z);
        double radius = MathUtils.sqrtOfSum(square(x), square(y), square(z));
        double longitude = radius == 0 ? 0 : Math.toDegrees(Math.acos(z / radius));
        double latitude = x == 0 ? 0 : Math.toDegrees(Math.atan(y / x));
        return new double[]{latitude, longitude, radius};
    }

    public static CartesianCoordinate getCoordinate(double x, double y, double z) {
        CartesianCoordinate cartesianCoordinate = new CartesianCoordinate(x, y, z);
        return AbstractCoordinate.getCoordinateFromCache(cartesianCoordinate).asCartesianCoordinate();
    }

    public static CartesianCoordinate getCartesianCoordinate() {
        return getCoordinate(0, 0, 0);
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
        assertOrdinates(getX());
        assertOrdinates(getY());
        assertOrdinates(getZ());
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
        return SphericCoordinate.getCoordinate(sphericOrdinates[0], sphericOrdinates[1], sphericOrdinates[2]);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    private static void assertOrdinates(double... ordinates) {
        try {
            Assert.areValidDoubles(ordinates);
            for (double ordinate : ordinates) {
                Assert.inRangeMax(ordinate, MAX_VALUE);
            }
        } catch (IllegalArgumentException ex) {
            DomainCfg.logError(CartesianCoordinate.class, ex);
            throw ex;
        }
    }
}
