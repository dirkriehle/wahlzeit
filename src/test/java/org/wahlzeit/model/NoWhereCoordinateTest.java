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

public class NoWhereCoordinateTest {
    static final Double VALUE_EXCEEDING_COORD_MAXVALUE = Double.MAX_VALUE - 1E291;

    protected Coordinate noWhere;
    protected Coordinate octantIa;
    protected Coordinate octantIb;
    protected Coordinate octantVII;
    protected Coordinate layerXYa;
    protected Coordinate layerXYb;

    protected void CheckDistance(Coordinate first, Coordinate second, double expectedDistance, double tolerance) {
        double distance = first.getDistance(second);
        Assert.assertEquals(expectedDistance, distance, tolerance);
    }

    @Before
    public void initTest() {
        noWhere = new NoWhereCoordinate();
        octantIa = new Coordinate(1.0, 2.0, 3.0);
        octantIb = new Coordinate(1.0, 2.0, 3.0);
        octantVII = new Coordinate(-1.0, -2.0, -3.0);
        layerXYa = new Coordinate(0, 1, 0);
        layerXYb = new Coordinate(5, 1, 0);
    }

    @Test
    public void coordinateNoWhere_andNoWhere_areNotEqual() {
        Assert.assertFalse(noWhere.equals(new NoWhereCoordinate()));
    }

    @Test
    public void coordinateNoWhere_andNull_areNotEqual() {
        Assert.assertFalse(noWhere.equals(null));
    }

    @Test
    public void someCoordinate_andNoWhere_areNotEqual() {
        Assert.assertFalse(octantIa.equals(noWhere));
        Assert.assertFalse(octantIb.equals(noWhere));
        Assert.assertFalse(octantVII.equals(noWhere));
    }

    @Test
    public void distanceOfAnyCoord_andNoWhere_isMinus1() {
        CheckDistance(octantIa, noWhere, -1, 0);
        CheckDistance(noWhere, octantIb, -1, 0);
    }

    @Test
    public void distanceOfAnyCoord_andNull_isMinus1() {
        CheckDistance(octantIa, null, -1, 0);
        CheckDistance(noWhere, null, -1, 0);
    }

    @Test
    public void hashCodes_ofNoWhereCoordinates_areNotEqual() {
        Assert.assertNotEquals(noWhere.hashCode(), new NoWhereCoordinate().hashCode());
    }
}