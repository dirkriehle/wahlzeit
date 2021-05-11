/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit_revisited.model;

/**
 * The gender denotes some user's/person's/character's/whatever gender. The undefined value denotes that no value was
 * provided or the entity is not human.
 */
public enum Gender {

    /**
     * UNDEFINED = user never entered anything
     */
    UNDEFINED(0), MALE(1), FEMALE(2), OTHER(3);

    /**
     *
     */
    private static final Gender[] ALL_VALUES = {
            UNDEFINED, MALE, FEMALE, OTHER
    };

    /**
     * @methodtype conversion
     */
    public static Gender getFromInt(int myValue) throws IllegalArgumentException {
        assertIsValidGenderAsInt(myValue);
        return ALL_VALUES[myValue];
    }

    /**
     *
     */
    protected static void assertIsValidGenderAsInt(int myValue) throws IllegalArgumentException {
        if ((myValue < 0) || (myValue > 3)) {
            throw new IllegalArgumentException("invalid Gender int: " + myValue);
        }
    }

    /**
     *
     */
    private static final String[] VALUE_NAMES = {
            "undefined", "male", "female", "other"
    };

    /**
     * @methodtype conversion
     */
    public static Gender getFromString(String myGender) throws IllegalArgumentException {
        for (Gender gender : Gender.values()) {
            if (VALUE_NAMES[gender.asInt()].equals(myGender)) {
                return gender;
            }
        }

        throw new IllegalArgumentException("invalid Gender string: " + myGender);
    }

    /**
     *
     */
    private final int value;

    /**
     *
     */
    Gender(int myValue) {
        value = myValue;
    }

    /**
     *
     */
    public int asInt() {
        return value;
    }

    /**
     *
     */
    public String asString() {
        return VALUE_NAMES[value];
    }

    /**
     *
     */
    public Gender[] getAllValues() {
        return ALL_VALUES;
    }

    /**
     *
     */
    public String getTypeName() {
        return "Gender";
    }

    /**
     *
     */
    public boolean isUndefined() {
        return (this == UNDEFINED);
    }

    /**
     *
     */
    public boolean isMale() {
        return (this == MALE);
    }

    /**
     *
     */
    public boolean isFemale() {
        return (this == FEMALE);
    }

    /**
     *
     */
    public boolean isOther() {
        return (this == OTHER);
    }

}
