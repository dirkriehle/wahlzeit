package org.wahlzeit.model;

public class Location {

    /**
     * The coordinate of the location.
     */
    protected Coordinate coordinate;

    /**
     * 
     * @methodtype constructor
     */
    public Location(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * 
     * @methodtype get
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }
}
