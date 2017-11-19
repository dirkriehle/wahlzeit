/*
 *  Copyright
 *
 *  Classname: NoWhereCoordinate
 *  Author: Tango1266
 *  Version: 16.11.17 15:23
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

import static java.lang.System.identityHashCode;

/**
 * Represents a null-coordinate with defined "do-nothing-behavior" when interacting with it
 */
public class NoWhereCoordinate implements Coordinate {

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return new CartesianCoordinate(0, 0, 0);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return new SphericCoordinate(0, 0, 0);
    }

    /**
     * Determines the distance between something and nothing
     * @return -1
     */
    @Override
    public double getDistance(Coordinate otherCoord) {
        return -1;
    }

    @Override
    public double getCartesianDistance(Coordinate otherCoord) {
        return -1;
    }

    @Override
    public double getSphericDistance(Coordinate otherCoord) {
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
