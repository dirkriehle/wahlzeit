package org.wahlzeit.model;

public class Coordinate {
    public static final Double MAX_VALUE = Double.MAX_VALUE - 1E292;

    private double y;
    private double x;
    private double z;

    public Coordinate() {}
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

    public double getDistance(Coordinate otherCoordinate) {
        if(otherCoordinate instanceof NoWhereCoordinate || otherCoordinate == null){
            return -1;
        }
        double xDifference= getX()- otherCoordinate.getX();
        double yDifference= getY()- otherCoordinate.getY();
        double zDifference= getZ()- otherCoordinate.getZ();
        double radicand= square(xDifference)+square(yDifference)+square(zDifference);
        return Math.sqrt(radicand);
    }

    protected boolean isEqual(Coordinate other){
        return getX()== other.getX() && getY() == other.getY() && getZ() == this.getZ();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coordinate){
            return isEqual((Coordinate) obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return "("+x+","+y+","+z+","+")";
    }

    private double square(double value) {
        return Math.pow(value,2);
    }

    private void throwExceptionIfNotInRange(double value) {
        if(Math.abs(value) > MAX_VALUE ){
            throw new IllegalArgumentException("The input value should be smaller than "+MAX_VALUE);
        }
    }
}
