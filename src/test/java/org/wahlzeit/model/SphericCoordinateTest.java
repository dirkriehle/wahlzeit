package org.wahlzeit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SphericCoordinateTest {

    @Test
    public void testSphericCoordinate() {
        double phi = 1.1;
        double theta = 3;
        double radius = 2.2;
        SphericCoordinate c = new SphericCoordinate(phi, theta, radius);
        assertEquals(phi, c.getPhi(), 0);
        assertEquals(theta, c.getTheta(), 0);
        assertEquals(radius, c.getRadius(), 0);
    }

    @Test
    public void testGetCartesianDistance() {
        SphericCoordinate c1 = new SphericCoordinate(1.3, -2.1, 0.5);
        SphericCoordinate c2 = new SphericCoordinate(1.3, -2.1, 0.5);
        SphericCoordinate c3 = new SphericCoordinate(1.3, -2.1, 1.5);
        assertEquals(0, c1.getCartesianDistance(c2), 0);
        assertEquals(1, c1.getCartesianDistance(c3), 0.0001);
    }
    
    @Test
    public void testIsEqual() {
        SphericCoordinate c1 = new SphericCoordinate(1, 2, 3);
        SphericCoordinate c2 = new SphericCoordinate(1, 2, 3);
        SphericCoordinate c3 = new SphericCoordinate(-3, 5, 3);
        SphericCoordinate c4 = new SphericCoordinate(1, 3, 0);
        CartesianCoordinate c5 = new CartesianCoordinate(0, 0, 0);
        SphericCoordinate c6 = null;
        assertTrue(c1.isEqual(c1));
        assertTrue(c1.isEqual(c2));
        assertFalse(c1.isEqual(c3));
        assertTrue(c4.isEqual(c5));
        assertFalse(c2.isEqual(c5));
        assertFalse(c1.isEqual(c6));
    }

    @Test
    public void testEquals() {
        SphericCoordinate c1 = new SphericCoordinate(2, 1, 0.3);
        SphericCoordinate c2 = new SphericCoordinate(2, 1, 0.3);
        SphericCoordinate c3 = new SphericCoordinate(-3.1, 5.2, 3);
        SphericCoordinate c4 = new SphericCoordinate(1, 3, 0);
        CartesianCoordinate c5 = new CartesianCoordinate(0, 0, 0);
        Coordinate c6 = null;
        String s1 = "test";
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(s1));
        assertTrue(c4.equals(c5));
        assertFalse(c1.equals(c6));
    }

    @Test
    public void testGetCentralAngle() {
        SphericCoordinate c1 = new SphericCoordinate(Math.PI, Math.PI, 1);
        SphericCoordinate c2 = new SphericCoordinate(2 * Math.PI, Math.PI, 1);
        CartesianCoordinate c3 = new CartesianCoordinate(0, 0, 1);
        SphericCoordinate c4 = new SphericCoordinate(Math.PI, 0, 1);
        assertEquals(Math.PI, c1.getCentralAngle(c2), 0.0001);
        assertEquals(1.5708, c4.getCentralAngle(c3), 0.0001);
    }

    @Test
    public void testAsCartesianCoordinate() {
        SphericCoordinate c1 = new SphericCoordinate(1, 1, 1);
        CartesianCoordinate c2 = c1.asCartesianCoordinate();
        assertEquals(0.45465, c2.getX(), 0.0001);
        assertEquals(0.7081, c2.getY(), 0.0001);
        assertEquals(0.5403, c2.getZ(), 0.0001);
    }
}
