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

import org.wahlzeit.utils.StringUtil;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A Tags instance represents a set of tags; each tag ist just a string.
 * All tags are maintained lowercase and without whitespace and ','.
 * For example, "Captain America" turns into "captainamerica,".
 */
public class Tags {

    /**
     *
     */
    public static final char SEPARATOR_CHAR = ',';

    /**
     *
     */
    public static final int MAX_NO_TAGS = 32;

    /**
     *
     */
    public static final int MIN_TAG_LEN = 3;

    /**
     *
     */
    public static final Tags EMPTY_TAGS = new Tags();

    /**
     *
     */
    protected Set<String> tags = new TreeSet<>();

    /**
     *
     */
    public Tags() {

    }

    /**
     *
     */

    public Tags(Set<String> myTags) {
        this.tags = asEscapedTagSet(myTags);
    }

    public Tags(String myTags) {
        this.tags = asTagSetFromString(myTags);
    }

    /**
     * @methodtype boolean-query
     */
    @Override
    public int hashCode() {
        return (tags == null) ? super.hashCode() : tags.hashCode();
    }

    /**
     *
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Tags)) return false;

        Tags other = (Tags) obj;
        return isEqual(other);
    }

    /**
     *
     */
    public boolean isEqual(Tags other) {
        if (tags == null) {
            return other.tags == null;
        }

        return tags.equals(other.tags);
    }

    /**
     *
     */
    public boolean hasTag(String tag) {
        return (null != tag) && tags.contains(tag);
    }

    /**
     * @methodtype get
     */
    public int getSize() {
        return tags.size();
    }

    /**
     * @methodtype get
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * @methodtype conversion
     */
    public String asString() {
        return asString(false, SEPARATOR_CHAR);
    }

    /**
     *
     */
    public String asString(boolean lead, char sep) {
        // Get's transformed into "tag1, tag2, tag3,"
        StringBuilder result = new StringBuilder();
        String seps = (lead ? " " : "") + sep + " ";
        for (String tag : tags) {
            result.append(tag);
            result.append(seps);
        }
        return result.toString();
    }

    /**
     * @methodtype conversion
     */
    public String[] asArray() {
        return tags.toArray(String[]::new);
    }


    /**
     * @methodtype conversion
     * @methodproperties convenience, class
     */
    public static Set<String> asTagSetFromString(String tags) {
        return asTagSetFromString(tags, SEPARATOR_CHAR);
    }

    /**
     * @methodtype conversion
     * @methodproperties class
     */
    public static Set<String> asTagSetFromString(String tags, char separator) {
        Set<String> result = new TreeSet<>();

        if (tags != null) {
            int i = 0;
            int j = 0;
            for (; i < tags.length(); i = j) {
                while (((i < tags.length()) && (tags.charAt(i) == separator))) {
                    i++;
                }

                for (j = i; ((j < tags.length()) && (tags.charAt(j) != separator)); ) {
                    j++;
                }

                if (i != j) {
                    String tag = asTag(tags.substring(i, j));
                    if (!StringUtil.isNullOrEmptyString(tag)) {
                        result.add(tag);
                    }
                }
            }
        }

        return result;
    }

    /**
     * @methodtype conversion
     * @methodproperties class
     */
    public static Set<String> asEscapedTagSet(Set<String> tagSet) {
        return tagSet.stream()
                .filter(s -> s.length() >= MIN_TAG_LEN) // Filter invalid tags
                .map(Tags::asTag) // Normalize and escape
                .collect(Collectors.toSet());
    }

    /**
     *
     */
    public static String asTag(String n) {
        StringBuilder result = new StringBuilder(n.length());

        for (int i = 0; i < n.length(); i++) {
            char c = n.charAt(i);
            if (Character.isLetter(c)) {
                result.append(Character.toLowerCase(c));
            } else if (Character.isDigit(c)) {
                result.append(c);
            }
        }

        return result.toString();
    }

}
