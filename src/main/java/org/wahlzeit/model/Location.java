package org.wahlzeit.model;

public class Location {

    private Coordinate coordinate;

    public Location(Coordinate coordinate) {
        setCoordinate(coordinate);
    }

    public Location() {
        setCoordinate(new NoWhereCoordinate());
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate){
        this.coordinate=coordinate;
    }
}
