package org.wahlzeit.model;

public class Coordinate {
    protected double x;
    protected double y;
    protected double z;

    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
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

    public double getDistance(Coordinate coordinate){
        double d = Math.pow(coordinate.getX() - this.x, 2) + Math.pow(coordinate.getY() - this.y, 2)
                + Math.pow(coordinate.getZ() - this.z, 2);
        double distance = Math.sqrt(d);
        return distance;
    }

    public boolean isEqual(Coordinate coordinate) {
        if(coordinate.getX() == this.x && coordinate.getY() == this.y && coordinate.getZ() == this.z)
            return true;
        return false;
    }

}
