/*
 *  Copyright
 *
 *  Classname: CoordinateTest
 *  Author: Tango1266
 *  Version: 16.11.17 16:10
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.model.coordinates.impl.CartesianCoordinate;
import org.wahlzeit.model.coordinates.impl.SphericCoordinate;

public class CoordinateTest {
    protected static final Double VALUE_EXCEEDING_COORD_MAXVALUE = Double.MAX_VALUE - 1E291;

    protected Coordinate berlinBarndBurgCartesian;
    protected Coordinate lissabonBrueckeCartesian;
    protected Coordinate berlinBarndBurgSpheric;
    protected Coordinate lissabonBrueckeSpheric;

    @Before
    public void setUpTest() {
        berlinBarndBurgCartesian = new CartesianCoordinate(897_997.802, 1_170_987.37, 6_204_939.37);
        lissabonBrueckeCartesian = new CartesianCoordinate(-794_012.08, -635_956.82, 6_296_347.17);

        berlinBarndBurgSpheric = new SphericCoordinate(52.5164, 13.3777);
        lissabonBrueckeSpheric = new SphericCoordinate(38.692668, -9.177944);
    }

    @Test
    public void convertingBerlinCartesian_toSphericAndBack_inIsEqualOut() {
        Coordinate convertedToSpherich = berlinBarndBurgCartesian.asSphericCoordinate();
        Coordinate convertedBackToCart = convertedToSpherich.asCartesianCoordinate();
        Assert.assertEquals(berlinBarndBurgCartesian, convertedBackToCart);
    }

    @Test
    public void convertingZeroSpheric_toCartesianAndBack_inIsEqualOut() {
        Coordinate spheric = new SphericCoordinate(0, 0);
        Assert.assertEquals(spheric, spheric.asCartesianCoordinate().asSphericCoordinate());
    }

    @Test
    public void convertingZeroCartesion_toSphericAndBack_inIsEqualOut() {
        Coordinate cartesian = new CartesianCoordinate(0, 0, 0);
        Assert.assertEquals(cartesian, cartesian.asSphericCoordinate().asCartesianCoordinate());
    }

    @Test
    public void creatingTwoCartesions_whereOneIsBuildFromSpheric_areEqual() {
        Coordinate spheric = new SphericCoordinate(10, 12);
        Coordinate cartesian = convertToCartesian(spheric);
        Assert.assertEquals(spheric, (cartesian.asSphericCoordinate()));
    }

    @Test
    public void getSphericBerlinToLissabon_afterCreateCartesianFromSphericAndConvertedBack__returns2317722() {
        double expected = 2317722;
        double tolerance = expected * 0.4;
          /*TODO: Test throws Exception when Creating a Spheric Coordinate:
          * java.lang.IllegalArgumentException: The value "-141.3073319997775" must be in the range of [-90.0:90.0]*/

        Coordinate berlinCartesian = convertToCartesian(berlinBarndBurgSpheric);
        Coordinate lissabonCartesian = convertToCartesian(lissabonBrueckeSpheric);

        SphericCoordinate berlinSphericConverted = berlinCartesian.asSphericCoordinate();
        SphericCoordinate lissabonSphericConverted = lissabonCartesian.asSphericCoordinate();
        Assert.assertEquals(expected, berlinSphericConverted.getSphericDistance(lissabonSphericConverted), tolerance);
    }




    protected static void checkDistance(Coordinate first, Coordinate second, double expectedDistance, double tolerance) {
        double distance = first.getDistance(second);
        Assert.assertEquals(expectedDistance, distance, tolerance);
    }

    private Coordinate convertToCartesian(Coordinate sphericCoord) {
        CartesianCoordinate cartesianConverted = sphericCoord.asCartesianCoordinate();
        return new CartesianCoordinate(cartesianConverted.getX(), cartesianConverted.getY(), cartesianConverted.getZ());
    }
}
