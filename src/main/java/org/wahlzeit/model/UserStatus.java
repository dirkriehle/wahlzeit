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

package org.wahlzeit.model;

import org.wahlzeit.utils.EnumValue;

/**
 * The UserStatus of a User denotes its state within the system.
 * A user (account) may have been created, confirmed, or disabled.
 * These states are not mutually exclusive.
 *
 * @author dirkriehle
 */
public enum UserStatus implements EnumValue {

    /**
     *
     */
    CREATED(0), // no bit set
    CONFIRMED(1), // only confirmed bit set
    DISABLED(2), DISABLED2(3); // disabled without or with confirmed bit set

    /**
     *
     */
    public static final int CONFIRMED_BIT = 0;
    public static final int DISABLED_BIT = 1;

    /**
     * All possible states of UserStatus
     */
    private static UserStatus[] allValues = {
            CREATED, CONFIRMED, DISABLED, DISABLED2
    };

    /**
     * @methodtype conversion
     */
    public static UserStatus getFromInt(int myValue) throws IllegalArgumentException {
        assertIsValidUserStatusAsInt(myValue);
        return allValues[myValue];
    }

    /**
     * @methodtype conversion
     */
    protected static void assertIsValidUserStatusAsInt(int myValue) throws IllegalArgumentException {
        if ((myValue < 0) || (myValue > 3)) {
            throw new IllegalArgumentException("invalid UserStatus int: " + myValue);
        }
    }

    /**
     *
     */
    private static String[] valueNames = {
            "created", "confirmed", "disabled", "disabled"
    };

    /**
     *
     */
    public static UserStatus getFromString(String aStatus) throws IllegalArgumentException {
        for (UserStatus status : UserStatus.values()) {
            if (valueNames[status.asInt()].equals(aStatus)) {
                return status;
            }
        }

        throw new IllegalArgumentException("invalid UserStatus string: " + aStatus);
    }

    /**
     *
     */
    private int value = 0;

    /**
     *
     */
    private UserStatus(int newValue) {
        value = newValue;
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
        return valueNames[value];
    }

    /**
     *
     */
    public UserStatus[] getAllValues() {
        return allValues;
    }

    /**
     *
     */
    public String getTypeName() {
        return "UserStatus";
    }

    /**
     *
     */
    public boolean isCreated() {
        return true;
    }

    /**
     *
     */
    public boolean isConfirmed() {
        return (value & (1 << CONFIRMED_BIT)) != 0;
    }

    /**
     *
     */
    public UserStatus asConfirmed() {
        return getFromInt(value | (1 << CONFIRMED_BIT));
    }

    /**
     *
     */
    public boolean isDisabled() {
        return (value & (1 << DISABLED_BIT)) != 0;
    }

    /**
     *
     */
    public UserStatus asDisabled() {
        return getFromInt(value | (1 << DISABLED_BIT));
    }

    /**
     *
     */
    public UserStatus asEnabled() {
        return getFromInt(value & (1 << CONFIRMED_BIT));
    }

}
