/*
 *  Copyright
 *
 *  Classname: MathUtils
 *  Author: Tango1266
 *  Version: 17.11.17 14:31
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

import com.google.common.math.DoubleMath;

public class MathUtils {
    static final double DEFAULT_PRECISION = 1.0E-8;
    private static double precision = DEFAULT_PRECISION;

    public static boolean doublesAreNotEqual(double valuaA, double valueB) {
        return !doublesAreEqual(valuaA, valueB);
    }

    public static boolean doublesAreEqual(double valuaA, double valueB) {
        return doublesAreEqual(valuaA, valueB, getPrecision());
    }

    public static boolean doublesAreEqual(double valuaA, double valueB, double precision) {
        return DoubleMath.fuzzyEquals(valuaA, valueB, precision);
    }

    /**
     * @return if not other defined, the default precision of 1.0E-10 will be returned
     */
    public static double getPrecision() {
        return precision;
    }

    public static void setPrecision(double desiredDelta) {
        precision = desiredDelta;
    }

    public static double square(double value) {
        return Math.pow(value, 2);
    }

    public static double sqrtOfSum(double... doubles) {
        double sum = 0;
        for (double aDouble : doubles) {
            sum += aDouble;
        }
        return Math.sqrt(sum);
    }

}
