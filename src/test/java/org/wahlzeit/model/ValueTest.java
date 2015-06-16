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

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author dirkriehle
 */
public class ValueTest {

    /**
     *
     */
    @Test
    public void testUserStatus() {
        assertTrue(UserStatus.CREATED == UserStatus.getFromInt(0));
        assertTrue(UserStatus.CONFIRMED == UserStatus.getFromInt(1));
        assertTrue(UserStatus.DISABLED == UserStatus.getFromInt(2));

        UserStatus us = UserStatus.CREATED;
        assertTrue(us.asInt() == 0);
        assertTrue(!us.isConfirmed());

        UserStatus us2 = us.asConfirmed();
        assertTrue(us != us2);
        assertTrue(us2.isConfirmed());

        UserStatus us3 = us2.asDisabled();
        assertTrue(us2 != us3);
        assertTrue(us3.isCreated());
        assertTrue(us3.isConfirmed());
        assertTrue(us3.isDisabled());

        UserStatus us4 = us.asDisabled();
        assertTrue(us4.isDisabled());
        assertTrue(us3 != us4);

        us3 = us3.asEnabled();
        assertTrue(us3.isConfirmed());
        assertTrue(!us3.isDisabled());

        us4 = us4.asEnabled();
        assertTrue(!us4.isConfirmed());
        assertTrue(!us4.isDisabled());

        us4 = us4.asConfirmed();
        assertTrue(us3 == us4);
    }

    /**
     *
     */
    @Test
    public void testPhotoStatus() {
        assert (PhotoStatus.VISIBLE == PhotoStatus.getFromInt(0));
        assert (PhotoStatus.INVISIBLE == PhotoStatus.getFromInt(1));
        assert (PhotoStatus.FLAGGED2 == PhotoStatus.getFromInt(3));
        assert (PhotoStatus.MODERATED == PhotoStatus.getFromInt(4));
        assert (PhotoStatus.MODERATED3 == PhotoStatus.getFromInt(6));

        PhotoStatus ps = PhotoStatus.VISIBLE;
        assert (ps.asInt() == 0);

        PhotoStatus ps2 = ps.asInvisible(true);
        assert (ps != ps2);
        assert (ps2.isInvisible());
        assert (!ps2.isFlagged());
        assert (!ps2.isModerated());
        assert (!ps2.isDisplayable());

        PhotoStatus ps3 = ps2.asFlagged(true);
        assert (ps2 != ps3);
        assert (ps3.isInvisible());
        assert (ps3.isFlagged());
        assert (!ps3.isModerated());
        assert (!ps3.isDisplayable());

        PhotoStatus ps3b = PhotoStatus.FLAGGED;
        assert (ps3 != ps3b);
        assert (!ps3b.isInvisible());
        assert (ps3.isFlagged());
        assert (!ps3.isModerated());
        assert (!ps3.isDisplayable());

        PhotoStatus ps2b = ps3b.asInvisible(true);
        assert (ps2b != ps3b);

        PhotoStatus ps4 = ps3.asModerated(true);
        assert (ps4.isInvisible());
        assert (ps4.isFlagged());
        assert (ps4.isModerated());
        assert (!ps4.isDisplayable());
    }

    /**
     *
     */
    @Test
    public void testObjectId() {
        PhotoId test = PhotoId.getNextId();

        int testInt = test.asInt();
        assert (test == PhotoId.getIdFromInt(testInt));

        String testString = test.asString();
        assert (test == PhotoId.getIdFromString(testString));
    }

}
