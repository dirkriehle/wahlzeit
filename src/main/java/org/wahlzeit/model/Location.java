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

    /**
     * 
     * returns true if locations contain equal coordinates
     */
    @Override
    public boolean equals(Object object) {
        if(object == null || object.getClass() != Location.class) {
            return false;
        }
        Location location = (Location) object;
        return this.coordinate.equals(location.coordinate);
    }
}
