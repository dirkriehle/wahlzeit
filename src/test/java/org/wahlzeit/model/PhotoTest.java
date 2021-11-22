package org.wahlzeit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PhotoTest {

    @Test
    public void testPhoto() {
        CartesianCoordinate CartesianCoordinate1 = new CartesianCoordinate(0.5, 7.8, 1.9);
        CartesianCoordinate CartesianCoordinate2 = new CartesianCoordinate(0.5, 3, 7.2);
        Location location1 = new Location(CartesianCoordinate1);
        Location location2 = new Location(CartesianCoordinate2);
        Photo photo1 = new Photo(location1);
        Photo photo2 = new Photo(location2);
        assertNotEquals(photo1.getId(), photo2.getId());
        assertEquals(location1, photo1.getLocation());
        assertEquals(location2, photo2.getLocation());
        assertNotEquals(photo1.getLocation(), photo2.getLocation());
    }

    @Test
    public void testsetLocation() {
        Photo photo = new Photo();
        Location location = new Location(new CartesianCoordinate(1, 1, 1));
        assertNull(photo.getLocation());
        photo.setLocation(location);
        assertEquals(location, photo.getLocation());
    }
}
