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

/**
 */
public class SphericCoordinate extends AbstractCoordinate {
    private double longitude;
    private double latitude;
    private double radius;

    public static final double EARTH_RADIUS_METER = 6_378_000;
    public static final double LONGITUDE_MAX_VALUE = 90.00;
    public static final double LATITUDE_MAX_VALUE = 180.00;

    public SphericCoordinate() {
        setRadius(EARTH_RADIUS_METER);
    }

    public SphericCoordinate(double latitude, double longitude) { this(latitude, longitude, EARTH_RADIUS_METER); }

    public SphericCoordinate(double latitude, double longitude, double radius) {
        Assert.areValidDoubles(latitude, longitude, radius);
        setLatitude(latitude);
        setLongitude(longitude);
        setRadius(radius);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setLatitude(double latitude) {
        assertLatitude(latitude);
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        assertLongitude(longitude);
        this.longitude = longitude;
    }

    public void setRadius(double radius) {
        assertRadius(radius);
        this.radius = radius;
    }

    /**
     * <a href=http://www.learningaboutelectronics.com/Articles/Cartesian-rectangular-to-spherical-coordinate-converter-calculator.php#answer>source</a>
     * @return array of doubles in format: [ x , y , z ]
     */
    public static double[] toCartesianOrdinates(double latitudeInDegree, double longitudeInDegree, double radius) {
        Assert.areValidDoubles(latitudeInDegree, longitudeInDegree, radius);
        double longitudeRad = Math.toRadians(longitudeInDegree);
        double latitudeRad = Math.toRadians(latitudeInDegree);
        double x = radius * Math.sin(longitudeRad) * Math.cos(latitudeRad);
        double y = radius * Math.sin(longitudeRad) * Math.sin(latitudeRad);
        double z = radius * Math.cos(longitudeRad);
        return new double[]{x, y, z};
    }

    @Override
    protected double doCalculateDistance(Coordinate otherCoord) {
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
    protected void assertClassInvariants() {
        assertLatitude(getLatitude());
        assertLongitude(getLongitude());
        assertRadius(getRadius());
    }

    /**
     * <a href=https://de.wikipedia.org/wiki/Kugelkoordinaten section "Andere Konventionen">source</a>
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        assertClassInvariants();
        double[] cartesianOrdinates = toCartesianOrdinates(getLatitude(), getLongitude(), getRadius());
        return new CartesianCoordinate(cartesianOrdinates[0], cartesianOrdinates[1], cartesianOrdinates[2]);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public String toString() {
        return "(" + latitude + "," + longitude + "), r= " + radius + ")";
    }

    private void assertLatitude(double latitude) {
        Assert.inRangeMax(latitude, LATITUDE_MAX_VALUE);
    }

    private void assertLongitude(double longitude) {
        Assert.inRangeMax(longitude, LONGITUDE_MAX_VALUE, "Longitude");
    }

    private void assertRadius(double radius) {
        Assert.notNegative(radius);
    }
}
