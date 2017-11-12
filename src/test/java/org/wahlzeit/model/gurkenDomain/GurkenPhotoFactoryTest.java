/*
 *  Copyright
 *
 *  Classname: GurkenPhotoFactoryTest
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
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoId;

/**
 * All test cases of the class {@link GurkenPhotoFactory}.
 */
public class GurkenPhotoFactoryTest extends GurkenDomainTest {

    GurkenPhotoFactory gurkenPhotoFactory;
    PhotoId photoID_one;

    @Before

    public void setUp() {
        gurkenPhotoFactory = GurkenPhotoFactory.getInstance();
        photoID_one = PhotoId.getNextId();

    }

    @Test
    public void getInstanceOfDomainPhotoFactory_notNull() {
        Assert.assertNotNull(gurkenPhotoFactory);
    }

    @Test
    public void gurkenPhotoFactory_isSubclassOfPhotoFactory_true() {
        Assert.assertTrue(PhotoFactory.class.isAssignableFrom(GurkenPhotoFactory.class));
    }

    @Test
    public void createGurkenPhoto_withoutID_notNull_isGurkenPhoto() {
        Photo gurkenPhoto = gurkenPhotoFactory.createPhoto();
        Assert.assertNotNull(gurkenPhoto);
        Assert.assertTrue(gurkenPhoto instanceof GurkenPhoto);
    }

    @Test(expected = Exception.class)
    public void createGurkenPhoto_withNullParameter_shouldThrowException() {
        gurkenPhotoFactory.createPhoto(null);
    }

    @Test
    public void createGurkenPhoto_withValidID_returnsGurkenPhoto() {
        Photo gurkenPhoto = gurkenPhotoFactory.createPhoto(photoID_one);
        Assert.assertTrue(gurkenPhoto instanceof GurkenPhoto);
    }

    @Test
    public void createGurkenPhoto_withIDone_returnsGurkenPhotoWithIDOne() {
        GurkenPhoto gurkenPhoto = (GurkenPhoto) gurkenPhotoFactory.createPhoto(photoID_one);
        Assert.assertEquals(photoID_one, gurkenPhoto.getId());
        Assert.assertTrue(photoID_one.equals(gurkenPhoto.getId()));
    }

    @Test
    public void createGurkenPhoto_fullySpecified_notNullAsExpected() {
        GurkenPhotoTest photoTest = new GurkenPhotoTest();
        photoTest.setUp();

        GurkenPhoto gurkenPhoto = gurkenPhotoFactory.createGurkenPhoto(photoID_one, photoTest.cucumberType, photoTest.avgSizeInMillimeter, Taste.TASTELESS, photoTest.noWhere);
        photoTest.assertAttribuesAreAsExpected(gurkenPhoto);
        Assert.assertEquals(photoID_one.asString(), gurkenPhoto.getId().asString());
    }
}
