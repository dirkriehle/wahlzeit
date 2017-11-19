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
