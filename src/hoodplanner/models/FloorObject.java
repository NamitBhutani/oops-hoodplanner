package hoodplanner.models;

import java.io.Serializable;

public class FloorObject implements Serializable {
    protected double length;
    protected double width;
    private double x;
    private double y;

    public FloorObject(double length, double width, double x, double y) {
        this.length = length;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public FloorObject() {
        this.length = 0;
        this.width = 0;
        this.x = 0;
        this.y = 0;
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

    public boolean checkOverlap(FloorObject other) {
        return this.x < other.x + other.length &&
                this.x + this.length > other.x &&
                this.y < other.y + other.width &&
                this.y + this.width > other.y;
    }

    public boolean checkWithinBounds(FloorObject boundary) {
        return this.x >= 0 && this.y >= 0 &&
                this.x + this.length <= boundary.length &&
                this.y + this.width <= boundary.width;
    }

    public boolean containsPoint(int x, int y) {
        System.out.println("x:" + this.x + " y:" + this.y + " length:" + this.length + " width:" + this.width);
        return x >= this.x && x <= this.x + this.length && y >= this.y && y <= this.y + this.width;
    }
}
