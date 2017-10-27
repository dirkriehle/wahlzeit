package org.wahlzeit.model;

public class Coordinate {
    private double y;
    private double x;
    private double z;

    public Coordinate(double y, double x, double z) {
        this.y = y;
        this.x = x;
        this.z = z;
    }

    public Coordinate() {

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
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "("+x+","+y+","+z+","+")";
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

    public double getDistance(Coordinate otherCoordinate) {
        if(otherCoordinate instanceof NoWhereCoordinate)return -1;

        double xDifference= getX()- otherCoordinate.getX();
        double yDifference= getY()- otherCoordinate.getY();
        double zDifference= getZ()- otherCoordinate.getZ();
        double radicand= square(xDifference)+square(yDifference)+square(zDifference);
        return Math.sqrt(radicand);
    }

    private double square(double value) {
        return Math.pow(value,2);
    }
}
