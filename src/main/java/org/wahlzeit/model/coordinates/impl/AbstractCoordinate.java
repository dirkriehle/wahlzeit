/*
 *  Copyright
 *
 *  Classname: AbstractCoordinate
 *  Author: Tango1266
 *  Version: 22.11.17 17:39
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

public abstract class AbstractCoordinate implements Coordinate {

    @Override
    public abstract int hashCode();

    /**
     * {@link #isEqual(Coordinate) isEqual(Coordinate)}
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Coordinate && isEqual((Coordinate) o);
    }

    @Override
    public abstract CartesianCoordinate asCartesianCoordinate();

    @Override
    public abstract SphericCoordinate asSphericCoordinate();

    /**
     * @return -1, if otherCood is null or NoWhereCoordinate. The direct distance, otherwise.
     */
    @Override
    public double getDistance(Coordinate otherCoord) {
        return getCartesianDistance(otherCoord);
    }

    @Override
    public abstract double getCartesianDistance(Coordinate otherCoord);

    @Override
    public abstract double getSphericDistance(Coordinate otherCoord);

    @Override
    public abstract boolean isEqual(Coordinate otherCoord);
}
