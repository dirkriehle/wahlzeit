package org.wahlzeit.model;

import org.junit.Assert;
import org.junit.Test;

public class NoWhereCoordinateTest extends CoordinateTest {

    @Test
    public void coordinateNoWhere_andNoWhere_areNotEqual() {
        Assert.assertFalse(noWhere.equals(new NoWhereCoordinate()));
    }

    @Test
    public void coordinateNoWhere_andNull_areNotEqual() {
        Assert.assertFalse(noWhere.equals(null));
    }

    @Test
    public void someCoordinate_andNoWhere_areNotEqual() {
        Assert.assertFalse(octantIa.equals(noWhere));
        Assert.assertFalse(octantIb.equals(noWhere));
        Assert.assertFalse(octantVII.equals(noWhere));
    }

    @Test
    public void distanceOfAnyCoord_andNoWhere_isMinus1() {
        CheckDistance(octantIa, noWhere, -1, 0);
        CheckDistance(noWhere, octantIb, -1, 0);
    }

    @Test
    public void distanceOfAnyCoord_andNull_isMinus1() {
        CheckDistance(octantIa, null, -1, 0);
        CheckDistance(noWhere, null, -1, 0);
    }

    @Test
    public void hashCodes_ofNoWhereCoordinates_areNotEqual() {
        Assert.assertNotEquals(noWhere.hashCode(), new NoWhereCoordinate().hashCode());
    }
}