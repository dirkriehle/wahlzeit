/*
 *  Copyright
 *
 *  Classname: SphericCoordinateTest
 *  Author: Tango1266
 *  Version: 16.11.17 16:05
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
import org.wahlzeit.model.coordinates.impl.SphericCoordinate;

import static org.wahlzeit.model.coordinates.impl.SphericCoordinate.*;

/**
 * https://www.kompf.de/gps/distcalc.html
 */
public class SphericCoordinateTest extends CoordinateTest {

    private SphericCoordinate ruesselsheimBhf;
    private SphericCoordinate ruesselsheimOpelbruecke;
    final double VALUE = 2.22;
    final double MIN_PRECISION = 0.0000001;
    final double LATITUDE_MAX = 180.00;
    final double LONGITUDE_MAX = 90.00;
    SphericCoordinate unsettedCoord;
    protected Coordinate berlinBarndBurgSpheric;
    protected Coordinate lissabonBrueckeSpheric;

    @Before
    public void setUp() {
        unsettedCoord = getCoordinate();

        //Coords from https://www.kompf.de/gps/distcalc.html
        ruesselsheimBhf = getCoordinate(49.9917, 8.41321);
        ruesselsheimOpelbruecke = getCoordinate(50.0049, 8.42182);

        berlinBarndBurgSpheric = getCoordinate(52.5164, 13.3777);
        lissabonBrueckeSpheric = getCoordinate(38.692668, -9.177944);
    }

    @Test
    public void staticMaxValues_forLongitude_is90() {
        Assert.assertEquals(LATITUDE_MAX, LATITUDE_MAX_VALUE, 0);
        Assert.assertEquals(LONGITUDE_MAX, LONGITUDE_MAX_VALUE, 0);
    }

    @Test
    public void staticMaxValues_forLatitude_is180() {
        Assert.assertEquals(LATITUDE_MAX, LATITUDE_MAX_VALUE, 0);
        Assert.assertEquals(LONGITUDE_MAX, LONGITUDE_MAX_VALUE, 0);
    }

    @Test
    public void createSpericCoordinate_notNull() {
        Assert.assertNotNull(getCoordinate());
    }

    @Test
    public void getLongitude_afterCreatingCoordWithValue_returnsValue() {
        SphericCoordinate sphCord = getCoordinate(-VALUE, VALUE);
        Assert.assertEquals(VALUE, sphCord.getLongitude(), 0);
    }

    @Test
    public void getLongitude_afterSettingItToValue_returnsValue() {
        SphericCoordinate sphCord = getCoordinate(0, VALUE);
        Assert.assertEquals(VALUE, sphCord.getLongitude(), 0);
    }

    @Test
    public void getLatitude_afterSettingItToValue_returnsValue() {
        SphericCoordinate sphCord = getCoordinate(VALUE, 0);

        Assert.assertEquals(VALUE, sphCord.getLatitude(), 0);
    }

    @Test
    public void getRadius_afterSettingItToValue_returnsValue() {
        SphericCoordinate sphCord = getCoordinate(0, 0, VALUE);
        Assert.assertEquals(VALUE, sphCord.getRadius(), 0);
    }

    @Test
    public void sphericCoord_isSubclassOfCoordinate_isTrue() {
        Assert.assertTrue(Coordinate.class.isAssignableFrom(SphericCoordinate.class));
    }

    @Test
    public void sphericCoord_isSuperclassOfCoordinate_isFalse() {
        Assert.assertFalse(SphericCoordinate.class.isAssignableFrom(Coordinate.class));
    }

