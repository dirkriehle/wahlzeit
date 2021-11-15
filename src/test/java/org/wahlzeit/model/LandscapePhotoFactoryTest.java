package org.wahlzeit.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

    public class LandscapePhotoFactoryTest {

    /**
     * 
     */
    @Test
    public void testGetInstance() {
        LandscapePhotoFactory photoFactory = LandscapePhotoFactory.getInstance();
        assertNotNull(photoFactory);
        assert(photoFactory instanceof LandscapePhotoFactory);
        assert(photoFactory instanceof PhotoFactory);
    }
    /**
     * 
     */
    @Test
    public void testCreatePhoto() {
        LandscapePhoto landscapePhoto = new LandscapePhoto();
        assert(landscapePhoto instanceof LandscapePhoto);
        assert(landscapePhoto instanceof Photo);
    }
}
