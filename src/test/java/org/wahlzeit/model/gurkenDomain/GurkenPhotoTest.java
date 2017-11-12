/*
 *  Copyright
 *
 *  Classname: GurkenPhotoTest
 *  Author: Tango1266
 *  Version: 13.11.17 00:16
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.model.Location;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoId;

/**
 * All test cases of the class {@link GurkenPhoto}.
 */
public class GurkenPhotoTest extends GurkenDomainTest {

    public int avgSizeInMillimeter;
    public Location noWhere;
    public String cucumberType;

    @Before
    public void setUp() {
        noWhere = new Location();
        avgSizeInMillimeter = 120;
        cucumberType = "cucumber";
    }

    @Test
    public void createGurkenPhoto_inEmptyConstructor_notNull() {
        GurkenPhoto gurkenPhoto = new GurkenPhoto();
        Assert.assertNotNull(gurkenPhoto);
    }

    @Test
    public void createGurkenPhoto_fullyDefined_attributesNotNull() {
        GurkenPhoto gurkenPhoto = new GurkenPhoto();
        gurkenPhoto.setLocation(noWhere);
        gurkenPhoto.setType(cucumberType);
        gurkenPhoto.setSize(avgSizeInMillimeter);
        gurkenPhoto.setTaste(Taste.TASTELESS);

        assertAttribuesAreAsExpected(gurkenPhoto);
    }

    @Test
    public void createGurkenPhoto_throughConstructucter_attributesNotNull() {
        GurkenPhoto gurkenPhoto = new GurkenPhoto(PhotoId.getNextId(), cucumberType, avgSizeInMillimeter, Taste.TASTELESS, noWhere);
        assertAttribuesAreAsExpected(gurkenPhoto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createGurkenPhoto_withNumbersAsType_ShouldThrowException() {
        new GurkenPhoto().setType("1234fu");
    }

    @Test
    public void createGurkenPhoto_withLiteralsAsType_ShouldNotThrowException() {
        try {
            new GurkenPhoto().setType("bar");
        } catch (Exception exception) {
            Assert.fail("Exception was thrown whereas no exception was expected");
        }
    }

    @Test
    public void gurkenPhoto_isSubclassOfPhoto_true() {
        Assert.assertTrue(Photo.class.isAssignableFrom(GurkenPhoto.class));
    }

    public void assertAttribuesAreAsExpected(GurkenPhoto gurkenPhoto) {
        Assert.assertNotNull(gurkenPhoto.getLocation());
        Assert.assertEquals(cucumberType, gurkenPhoto.getType());
        Assert.assertEquals(avgSizeInMillimeter, gurkenPhoto.getSize());
        Assert.assertEquals(Taste.TASTELESS, gurkenPhoto.getTaste());
    }
}