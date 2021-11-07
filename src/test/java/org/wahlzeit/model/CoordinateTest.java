package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoordinateTest {

    @Test
    public void testCoordinate() {
        double x = 1.1;
        double y = 3;
        double z = 2.2;
        Coordinate c = new Coordinate(x, y, z);
        assertEquals(x, c.getX(), 0);
        assertEquals(y, c.getY(), 0);
        assertEquals(z, c.getZ(), 0);
    }

    @Test
    public void testGetDistance() {
        Coordinate c1 = new Coordinate(1, 2, 3);
        Coordinate c2 = new Coordinate(1, 2, 3);
        Coordinate c3 = new Coordinate(-3, 5, 3);
        Coordinate c4 = new Coordinate(-3, 5, -4);
        assertEquals(0, c1.getDistance(c2), 0);
        assertEquals(5, c1.getDistance(c3), 0);
        assertEquals(7, c3.getDistance(c4), 0);
    }
    
    @Test
    public void testIsEqual() {
        Coordinate c1 = new Coordinate(1, 2, 3);
        Coordinate c2 = new Coordinate(1, 2, 3);
        Coordinate c3 = new Coordinate(-3, 5, 3);
        Coordinate c4 = null;
        assertTrue(c1.isEqual(c1));
        assertTrue(c1.isEqual(c2));
        assertFalse(c1.isEqual(c3));
        assertFalse(c1.isEqual(c4));
    }

    @Test
    public void testEquals() {
        Coordinate c1 = new Coordinate(2, 1, 0.3);
        Coordinate c2 = new Coordinate(2, 1, 0.3);
        Coordinate c3 = new Coordinate(-3.1, 5.2, 3);
        Coordinate c4 = null;
        String s1 = "test";
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(c4));
        assertFalse(c1.equals(s1));
    }
}
