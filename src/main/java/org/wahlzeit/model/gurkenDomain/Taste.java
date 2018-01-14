/*
 *  Copyright
 *
 *  Classname: Taste
 *  Author: Tango1266
 *  Version: 13.11.17 00:20
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

package org.wahlzeit.model.gurkenDomain;

import org.wahlzeit.utils.EnumValue;

/**
 * Enum of tastes e.g. salty
 */
public enum Taste implements EnumValue {

    /**
     *
     */
    TASTELESS(0),
    SOUR(1),
    SPICY(2),
    SWEET(3),
    MILD(4),
    SALTY(5),
    UNSPECIFIED(6);

    private static Taste[] allValues = {
            TASTELESS,
            SALTY,
            SOUR,
            SPICY,
            SWEET,
            MILD,
            UNSPECIFIED
    };
    private static String[] valueNames = {
            "tasteless",
            "sour",
            "spicy",
            "sweet",
            "mild",
            "salty",
            "unspecified"
    };
    private int value;

    Taste(int value) {
        this.value = value;
    }

    @Override
    public int asInt() {
        return value;
    }

    @Override
    public String asString() {
        return valueNames[value];
    }

    @Override
    public EnumValue[] getAllValues() {
        return allValues;
    }

    @Override
    public String getTypeName() {
        return getClass().getName();
    }
}
