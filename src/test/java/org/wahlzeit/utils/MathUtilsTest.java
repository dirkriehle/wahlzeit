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
import org.junit.Test;

import static org.wahlzeit.utils.MathUtils.sqrtOfSum;
import static org.wahlzeit.utils.MathUtils.square;

public class MathUtilsTest {

    /**
     * http://www.learningaboutelectronics.com/Articles/Cartesian-rectangular-to-spherical-coordinate-converter-calculator.php#answer
     */
    @Test
    public void calculationForConvertingCart_toSphericFromInternetExample_willHaveSameResult() {
        double x = 2.0, y = 3.0, z = 8.0;
        double r = sqrtOfSum(square(x), square(y), square(z));
        double theta = Math.acos(z / r);
        double phi = Math.atan(y / 2);

        assertEqual(8.77, r);
        assertEqual(24.26, Math.toDegrees(theta));
        assertEqual(56.31, Math.toDegrees(phi));

        assertEqual(8.77, MathUtils.toSphericalOrdinates(x, y, z)[2]);
        assertEqual(24.26, MathUtils.toSphericalOrdinates(x, y, z)[1]);
        assertEqual(56.31, MathUtils.toSphericalOrdinates(x, y, z)[0]);
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

        assertEqual(x, MathUtils.toCartesianOrdinates(phi, theta, r)[0]);
        assertEqual(y, MathUtils.toCartesianOrdinates(phi, theta, r)[1]);
        assertEqual(z, MathUtils.toCartesianOrdinates(phi, theta, r)[2]);
    }

    private void assertEqual(double expected, double actual) {
        Assert.assertEquals(expected, actual, 0.01);
    }

}