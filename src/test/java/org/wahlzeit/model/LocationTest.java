package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    
    @Test
    public void testLocation() {
        CartesianCoordinate CartesianCoordinate = new CartesianCoordinate(1.1, 2.4, 5.3);
        Location location = new Location(CartesianCoordinate);
        assertEquals(CartesianCoordinate, location.getCoordinate());
    }
}
