package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * All the test cases of the class Coordinate
 */
public class CoordinateTest {

    @Test
    public void getDistance_Returns_CorrectValue(){
        Coordinate coordinate1 = new Coordinate(1, 1, 1);
        Coordinate coordinate2 = new Coordinate(2, 2, 2);
        assertEquals(coordinate1.getDistance(coordinate2), Math.sqrt(3), 5);
    }

    @Test
    public void isEqual_samePoints_returnsTrue(){
        Coordinate coordinate = new Coordinate(1, 1, 1);
        assertTrue(coordinate.isEqual(coordinate));
    }

    @Test
    public void isEqual_differentPoints_returnsFalse(){
        Coordinate coordinate1 = new Coordinate(1, 1, 1);
        Coordinate coordinate2 = new Coordinate(2, 2, 2);
        assertFalse(coordinate1.isEqual(coordinate2));
    }


}
