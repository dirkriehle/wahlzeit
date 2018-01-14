/*
 *  Copyright
 *
 *  Classname: Coordinate
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

package org.wahlzeit.model.coordinates;

import org.wahlzeit.model.coordinates.impl.CartesianCoordinate;
import org.wahlzeit.model.coordinates.impl.SphericCoordinate;

public interface Coordinate {

    /**
     * @return this, if instanceof {@link CartesianCoordinate},
     * otherwise a new instance of {@link CartesianCoordinate}
     * with attributes which where interpreted from attributes of this instance
     * @methodtype conversion
     */
    CartesianCoordinate asCartesianCoordinate();

    /**
     * @return this, if instanceof {@link SphericCoordinate},
     * otherwise new instance of {@link SphericCoordinate}
     * with attributes which where interpreted from attributes of this instance
     * @methodtype conversion
     */
    SphericCoordinate asSphericCoordinate();

    /**
     * @see {@link #getCartesianDistance(Coordinate)}
     */
    double getDistance(Coordinate otherCoord);

    /**
     * @return -1 if otherCoord is NoWhereCoord,
     * else the direct distance between two points within the cartesian system
     */
    double getCartesianDistance(Coordinate otherCoord);

    /**
     * @return -1 if otherCoord is NoWhereCoord,
     * else the distance between two points on a sphere surface
     * with the default radius {@link SphericCoordinate#EARTH_RADIUS_METER}
     */
    double getSphericDistance(Coordinate otherCoord);

    /**
     * @return TRUE, if the cartesian distance between two {@link Coordinate}
     * is equal within the failure {@link org.wahlzeit.utils.MathUtils#default_precision}.
     * @see  {@link Coordinate#getDistance(Coordinate)}
     * @methodtype boolean-query
     */
    boolean isEqual(Coordinate otherCoord);

}
