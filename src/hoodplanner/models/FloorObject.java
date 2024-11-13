package hoodplanner.models;

import java.io.Serializable;

public class FloorObject implements Serializable {
    private double length;
    private double width;
    private double x;
    private double y;

    public FloorObject(double length, double width, double x, double y) {
        this.length = length;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "FloorObject{" +
                "length=" + length +
                ", width=" + width +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean checkOverlap(FloorObject other) {
        return this.x < other.x + other.length &&
                this.x + this.length > other.x &&
                this.y < other.y + other.width &&
                this.y + this.width > other.y;
    }

    public boolean checkWithinBounds(FloorObject other) {
        return this.x >= 0 && this.y >= 0 &&
                this.x + this.length <= other.length &&
                this.y + this.width <= other.width;
    }
}
