package org.wahlzeit.model;

public class Location {
    protected Coordinate coordinate;

    public Location(Coordinate coordinate){
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate){
        this.coordinate = coordinate;
    }
}
