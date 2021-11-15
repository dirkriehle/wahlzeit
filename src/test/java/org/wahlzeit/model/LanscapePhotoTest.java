package org.wahlzeit.model;

import org.junit.Test;

public class LanscapePhotoTest {
    
    /**
     * 
     */
    @Test
    public void testLandscapePhoto() {
        LandscapePhoto landscapePhoto = new LandscapePhoto();
        assert(landscapePhoto instanceof LandscapePhoto);
        assert(landscapePhoto instanceof Photo);
    }
}
