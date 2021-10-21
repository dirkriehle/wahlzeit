package org.wahlzeit.model;

import java.util.Objects;

public class Location {

    private final Coordinate coordinate;

    /**
     * @methodtype constructor
     */
    public Location(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * @methodtype constructor
     */
    public Location(final double x, final double y, final double z) {
        this.coordinate = new Coordinate(x, y, z);
    }

    /**
     * @methodtype get
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(getCoordinate(), location.getCoordinate());
    }

}
