/*
 *  Copyright
 *
 *  Classname: CartesianCoordinateTest
 *  Author: Tango1266
 *  Version: 16.11.17 15:58
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

import java.util.HashMap;

import static org.wahlzeit.model.coordinates.CoordinateTest.VALUE_EXCEEDING_COORD_MAXVALUE;
import static org.wahlzeit.model.coordinates.CoordinateTest.checkDistance;

public class CartesianCoordinateTest {

    CartesianCoordinate octantIa;
    CartesianCoordinate octantIb;
    CartesianCoordinate octantVII;
    CartesianCoordinate layerXYa;
    CartesianCoordinate layerXYb;

    protected Coordinate berlinBarndBurgCartesian;
    protected Coordinate lissabonBrueckeCartesian;

    /*CreationTests*/
    @Before
    public void setUp() {
        octantIa = new CartesianCoordinate(2.0, 1.0, 3.0);
        octantIb = new CartesianCoordinate(2.0, 1.0, 3.0);
        octantVII = new CartesianCoordinate(-2.0, -1.0, -3.0);
        layerXYa = new CartesianCoordinate(1, 0, 0);
        layerXYb = new CartesianCoordinate(1, 5, 0);

        berlinBarndBurgCartesian = new CartesianCoordinate(897_997.802, 1_170_987.368, 6_204_939.366);
        lissabonBrueckeCartesian = new CartesianCoordinate(-794_012.0811, -635_956.8219, 6_296_347.174);
    }

    @Test
    public void createCoordinate_initThroughSetter_notNull() {
        CartesianCoordinate foo = new CartesianCoordinate();
        foo.setX(0.0);
        foo.setY(0.0);
        foo.setZ(-1.0);
        Assert.assertNotNull(foo);
    }

    @Test
    public void createCoordinate_initThroughConstructor_notNull() {
        Coordinate bar = new CartesianCoordinate(0.0, 0.0, -1.0);
        Assert.assertNotNull(bar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_withExceedingMaxValueInZ_shouldThroughException() {
        new CartesianCoordinate(1, 1, VALUE_EXCEEDING_COORD_MAXVALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_withExceedingMaxValueInX_shouldThroughException() {
        new CartesianCoordinate(VALUE_EXCEEDING_COORD_MAXVALUE, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_withExceedingMaxValueInY_shouldThroughIllegalArgumentException() {
        new CartesianCoordinate(1, VALUE_EXCEEDING_COORD_MAXVALUE, 1);
    }

    @Test
    public void createCoordinate_belowMaxValue_shouldNOTThroughExceptions() {
        Double valueBelowCoordMaxValue = Double.MAX_VALUE - 1E292;
        try {
            new CartesianCoordinate(1, 1, valueBelowCoordMaxValue);
            new CartesianCoordinate(valueBelowCoordMaxValue, 1, 1);
            new CartesianCoordinate(1, valueBelowCoordMaxValue, 1);
        } catch (Exception ex) {
            Assert.fail("Exception was thrown but shouldn't.");
        }
    }

    @Test
    public void createCoordinate_withCoordMaxValue_shouldNOTThroughExceptions() {
        try {
            new CartesianCoordinate(1, 1, CartesianCoordinate.MAX_VALUE);
            new CartesianCoordinate(CartesianCoordinate.MAX_VALUE, 1, 1);
            new CartesianCoordinate(1, CartesianCoordinate.MAX_VALUE, 1);
        } catch (Exception ex) {
            Assert.fail("Exception was thrown but shouldn't.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_belowCoordMinValues_shouldThroughIllegalArgumentException() {
        new CartesianCoordinate(1, 1, -VALUE_EXCEEDING_COORD_MAXVALUE);
    }

    @Test
    public void createCoordinate_aboveCoordMinValue_shouldNOTThroughExceptions() {
        try {
            new CartesianCoordinate(1, 1, -CartesianCoordinate.MAX_VALUE);
            new CartesianCoordinate(-CartesianCoordinate.MAX_VALUE, 1, 1);
            new CartesianCoordinate(1, -CartesianCoordinate.MAX_VALUE, 1);
        } catch (Exception ex) {
            Assert.fail("Exception was thrown but shouldn't.");
        }
    }
    /*EqualityTests*/

    @Test
    public void octantIa_andB_areEqual() {
        Assert.assertEquals(octantIa, octantIb);
    }

    @Test
    public void octantVII_andOctantIa_areNotEqual() {
        Assert.assertNotEquals(octantIa, octantVII);
    }

    @Test
    public void someString_andOctantIa_areNotEqual() {
        Assert.assertFalse(octantIa.equals("fooLocation"));
    }

    @Test
    public void octantIa_andNull_areNotEqual() {
        Assert.assertFalse(octantIa.equals(null));
    }

    @Test
    public void octantIa_multipliedByMinusOne_isOctantVII() {
        octantIa.setX(octantIa.getX() * (-1));
        octantIa.setY(octantIa.getY() * (-1));
        octantIa.setZ(octantIa.getZ() * (-1));

        Assert.assertTrue(octantIa.equals(octantVII));
    }

    @Test
    public void octantIa_andB_withDifferentOrdinateX_areNotEqual() {
        octantIb.setX(octantIb.getX() + 1);
        Assert.assertNotEquals(octantIa, octantIb);
    }

    @Test
    public void octantIa_andB_withDifferentOrdinateY_areNotEqual() {
        octantIb.setY(octantIb.getY() + 1);
        Assert.assertNotEquals(octantIa, octantIb);
    }

    @Test
    public void octantIa_andB_withDifferentOrdinateZ_areNotEqual() {
        octantIb.setZ(octantIb.getZ() + 1);
        Assert.assertNotEquals(octantIa, octantIb);
    }

    @Test
    public void someCoordinates_withMarginalDifferentOrdinate_areNotEqual() {
        double smallValueA = 1.0E-6;
        double smallValueB = 1.1E-6;
        Coordinate coordinateA = new CartesianCoordinate(smallValueA, smallValueA, smallValueA);
        Coordinate coordinateB = new CartesianCoordinate(smallValueB, smallValueB, smallValueB);
        Assert.assertNotEquals(coordinateA, coordinateB);
    }

    @Test
    public void someCoordinate_withCalculatedOrdinate_areEqual() {
        double smallValueA = 1.0;
        double smallValueB = 1 / 3.0 + 1 / 3.0 + 1 / 3.0;
        Coordinate coordinateA = new CartesianCoordinate(smallValueA, smallValueA, smallValueA);
        Coordinate coordinateB = new CartesianCoordinate(smallValueB, smallValueB, smallValueB);
        Assert.assertEquals(coordinateA, coordinateB);
    }

    @Test
    public void hashCodes_ofOctantIa_andB_areEqual() {
        Assert.assertEquals(octantIa.hashCode(), octantIb.hashCode());
    }

    @Test
    public void hashCodes_ofOctantIa_andOctantVII_areNotEqual() {
        Assert.assertNotEquals(octantIa.hashCode(), octantVII.hashCode());
    }

    @Test
    public void puttingInHashMap_ofEqualCoordinates_shouldRecognizedOnce() {
        HashMap hashMap = new HashMap();
        hashMap.put(octantIa, "octantIa");
        hashMap.put(octantIb, "octantIb");
        Assert.assertEquals(1, hashMap.size());
        Assert.assertEquals(hashMap.get(octantIa), "octantIb");
    }

    @Test
    public void setOctantIa_andOctantVII_comparedUsingHashCode_areNotEqual() {
        Assert.assertFalse(octantIa == octantVII);
    }
    /*DistanceTests*/

    @Test
    public void distanceOfCoordinateA_andB_is0() {
        checkDistance(octantIa, octantIb, 0, 0);
    }

    @Test
    public void distanceOfLayerXYa_andXYb_is5() {
        checkDistance(layerXYa, layerXYb, 5, 0);
    }

    @Test
    public void distanceOfCoordinateB_andC_is7_5() {
        checkDistance(octantIb, octantVII, 7.5, 0.02);
    }

    @Test
    public void distanceOfCoordinate_withOneCoordinateMaxAndMinOrdinate_is0() {
        Coordinate minCoordinate = new CartesianCoordinate(0, CartesianCoordinate.MAX_VALUE * (-1), 0);
        Coordinate maxCoordinate = new CartesianCoordinate(0, CartesianCoordinate.MAX_VALUE, 0);
        double expectedDistance = CartesianCoordinate.MAX_VALUE - CartesianCoordinate.MAX_VALUE * (-1);
        Assert.assertEquals(expectedDistance, minCoordinate.getDistance(maxCoordinate), 0);
    }


    @Test
    public void getCartesianDistance_berlinToLissabon_returns2317722() {
        double expected = 2317722;
        double tolerance = expected * 0.1;
        Assert.assertEquals(expected, berlinBarndBurgCartesian.getCartesianDistance(lissabonBrueckeCartesian), tolerance);
        Assert.assertEquals(expected, berlinBarndBurgCartesian.getDistance(lissabonBrueckeCartesian), tolerance);
    }

    @Test
    public void getSphericDistance_berlinToLissabon_returns2317722() {
        double expected = 2317722;
        double tolerance = expected * 0.5;
          /*TODO: Test throws Exception when Creating a Spheric Coordinate:
          * java.lang.IllegalArgumentException: The value "-141.3073319997775" must be in the range of [-90.0:90.0]*/
        SphericCoordinate berlinSphericConverted = berlinBarndBurgCartesian.asSphericCoordinate();
        SphericCoordinate lissabonSphericConverted = lissabonBrueckeCartesian.asSphericCoordinate();
        Assert.assertEquals(expected, berlinSphericConverted.getSphericDistance(lissabonSphericConverted), tolerance);
//        Assert.assertEquals(expected, berlinBarndBurgCartesian.getSphericDistance(lissabonBrueckeCartesian), tolerance);
    }

}
