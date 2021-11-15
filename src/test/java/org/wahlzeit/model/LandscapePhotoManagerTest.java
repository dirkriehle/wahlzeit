package org.wahlzeit.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LandscapePhotoManagerTest {
    
    /**
     * 
     */
    @Test
    public void testInstance() {
        assertNotNull(LandscapePhotoManager.instance);
    }

    /**
     * 
     */
    @Test
    public void testLandscapePhotoManager() {
        LandscapePhotoManager photoManager = new LandscapePhotoManager();
        assertNotNull(photoManager.photoTagCollector);
    }

    /**
     * 
     */
    @Test
    public void testGetInstance() {
        PhotoManager photoManager = LandscapePhotoManager.getInstance();
        assert(photoManager instanceof LandscapePhotoManager);
    }
}
