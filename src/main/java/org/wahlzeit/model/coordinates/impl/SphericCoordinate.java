/*
 *  Copyright
 *
 *  Classname: SphericCoordinate
 *  Author: Tango1266
 *  Version: 16.11.17 15:29
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

/**
 */
public class SphericCoordinate extends AbstractCoordinate {
    private double longitude;
    private double latitude;
    private double radius;

    public static final double EARTH_RADIUS_METER = 6_378_000;
    public static final double LONGITUDE_MAX_VALUE = 90.00;
    public static final double LATITUDE_MAX_VALUE = 180.00;
    public static final double PRECISION = 1.0E-10;

    public SphericCoordinate() {
        setRadius(EARTH_RADIUS_METER);
    }

    public SphericCoordinate(double latitude, double longitude) {
        this(latitude, longitude, EARTH_RADIUS_METER);
    }

    public SphericCoordinate(double latitude, double longitude, double radius) {
        setLatitude(latitude);
        setLongitude(longitude);
        setRadius(radius);
    }

    public void setLongitude(double longitude) {
        Assert.inRangeMax(longitude, LONGITUDE_MAX_VALUE, "Longitude");
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        Assert.notNegative(radius);
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        Assert.inRangeMax(latitude, LATITUDE_MAX_VALUE);
        this.latitude = latitude;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLongitude());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getRadius());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean isEqual(Coordinate otherCoord) {
        if (this == otherCoord) {
            return true;
        }
        if (otherCoord == null) {
            return false;
        }

        SphericCoordinate otherSpherCoord = otherCoord.asSphericCoordinate();

        if (MathUtils.doublesAreNotEqual(otherSpherCoord.getLongitude(), getLongitude())) {
            return false;
        }
        if (MathUtils.doublesAreNotEqual(otherSpherCoord.getLatitude(), getLatitude())) {
            return false;
        }
        return MathUtils.doublesAreEqual(otherSpherCoord.getRadius(), getRadius());
    }

    @Override
    public double getSphericDistance(Coordinate otherCoord) {
        SphericCoordinate otherSphCoord = otherCoord.asSphericCoordinate();

        double latitudeAsRad = Math.toRadians(getLatitude());
        double otherLatitudeAsRad = Math.toRadians(otherSphCoord.getLatitude());
        double longitudeDiffAsRad = Math.toRadians(otherSphCoord.longitude - getLongitude());

        double sinLatitudeProduct = Math.sin(latitudeAsRad) * Math.sin(otherLatitudeAsRad);
        double cosLatitudeProduct = Math.cos(latitudeAsRad) * Math.cos(otherLatitudeAsRad);
        double cosLongitudeDiff = Math.cos(longitudeDiffAsRad);
        double acosSum = Math.acos(sinLatitudeProduct + (cosLatitudeProduct * cosLongitudeDiff));

        return getRadius() * acosSum;
    }

    @Override
    public double getCartesianDistance(Coordinate otherCoord) {
        return asCartesianCoordinate().getDistance(otherCoord);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    /**
     * https://de.wikipedia.org/wiki/Kugelkoordinaten section "Andere Konventionen"
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        double[] cartesianOrdinates = MathUtils.toCartesianOrdinates(getLatitude(), getLongitude(), getRadius());
        return new CartesianCoordinate(cartesianOrdinates[0], cartesianOrdinates[1], cartesianOrdinates[2]);
    }

    @Override
    public String toString() {
        return "(" + latitude + "," + longitude + "), r= " + radius + ")";
    }
}
