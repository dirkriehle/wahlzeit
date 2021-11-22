package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    
    @Test
    public void testLocation() {
        CartesianCoordinate cartesianCoordinate = new CartesianCoordinate(1.1, 2.4, 5.3);
        SphericCoordinate sphericCoordinate = new SphericCoordinate(1.9, 2.1, 7.3);
        Location location1 = new Location(cartesianCoordinate);
        Location location2 = new Location(sphericCoordinate);
        assertEquals(cartesianCoordinate, location1.getCoordinate());
        assertEquals(sphericCoordinate, location2.getCoordinate());
    }
}
