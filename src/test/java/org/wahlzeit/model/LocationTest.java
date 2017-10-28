package org.wahlzeit.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocationTest {

    private Location noWhereLocation;
    private Location fooLocation;
    private Location barLocation;

    private Coordinate fooCoords;
    private Coordinate barCoords;

    @Before
    public void setUp(){
        noWhereLocation=new Location(new NoWhereCoordinate());
        fooCoords=new Coordinate(0,1,2);
        barCoords=new Coordinate(-2,-1,0);
        fooLocation = new Location(fooCoords);
        barLocation = new Location(barCoords);
    }

    @Test
    public void createLocation_withoutCoordinate_isNotNull(){
        Location noWhere = new Location();
        Assert.assertNotNull(noWhere.getCoordinate());
    }

    @Test
    public void createLocation_withCoordinate_isNotNull(){
        Coordinate noWhereCoordinate = new NoWhereCoordinate();
        Location noWhere = new Location(noWhereCoordinate);
        Assert.assertNotNull(noWhere.getCoordinate());
    }

    @Test
    public void locationEquals_whenCoordsEquals_isTrue(){
        Assert.assertEquals(fooLocation, new Location(fooCoords));
    }

    @Test
    public void locationEquals_whenCoordsNotEquals_isTrue(){
        Assert.assertNotEquals(fooLocation, barLocation);
    }

    @Test
    public void distanceOfNowWhere_andNoWhere_isMinus1(){
        Assert.assertEquals(-1,noWhereLocation.getDistance(noWhereLocation),0);
    }
    @Test
    public void distanceOfFooLocation_andNoWhere_isMinus1(){
        Assert.assertEquals(-1,fooLocation.getDistance(noWhereLocation),0);
    }
    @Test
    public void distanceOfFooLocation_andBarLocation_isNotMinus1(){
        Assert.assertNotEquals(-1,fooLocation.getDistance(barLocation),0);
    }
}
