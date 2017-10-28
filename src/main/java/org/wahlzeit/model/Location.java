package org.wahlzeit.model;

public class Location {

    public Coordinate coordinate;

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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Location){
            return coordinate.equals(((Location) obj).coordinate);
        }
        return false;
    }

    public double getDistance(Location noWhereLocation) {
        return coordinate.getDistance(noWhereLocation.coordinate);
    }
}
