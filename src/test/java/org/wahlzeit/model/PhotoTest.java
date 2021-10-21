package org.wahlzeit.model;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class PhotoTest {

    @Test
    public void testGetLocation() {
        final Photo photo = new Photo();
        assertTrue(photo.getLocation().isEmpty());
    }

    @Test
    public void testSetLocation() {
        final Photo photo = new Photo();
        photo.setLocation(new Location(0.1, 0.2, 0.3));
        assertTrue(photo.getLocation().isPresent());
        assertEquals(photo.getLocation(), Optional.of(new Location(0.1, 0.2, 0.3)));
        assertEquals(photo.getLocation().get(), new Location(0.1, 0.2, 0.3));
    }

}
