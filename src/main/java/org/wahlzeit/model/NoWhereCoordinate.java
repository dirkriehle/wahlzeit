package org.wahlzeit.model;

public class NoWhereCoordinate extends Coordinate {
    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public void setX(double x) {
    }

    @Override
    public void setY(double y) {
    }

    @Override
    public void setZ(double z) {
    }

    @Override
    public boolean isEqual(Coordinate other) {
        return false;
    }

    @Override
    public double getDistance(Coordinate otherCoordinate) {
        return -1;
    }
}
