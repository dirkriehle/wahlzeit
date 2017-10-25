package org.wahlzeit.model;

import org.junit.Assert;
import org.junit.Test;

public class LocationTest {

    @Test
    public void createLocationWithoutCoordinate(){
        Location noWhere = new Location();
        Assert.assertNotNull(noWhere.getCoordinate());
    }

    @Test
    public void createLocationWithCoordinate(){
        Coordinate noWhereCoordinate = new NoWhereCoordinate();
        Location noWhere = new Location(noWhereCoordinate);
        Assert.assertNotNull(noWhere.getCoordinate());
    }
}
