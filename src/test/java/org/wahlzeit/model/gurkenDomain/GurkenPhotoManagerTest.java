/*
 *  Copyright
 *
 *  Classname: GurkenPhotoManagerTest
 *  Author: Tango1266
 *  Version: 13.11.17 00:17
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

import org.junit.*;
import org.junit.rules.TestRule;
import org.wahlzeit.model.*;
import org.wahlzeit.model.coordinates.impl.NoWhereCoordinate;
import org.wahlzeit.testEnvironmentProvider.DependencyInjectionRule;

import java.io.IOException;

/**
 * All test cases of the class {@link GurkenPhotoManager}.
 */
public class GurkenPhotoManagerTest extends GurkenDomainTest {

    final String TYPE = "gurkenTypeHirarchy";

    PhotoManager gurkenPhotoManager;
    GurkenPhotoFactory gurkenPhotoFactory;

    Location noWhere;

    PhotoId oneID;
    PhotoId twoID;
    PhotoId threeID;
    PhotoId fourID;

    GurkenPhoto cucumberSalty100;
    GurkenPhoto cucumberSalty120;
    GurkenPhoto cucumberTasteless100;
    GurkenPhoto cucumberTasteless120;
    
    @ClassRule
    public static TestRule rule = new DependencyInjectionRule();

    @Before
    public void setUp() {
        setUpFields();
        prepairPhotoCache();
    }

    @After
    public void tearDown() {
        try {
            gurkenPhotoManager.removePhoto(cucumberSalty100);
            gurkenPhotoManager.removePhoto(cucumberSalty120);
            gurkenPhotoManager.removePhoto(cucumberTasteless100);
            gurkenPhotoManager.removePhoto(cucumberTasteless120);
        } catch (IOException ioEx) {
            Assert.fail("PhotoCache could not be initialized \n Stacktrace: \n" + ioEx.getStackTrace());
        }
    }

    @Test
    public void getPhoto_byOneID_shouldReturnCucumberSalty100() {
        Assert.assertEquals(cucumberSalty100, gurkenPhotoManager.getPhoto(oneID));
    }

    @Test
    public void getPhoto_byOneID_shouldNotReturnCucumberSalty120() {
        Assert.assertNotEquals(cucumberSalty120, gurkenPhotoManager.getPhoto(oneID));
    }

    @Test
    public void getPhoto_byTwoID_shouldReturnCucumberSalty120() {
        Assert.assertEquals(cucumberSalty120, gurkenPhotoManager.getPhoto(twoID));
    }

    @Test
    public void getPhoto_byTwoID_shouldNotReturnCucumberSalty100() {
        Assert.assertNotEquals(cucumberSalty100, gurkenPhotoManager.getPhoto(twoID));
    }

    @Test
    public void getGurkenPhoto_byThreeID_shouldReturnCucumberTasteless100() {
        Assert.assertEquals(cucumberTasteless100, gurkenPhotoManager.getPhoto(threeID));
    }

    @Test
    public void getGurkenPhoto_byThreeID_shouldNotReturnCucumberTasteless120() {
        Assert.assertNotEquals(cucumberSalty120, gurkenPhotoManager.getPhoto(threeID));
    }

    @Test
    public void getPhoto_byFourID_shouldReturnPhotoAssignableFromGurkenPhoto() {
        Assert.assertTrue(gurkenPhotoManager.getPhoto(fourID) instanceof Photo);
        Assert.assertTrue(Photo.class.isAssignableFrom(gurkenPhotoManager.getPhoto(fourID).getClass()));
    }

    @Test
    public void getGurkenPhoto_byFourID_shouldReturnGurkenPhoto() {
        Assert.assertTrue(gurkenPhotoManager.getPhoto(fourID) instanceof GurkenPhoto);
    }

    @Test
    public void removePhoto_setUpTearDownTest() {
        tearDown();
        int numberOfAddedPhotosToCache = 4;

        Assert.assertEquals(0, gurkenPhotoManager.getPhotoCache().size());
        setUp();
        Assert.assertEquals(numberOfAddedPhotosToCache, gurkenPhotoManager.getPhotoCache().size());
    }

    @Test
    public void gurkenPhotoManager_isSubclassOfPhotomanager_isTrue() {
        Assert.assertTrue(PhotoManager.class.isAssignableFrom(GurkenPhotoManager.class));
    }

    private void prepairPhotoCache() {
        try {
            gurkenPhotoManager.addPhoto(cucumberSalty100);
            gurkenPhotoManager.addPhoto(cucumberSalty120);
            gurkenPhotoManager.addPhoto(cucumberTasteless100);
            gurkenPhotoManager.addPhoto(cucumberTasteless120);
        } catch (IOException ioEx) {
            Assert.fail("PhotoCache could not be initialized \n Stacktrace: \n" + ioEx.getStackTrace());
        }
    }

    private void setUpFields() {
        gurkenPhotoManager = PhotoManager.getInstance();
        gurkenPhotoFactory = (GurkenPhotoFactory) PhotoFactory.getInstance();

        noWhere = new Location(NoWhereCoordinate.getNoWhereCoordinate());
        oneID = PhotoId.getNextId();
        twoID = PhotoId.getNextId();
        threeID = PhotoId.getNextId();
        fourID = PhotoId.getNextId();

        cucumberSalty100 = createPhoto(oneID, 100, Taste.SALTY);
        cucumberSalty120 = createPhoto(twoID, 120, Taste.SALTY);
        cucumberTasteless100 = createPhoto(threeID, 100, Taste.TASTELESS);
        cucumberTasteless120 = createPhoto(fourID, 120, Taste.TASTELESS);
    }

    private GurkenPhoto createPhoto(PhotoId id, int sizeInMillimeter, Taste taste) {
        return gurkenPhotoFactory.createGurkenPhoto(id, TYPE, sizeInMillimeter, taste, noWhere);
    }
}
