package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTest {

    @Test
    public void testGetX() {
        final Coordinate coordinate = new Coordinate(0.1, 1.1, 2.1);
        assertEquals(0.1, coordinate.getX(), 0.0);
    }

    @Test
    public void testGetY() {
        final Coordinate coordinate = new Coordinate(0.1, 1.1, 2.1);
        assertEquals(1.1, coordinate.getY(), 0.0);
    }

    @Test
    public void testGetZ() {
        final Coordinate coordinate = new Coordinate(0.1, 1.1, 2.1);
        assertEquals(2.1, coordinate.getZ(), 0.0);
    }
}
