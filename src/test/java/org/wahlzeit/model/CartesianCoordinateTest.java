package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CartesianCoordinateTest {

    @Test
    public void testCartesianCoordinate() {
        double x = 1.1;
        double y = 3;
        double z = 2.2;
        CartesianCoordinate c = new CartesianCoordinate(x, y, z);
        assertEquals(x, c.getX(), 0);
        assertEquals(y, c.getY(), 0);
        assertEquals(z, c.getZ(), 0);
    }

    @Test
    public void testGetCartesianDistance() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate c2 = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate c3 = new CartesianCoordinate(-3, 5, 3);
        CartesianCoordinate c4 = new CartesianCoordinate(-3, 5, -4);
        assertEquals(0, c1.getCartesianDistance(c2), 0);
        assertEquals(5, c1.getCartesianDistance(c3), 0);
        assertEquals(7, c3.getCartesianDistance(c4), 0);
    }
    
    @Test
    public void testIsEqual() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate c2 = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate c3 = new CartesianCoordinate(-3, 5, 3);
        CartesianCoordinate c4 = null;
        assertTrue(c1.isEqual(c1));
        assertTrue(c1.isEqual(c2));
        assertFalse(c1.isEqual(c3));
        assertFalse(c1.isEqual(c4));
    }

    @Test
    public void testEquals() {
        CartesianCoordinate c1 = new CartesianCoordinate(2, 1, 0.3);
        CartesianCoordinate c2 = new CartesianCoordinate(2, 1, 0.3);
        CartesianCoordinate c3 = new CartesianCoordinate(-3.1, 5.2, 3);
        CartesianCoordinate c4 = null;
        String s1 = "test";
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(c4));
        assertFalse(c1.equals(s1));
    }

    @Test
    public void testAsSphericCoordinate() {
        CartesianCoordinate c1 = new CartesianCoordinate(1, 1, 1);
        SphericCoordinate c2 = c1.asSphericCoordinate();
        assertEquals(0.7854, c2.getPhi(), 0.0001);
        assertEquals(0.9553, c2.getTheta(), 0.0001);
        assertEquals(1.732, c2.getRadius(), 0.0001);
    }
}
