package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    
    @Test
    public void testLocation() {
        Coordinate coordinate = new Coordinate(1.1, 2.4, 5.3);
        Location location = new Location(coordinate);
        assertEquals(coordinate, location.getCoordinate());
    }
}
