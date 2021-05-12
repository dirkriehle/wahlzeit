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

import static org.junit.Assert.*;

/**
 * Test cases for a variety of value object classes.
 */
public class ValueTest {

    /**
     *
     */
    @Test
    public void testUserStatus() {
        assertSame(UserStatus.CREATED, UserStatus.getFromInt(0));
        assertSame(UserStatus.CONFIRMED, UserStatus.getFromInt(1));
        assertSame(UserStatus.DISABLED, UserStatus.getFromInt(2));

        UserStatus us = UserStatus.CREATED;
        assertEquals(0, us.asInt());
        assertFalse(us.isConfirmed());

        UserStatus us2 = us.asConfirmed();
        assertNotSame(us, us2);
        assertTrue(us2.isConfirmed());

        UserStatus us3 = us2.asDisabled();
        assertNotSame(us2, us3);
        assertTrue(us3.isCreated());
        assertTrue(us3.isConfirmed());
        assertTrue(us3.isDisabled());

        UserStatus us4 = us.asDisabled();
        assertTrue(us4.isDisabled());
        assertNotSame(us3, us4);

        us3 = us3.asEnabled();
        assertTrue(us3.isConfirmed());
        assertFalse(us3.isDisabled());

        us4 = us4.asEnabled();
        assertFalse(us4.isConfirmed());
        assertFalse(us4.isDisabled());

        us4 = us4.asConfirmed();
        assertSame(us3, us4);
    }

    /**
     *
     */
    @Test
    public void testPhotoStatus() {
        assertSame(PhotoStatus.VISIBLE, PhotoStatus.getFromInt(0));
        assertSame(PhotoStatus.INVISIBLE, PhotoStatus.getFromInt(1));
        assertSame(PhotoStatus.FLAGGED, PhotoStatus.getFromInt(2));
        assertSame(PhotoStatus.MODERATED, PhotoStatus.getFromInt(4));

        PhotoStatus ps = PhotoStatus.VISIBLE;
        assertEquals(0, ps.asInt());

        PhotoStatus ps2 = ps.asInvisible(true);
        assertNotSame(ps, ps2);
        assertTrue(ps2.isInvisible());
        assertFalse(ps2.isFlagged());
        assertFalse(ps2.isModerated());
        assertFalse(ps2.isDisplayable());

        PhotoStatus ps3 = ps2.asFlagged(true);
        assertNotSame(ps2, ps3);
        assertTrue(ps3.isInvisible());
        assertTrue(ps3.isFlagged());
        assertFalse(ps3.isModerated());
        assertFalse(ps3.isDisplayable());

        PhotoStatus ps3b = PhotoStatus.FLAGGED;
        assertNotSame(ps3, ps3b);
        assertFalse(ps3b.isInvisible());
        assertTrue(ps3.isFlagged());
        assertFalse(ps3.isModerated());
        assertFalse(ps3.isDisplayable());

        PhotoStatus ps2b = ps3b.asInvisible(true);
        assertNotSame(ps2b, ps3b);

        PhotoStatus ps4 = ps3.asModerated(true);
        assertTrue(ps4.isInvisible());
        assertTrue(ps4.isFlagged());
        assertTrue(ps4.isModerated());
        assertFalse(ps4.isDisplayable());
    }

}