    @Test
    public void setLongitude_withinMaxValueRange_shouldNotThrowException() {
        try {
            getCoordinate(0, LONGITUDE_MAX);
            getCoordinate(0, -LONGITUDE_MAX);
            unsettedCoord = getCoordinate();
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test
    public void setLatitude_withinMaxValueRange_shouldNotThrowException() {
        try {
            getCoordinate(LATITUDE_MAX, 0);
            getCoordinate(-LATITUDE_MAX, 0);
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLongitude_greaterThan90_shouldThrowException() {
        getCoordinate(0, LONGITUDE_MAX + MIN_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLongitude_lessThanMinus90_shouldThrowException() {
        getCoordinate(0, -LONGITUDE_MAX - MIN_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLatidude_greaterThan180_shouldThrowException() {
        getCoordinate(LATITUDE_MAX + MIN_PRECISION, 0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void setLatidude_lessThanMinus180_shouldThrowException() {
        getCoordinate(-LATITUDE_MAX - MIN_PRECISION, 0);

    }

    @Test
    public void getSphericDistance_rueselBhfToOpelBrueck_returns1593() {
        double expected = 1593;
        double tolerance = expected * 0.5;
        Assert.assertEquals(expected, ruesselsheimBhf.getSphericDistance(ruesselsheimOpelbruecke), tolerance);
        Assert.assertEquals(expected, ruesselsheimBhf.getDistance(ruesselsheimOpelbruecke), tolerance);
    }

    @Test
    public void getSphericDistance_berlinToLissabon_returns2317722() {
        double expected = 2317722;
        double tolerance = expected * 0.1;
        Assert.assertEquals(expected, berlinBarndBurgSpheric.getSphericDistance(lissabonBrueckeSpheric), tolerance);
        Assert.assertEquals(expected, berlinBarndBurgSpheric.getDistance(lissabonBrueckeSpheric), tolerance);
    }

    @Test
    public void isEquals_berlinAndLissabon_isFalse() {
        Assert.assertFalse(berlinBarndBurgSpheric.isEqual(lissabonBrueckeSpheric));
    }

    @Test
    public void isEquals_berlinAndBerlin_isTrue() {
        Assert.assertTrue(berlinBarndBurgSpheric.isEqual(berlinBarndBurgSpheric));
    }

    @Test
    public void getCartesianDistance_rueselBhfToOpelBrueck_returns1593() {
        double expected = 1593;
        double tolerance = expected * 0.5;
        Assert.assertEquals(expected, ruesselsheimBhf.getCartesianDistance(ruesselsheimOpelbruecke), tolerance);
    }

    @Test
    public void getCartesianDistance_berlinToLissabon_returns2317722() {
        double expected = 2317722;
        double tolerance = expected * 0.1;
        Assert.assertEquals(expected, berlinBarndBurgSpheric.getCartesianDistance(lissabonBrueckeSpheric), tolerance);
    }

    /**
     * http://www.learningaboutelectronics.com/Articles/Spherical-to-cartesian-rectangular-coordinate-converter-calculator.php#answer
     */
    @Test
    public void calculationForConvertingSpheric_toCartFromInternetExample_willHaveSameResult() {
        double theta = 20.00; //longitude
        double phi = 60.00; //latitude
        double r = 12;

        double x = r * Math.sin(Math.toRadians(theta)) * Math.cos(Math.toRadians(phi));
        double y = r * Math.sin(Math.toRadians(theta)) * Math.sin(Math.toRadians(phi));
        double z = r * Math.cos(Math.toRadians(theta));

        assertEqual(2.05, x);
        assertEqual(3.55, y);
        assertEqual(11.28, z);

        assertEqual(x, toCartesianOrdinates(phi, theta, r)[0]);
        assertEqual(y, toCartesianOrdinates(phi, theta, r)[1]);
        assertEqual(z, toCartesianOrdinates(phi, theta, r)[2]);
    }

    @Test
    public void twoCreatedCoordinates_withSameValues_areSameInstance() {
        SphericCoordinate sphericCoordinate = SphericCoordinate.getCoordinate(1, 2);
        Assert.assertTrue(sphericCoordinate == SphericCoordinate.getCoordinate(1, 2));
    }

    @Test
    public void twoCreatedCoordinates_withDifferentValues_areDofferentInstance() {
        SphericCoordinate coordA = SphericCoordinate.getCoordinate(1, 2);
        SphericCoordinate coordB = SphericCoordinate.getCoordinate(2, 1);
        Assert.assertFalse(coordA == coordB);
    }
}
