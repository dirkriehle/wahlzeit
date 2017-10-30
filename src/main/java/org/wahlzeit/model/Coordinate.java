package org.wahlzeit.model;

/**
 * Represents a Cartesian coordinate of a three dimensional Cartesian coordinate system
 */
public class Coordinate {
    private double y;
    private double x;
    private double z;
    public static final Double MAX_VALUE = Double.MAX_VALUE - 1E292;

    public Coordinate() {
    }

    public Coordinate(double y, double x, double z) {
        setY(y);
        setX(x);
        setZ(z);
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        throwExceptionIfNotInRange(x);
        this.x = x;
    }

    public void setY(double y) {
        throwExceptionIfNotInRange(y);
        this.y = y;
    }

    public void setZ(double z) {
        throwExceptionIfNotInRange(z);
        this.z = z;
    }

    public double getDistance(Coordinate otherCoord) {
        if (otherCoord instanceof NoWhereCoordinate || otherCoord == null) {
            return -1;
        }
        double xDifference = getX() - otherCoord.getX();
        double yDifference = getY() - otherCoord.getY();
        double zDifference = getZ() - otherCoord.getZ();
        double radicand = square(xDifference) + square(yDifference) + square(zDifference);
        return Math.sqrt(radicand);
    }

    public boolean isEqual(Coordinate otherCoord) {
        if (otherCoord == this) {
            return true;
        }
        if (otherCoord == null) {
            return false;
        }
        boolean xOrdinatesAreEqual = doublesAreEqual(getX(), otherCoord.getX());
        boolean yOrdinatesAreEqual = doublesAreEqual(getY(), otherCoord.getY());
        boolean zOrdinatesAreEqual = doublesAreEqual(getZ(), otherCoord.getZ());
        return xOrdinatesAreEqual && yOrdinatesAreEqual && zOrdinatesAreEqual;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(y);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            return isEqual((Coordinate) obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + ")";
    }

    private boolean doublesAreEqual(double firstDouble, double secondDouble) {
        return Double.compare(firstDouble, secondDouble) == 0;
    }

    private double square(double value) {
        return Math.pow(value, 2);
    }

    private void throwExceptionIfNotInRange(double value) {
        if (Math.abs(value) > MAX_VALUE) {
            throw new IllegalArgumentException("The input value should be smaller than " + MAX_VALUE);
        }
    }
}
