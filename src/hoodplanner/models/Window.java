package hoodplanner.models;

import java.io.Serializable;

public class Window implements Serializable {
    public int length;
    public int distFromStart;

    public Window(int length, int distFromStart) {
        this.length = length;
        this.distFromStart = distFromStart;
    }

    public int getLength() {
        return length;
    }

    public int getDistanceFromStart() {
        return distFromStart;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setDistanceFromStart(int distFromStart) {
        this.distFromStart = distFromStart;
    }

}