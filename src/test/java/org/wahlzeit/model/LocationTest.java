package org.wahlzeit.model;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonTypeInfo;
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
    public void createLocationWithoutCoordinateIsNotNull(){
        Location noWhere = new Location();
        Assert.assertNotNull(noWhere.getCoordinate());
    }

    @Test
    public void createLocationWithCoordinateIsNotNull(){
        Coordinate noWhereCoordinate = new NoWhereCoordinate();
        Location noWhere = new Location(noWhereCoordinate);
        Assert.assertNotNull(noWhere.getCoordinate());
    }

    @Test
    public void locationEqualsIsTrueWhenCoordsEquals(){
        Assert.assertEquals(fooLocation, new Location(fooCoords));
    }

    @Test
    public void locationEqualsFalseWhenCoordsUnequal(){
        Assert.assertNotEquals(fooLocation, barLocation);
    }
}
