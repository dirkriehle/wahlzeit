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

package org.wahlzeit.utils;

public class Version {

    /**
     *
     */
    public static final String CODE_VERSION = "3.0";

    /**
     *
     */
    public static final String DATA_VERSION = "2.0";

    /**
     *
     */
    public static int getCodeVersionAsInt() {
        return getVersionAsInt(CODE_VERSION);
    }

    /**
     *
     */
    public static int getDataVersionAsInt() {
        return getVersionAsInt(DATA_VERSION);
    }

    /**
     *
     */
    public static int getVersionAsInt(String version) {
        int majorNumber = getMajorNumberAsInt(version);
        int minorNumber = getMinorNumberAsInt(version);
        int revisionNumber = getRevisionNumberAsInt(version);
        return majorNumber * 1000000 + minorNumber * 1000 + revisionNumber;
    }

    /**
     *
     */
    public static int getMajorNumberAsInt(String version) {
        int endIndex = version.indexOf('.');
        String number = version.substring(0, endIndex);
        return Integer.valueOf(number);
    }

    /**
     *
     */
    public static int getMinorNumberAsInt(String version) {
        int startIndex = version.indexOf('.') + 1;
        int endIndex = version.indexOf('.', startIndex);
        String number = version.substring(startIndex, endIndex);
        return Integer.valueOf(number);
    }

    /**
     *
     */
    public static int getRevisionNumberAsInt(String version) {
        int startIndex = version.indexOf('.') + 1;
        startIndex = version.indexOf('.', startIndex) + 1;
        String number = version.substring(startIndex);
        return Integer.valueOf(number);
    }

}
