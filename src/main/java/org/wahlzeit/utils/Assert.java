/*
 *  Copyright
 *
 *  Classname: Assert
 *  Author: Tango1266
 *  Version: 17.11.17 14:21
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

import org.wahlzeit.model.config.DomainCfg;

import java.util.regex.Pattern;

public class Assert {
    /**
     * checks whether a value is lesser then zero
     * @methodtype assert
     */
    public static void notNegative(double value) throws IllegalArgumentException {
        if (value < 0) {
            RuntimeException exception = new IllegalArgumentException("The radius must be greater then 0 but was actually " + value);
            DomainCfg.logError(Assert.class, exception);
            throw exception;
        }
    }

    /**
     * checks whether a value is lesser then |maxValue|
     * @methodtype assert
     */
    public static void inRangeMax(double value, double maxValue) throws IllegalArgumentException {
        inRangeMax(value, maxValue, "");
    }

    public static void inRangeMax(double value, double maxValue, String valueSpecification) throws IllegalArgumentException {
        if (maxValue - Math.abs(value) < -MathUtils.getPrecision()) {
            String errorMessage = "";
            if (valueSpecification.length() > 0) {
                errorMessage = String.format(valueSpecification + " exceeded the defined range. ");
            }
            errorMessage = String.format(errorMessage + "The value was \"" + value + "\" but must be in the range of [-" + maxValue + ":" + maxValue + "]");
            RuntimeException exception = new IllegalArgumentException(errorMessage);
            DomainCfg.logError(Assert.class, exception);
            throw exception;
        }
    }

    /**
     * Checks if an field points to NULL
     */
    public static void isNull(Object objectField, String objectSpecification) throws IllegalStateException {
        if (objectField != null) {
            String errorMessage = objectSpecification.length() > 0 ? String.format(objectSpecification) : "Field";
            errorMessage += " is already initialized.";
            throw new IllegalStateException(errorMessage);
        }
    }

    public static void isFalse(boolean bool, String errorMessage) {
        if (bool != false) {
            RuntimeException exception = new IllegalStateException(errorMessage);
            DomainCfg.logError(Assert.class, exception);
            throw exception;
        }
    }

    public static void notNull(Object notNullobject, String objectSpecification) throws IllegalStateException {
        String object = objectSpecification != null && objectSpecification.length() > 0 ? objectSpecification : "Object";
        if (notNullobject == null) {
            RuntimeException exception = new IllegalStateException(object + " must not be null");
            DomainCfg.logError(Assert.class, exception);
            throw exception;
        }
    }

    /**
     * checks with asserts that all double params are finite and a number
     * @param doubleValues one ore multiply double values
     */
    public static void areValidDoubles(double... doubleValues) throws IllegalArgumentException {
        String theProvidedValue = "The provided Value ";
        for (double doubleValue : doubleValues) {
            RuntimeException exception = null;
            if (Double.isInfinite(doubleValue)) {
                exception = new IllegalArgumentException(theProvidedValue + "must be smaller than " + Double.MAX_VALUE);
                DomainCfg.logError(Assert.class, exception);
                throw exception;
            }
            if (Double.isNaN(doubleValue)) {
                exception = new IllegalArgumentException(theProvidedValue + "is not a number. This may occur during an invalid operation like: a / 0");
                DomainCfg.logError(Assert.class, exception);
                throw exception;
            }
        }
    }

    public static void stringNotEmpty(String gurkenTypeName) {
        if (gurkenTypeName.isEmpty()) {
            RuntimeException exception = new IllegalArgumentException("The provided String " + "must not be empty");
            DomainCfg.logError(Assert.class, exception);
            throw exception;
        }
    }

    public static void stringStartsWithLiterals(String string, String objectSpecification) {
        String theProvided = "The provided ";
        if (Pattern.matches("^(\\d.*)", string)) {
            throw new IllegalArgumentException(theProvided + objectSpecification + " String should start with literals but is actually" + string);
        }
    }
}
