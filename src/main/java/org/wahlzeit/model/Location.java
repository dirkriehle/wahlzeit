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

import org.wahlzeit.model.coordinates.Coordinate;
import org.wahlzeit.model.coordinates.impl.NoWhereCoordinate;

/**
 * Represents a geographical Location
 */
public class Location {
    /*TODO: discuss requirement change so this field can be made private*/
    public Coordinate coordinate;

    public Location(Coordinate coordinate) {
        setCoordinate(coordinate);
    }

    public Location() {
        setCoordinate(new NoWhereCoordinate());
    }

    /**
     * @return either {@link NoWhereCoordinate} or {@link Coordinate}
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * sets the coordinates to either provided Coordinate or if null to NoWhereCoordinate.
     */
    public void setCoordinate(Coordinate coordinate) {
        if (coordinate == null) {
            this.coordinate = new NoWhereCoordinate();
            return;
        }
        this.coordinate = coordinate;
    }

    /**
     * {@link Coordinate#equals(Object) Coordinate#equals}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            return coordinate.equals(((Location) obj).coordinate);
        }
        return false;
    }

    /**
     * {@link Coordinate#getDistance(Coordinate)}  Coordinate#getDistance}
     */
    public double getDistance(Location noWhereLocation) {
        return coordinate.getDistance(noWhereLocation.coordinate);
    }
}
