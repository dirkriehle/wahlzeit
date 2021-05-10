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

package org.wahlzeit_revisited.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A set of utility functions for basic string manipulations.
 */
public class StringUtil {

    /**
     *
     */
    public static boolean isLegalUserName(String s) {
        return isSafeString(s) && !s.equals("");
    }

    /**
     *
     */
    public static boolean isLegalPassword(String s) {
        return isSafeString(s) && !s.equals("");
    }

    /**
     *
     */
    public static boolean isValidStrictEmailAddress(String s) {
        int a = s.indexOf('@');
        int d = s.lastIndexOf('.');
        return isSafeIncluding(s, "_-+@.") && (a > 0) && (a < (d - 2)) && (d < (s.length() - 2));
    }

    /**
     *
     */
    public static boolean isValidLocalEmailAddress(String s) {
        int a = s.indexOf('@');
        int d = s.lastIndexOf('@');
        return isSafeIncluding(s, "_-+@.") && (a > 0) && (a == d);
    }

    /**
     *
     */
    public static boolean isLegalCharacterName(String s) {
        return isSafeString(s);
    }

    /**
     *
     */
    public static boolean isLegalSeriesName(String s) {
        return isSafeString(s);
    }

    /**
     *
     */
    public static boolean isLegalTagsString(String s) {
        return isSafeString(s);
    }

    /**
     *
     */
    public static boolean isValidURL(String s) {
        try {
            return new URL(s) != null;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    /**
     *
     */
    public static boolean isNullOrEmptyString(Object s) {
        return (s == null) || s.equals("");
    }

    /**
     *
     */
    public static boolean isSafeString(String s) {
        return isSafeWebString(s); // & isSafeQueryArg(s);
    }

    /**
     *
     */
    public static boolean isSafeWebString(String s) {
        return isSafeExcluding(s, "`'&<>;\"\\");
    }

    /**
     *
     */
    public static boolean isSafeQueryArg(String s) {
        return isSafeWebString(s);
    }

    /**
     * @methodtype boolean-query
     */
    protected static boolean isSafeIncluding(String s, String l) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && (l.indexOf(c) == -1)) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     */
    protected static boolean isSafeExcluding(String s, String l) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && (l.indexOf(c) != -1)) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     */
    public static String maskChar(String s, char c) {
        StringBuilder result = new StringBuilder(s.length() + 4);
        for (int i = 0; i < s.length(); i++) {
            char v = s.charAt(i);
            if (v == c) {
                result.append('\\');
            }
            result.append(v);
        }

        return result.toString();
    }

    /**
     *
     */
    public static String asFourDigits(long id) {
        if (id < 10) {
            return "000" + id;
        } else if (id < 100) {
            return "00" + id;
        } else if (id < 1000) {
            return "0" + id;
        } else {
            return "" + id;
        }
    }

    /**
     *
     */
    public static String asThreeDigits(long id) {
        if (id < 10) {
            return "00" + id;
        } else if (id < 100) {
            return "0" + id;
        } else {
            return "" + id;
        }
    }

    /**
     *
     */
    public static String asStringInSeconds(long duration) {
        long seconds = duration / 1000;
        long milliSeconds = duration - (seconds * 1000);
        return seconds + "." + asThreeDigits(milliSeconds);
    }

    /**
     * The string, which separates path segments in a URL
     */
    private static final String URL_SEPARATOR = "/";

    /**
     * Convert separators in a filesystem path to URL separators.
     * It does not escape the URL characters.
     * Use java.net.URLEncoder for this.
     *
     * @param path
     * @return
     * @fixme Review for performance
     */
    public static String pathAsUrlString(String path) {
        if (!File.separator.equals(URL_SEPARATOR)) {
            // We are not on a platform where file separator matches the url separator,
            // we need to convert between them.
            path = path.replaceAll(Pattern.quote(File.separator), Matcher.quoteReplacement(URL_SEPARATOR));
        }
        return path;
    }

    /**
     *
     */
    public static URL asUrl(String s) throws IllegalArgumentException {
        try {
            return new URL(s);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("invalid URL string");
        }
    }

    /**
     *
     */
    public static URL asUrlOrDefault(String s, URL defval) {
        try {
            return new URL(s);
        } catch (MalformedURLException ex) {
            return defval;
        }
    }

}
