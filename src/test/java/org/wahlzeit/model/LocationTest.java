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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.model.coordinates.Coordinate;
import org.wahlzeit.model.coordinates.impl.CartesianCoordinate;
import org.wahlzeit.model.coordinates.impl.NoWhereCoordinate;

public class LocationTest {

    private Location noWhereLocation;
    private Location fooLocation;
    private Location barLocation;

    private Coordinate fooCoords;
    private Coordinate barCoords;

    @Before
    public void setUp() {
        noWhereLocation = new Location(new NoWhereCoordinate());
        fooCoords = CartesianCoordinate.getCoordinate(1, 0, 2);
        barCoords = CartesianCoordinate.getCoordinate(-1, -2, 0);
        fooLocation = new Location(fooCoords);
        barLocation = new Location(barCoords);
    }

    @Test
    public void createLocation_withoutCoordinate_isNotNull() {
        Location noWhere = new Location();
        Assert.assertNotNull(noWhere.getCoordinate());
    }

    @Test
    public void createLocation_withCoordinate_isNotNull() {
        Coordinate noWhereCoordinate = new NoWhereCoordinate();
        Location noWhere = new Location(noWhereCoordinate);
        Assert.assertNotNull(noWhere.getCoordinate());
        noWhere.equals(noWhere);
    }

    @Test
    public void getCoordinate_afterCreateLocation_withCoordinate_shouldReturnNoWhereCoord() {
        Location noWhere = new Location();
        Assert.assertEquals(NoWhereCoordinate.class, noWhere.getCoordinate().getClass());
    }

    @Test
    public void getCoordinate_afterSetCoordinateWithNull_shouldReturnNoWhereCoord() {
        Location noWhere = new Location(null);
        noWhere.setCoordinate(null);
        Assert.assertEquals(NoWhereCoordinate.class, noWhere.getCoordinate().getClass());
    }

    @Test
    public void locationEquals_whenCoordsEquals_isTrue() {
        Assert.assertEquals(fooLocation, new Location(fooCoords));
    }

    @Test
    public void locationEquals_whenCoordsNotEquals_isTrue() {
        Assert.assertNotEquals(fooLocation, barLocation);
    }

    @Test
    public void distanceOfNowWhere_andNoWhere_isMinus1() {
        Assert.assertEquals(-1, noWhereLocation.getDistance(noWhereLocation), 0);
    }

    @Test
    public void distanceOfFooLocation_andNoWhere_isMinus1() {
        Assert.assertEquals(-1, fooLocation.getDistance(noWhereLocation), 0);
    }

    @Test
    public void distanceOfFooLocation_andBarLocation_isNotMinus1() {
        Assert.assertNotEquals(-1, fooLocation.getDistance(barLocation), 0);
    }
}
