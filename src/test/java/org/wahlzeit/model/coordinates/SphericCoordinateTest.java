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

/**
 * https://www.kompf.de/gps/distcalc.html
 */
public class SphericCoordinateTest extends CoordinateTest {

    private SphericCoordinate ruesselsheimBhf;
    private SphericCoordinate ruesselsheimOpelbruecke;
    final double[] RADIANS = {-1.0, 0, 0.1, 1, 123456};
    final double VALUE = 2.22;
    final double MIN_PRECISION = 0.00000001;
    final double LATITUDE_MAX = 180.00;
    final double LONGITUDE_MAX = 90.00;
    SphericCoordinate unsettedCoord;
    protected Coordinate berlinBarndBurgSpheric;
    protected Coordinate lissabonBrueckeSpheric;

    @Before
    public void setUp() {
        unsettedCoord = new SphericCoordinate();

        //Coords from https://www.kompf.de/gps/distcalc.html
        ruesselsheimBhf = new SphericCoordinate(49.9917, 8.41321);
        ruesselsheimOpelbruecke = new SphericCoordinate(50.0049, 8.42182);

        berlinBarndBurgSpheric = new SphericCoordinate(52.5164, 13.3777);
        lissabonBrueckeSpheric = new SphericCoordinate(38.692668, -9.177944);
    }

    @Test
    public void staticMaxValues_forLongitude_is90() {
        Assert.assertEquals(LATITUDE_MAX, SphericCoordinate.LATITUDE_MAX_VALUE, 0);
        Assert.assertEquals(LONGITUDE_MAX, SphericCoordinate.LONGITUDE_MAX_VALUE, 0);
    }

    @Test
    public void staticMaxValues_forLatitude_is180() {
        Assert.assertEquals(LATITUDE_MAX, SphericCoordinate.LATITUDE_MAX_VALUE, 0);
        Assert.assertEquals(LONGITUDE_MAX, SphericCoordinate.LONGITUDE_MAX_VALUE, 0);
    }

    @Test
    public void createSpericCoordinate_notNull() {
        Assert.assertNotNull(new SphericCoordinate());
    }

    @Test
    public void getLongitude_afterCreatingCoordWithValue_returnsValue() {
        SphericCoordinate sphCord = new SphericCoordinate(-VALUE, VALUE);
        Assert.assertEquals(VALUE, sphCord.getLongitude(), 0);
    }

    @Test
    public void getLongitude_afterSettingItToValue_returnsValue() {
        SphericCoordinate sphCord = new SphericCoordinate();
        sphCord.setLongitude(VALUE);
        Assert.assertEquals(VALUE, sphCord.getLongitude(), 0);
    }

    @Test
    public void getLatitude_afterSettingItToValue_returnsValue() {
        SphericCoordinate sphCord = new SphericCoordinate();
        sphCord.setLatitude(VALUE);
        Assert.assertEquals(VALUE, sphCord.getLatitude(), 0);
    }

    @Test
    public void getRadius_afterSettingItToValue_returnsValue() {
        SphericCoordinate sphCord = new SphericCoordinate();
        sphCord.setRadius(VALUE);
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
            unsettedCoord.setLongitude(LONGITUDE_MAX);
            unsettedCoord.setLongitude(-LONGITUDE_MAX);
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test
    public void setLatitude_withinMaxValueRange_shouldNotThrowException() {
        try {
            unsettedCoord.setLatitude(LATITUDE_MAX);
            unsettedCoord.setLatitude(-LATITUDE_MAX);
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLongitude_greaterThan90_shouldThrowException() {
        unsettedCoord.setLongitude(LONGITUDE_MAX + MIN_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLongitude_lessThanMinus90_shouldThrowException() {
        unsettedCoord.setLongitude(-LONGITUDE_MAX - MIN_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLatidude_greaterThan180_shouldThrowException() {
        unsettedCoord.setLatitude(LATITUDE_MAX + MIN_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLatidude_lessThanMinus180_shouldThrowException() {
        unsettedCoord.setLatitude(-LATITUDE_MAX - MIN_PRECISION);
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
}
