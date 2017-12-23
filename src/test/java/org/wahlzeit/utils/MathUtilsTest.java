/*
 *  Copyright
 *
 *  Classname: MathUtilsTest
 *  Author: Tango1266
 *  Version: 17.11.17 15:47
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

package org.wahlzeit.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MathUtilsTest {
    private final double FIVE_DECIMAL_PRECISION = 0.00001;
    private double valueA;
    private double valueA2;
    private double precision1E_7;
    private double sixDecimalValueA;
    private double sixDecimalValueB;

    @Test
    public void round_sixDecimalValueA_AndB_on5Decimals_resultsAreEqual() {
        int decimals = 5;
        String roundToDecimals = "%." + decimals + "f ";
        System.out.printf(roundToDecimals + "\n" + roundToDecimals, sixDecimalValueA, sixDecimalValueB);
        double value1 = MathUtils.round(sixDecimalValueA, decimals);
        double value2 = MathUtils.round(sixDecimalValueB, decimals);
        System.out.println("\n____________________\n" + value1 + "\n" + value2);
        Assert.assertEquals(value1, value2, FIVE_DECIMAL_PRECISION);
    }

    @Test
    public void round_sixDecimalValueA_AndB_on4Decimals_resultsAreNotEqual() {
        int decimals = 4;
        String roundToDecimals = "%." + decimals + "f ";
        System.out.printf(roundToDecimals + "\n" + roundToDecimals, sixDecimalValueA, sixDecimalValueB);
        double value1 = MathUtils.round(sixDecimalValueA, decimals);
        double value2 = MathUtils.round(sixDecimalValueB, decimals);
        System.out.println("\n____________________\n" + value1 + "\n" + value2);
        Assert.assertNotEquals(value1, value2, FIVE_DECIMAL_PRECISION);
    }

    @Before
    public void setUp() {
        MathUtils.setPrecision(MathUtils.default_precision);
        valueA = 1E-7;
        valueA2 = 2E-7;
        precision1E_7 = 1E-7;
        sixDecimalValueA = 1.123454;
        sixDecimalValueB = 1.123449;
    }

    @Test
    public void doublesAreNotEqual_valueA_and_A2_withDefaultPrecision_true() {
        Assert.assertTrue(MathUtils.doublesAreNotEqual(valueA, valueA2));
    }

    @Test
    public void doublesAreEqual_valueA_and_A2_whenPrecisionSetTo_1E_7() {
        MathUtils.setPrecision(precision1E_7);
        Assert.assertTrue(MathUtils.doublesAreEqual(valueA, valueA2));
    }

    @Test
    public void doublesAreEqual_valueA_and_A2_withPrecision_1E_7() {
        Assert.assertTrue(MathUtils.doublesAreEqual(valueA, valueA2, precision1E_7));
    }
}