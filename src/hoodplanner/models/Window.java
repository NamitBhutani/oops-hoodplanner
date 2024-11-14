package hoodplanner.models;

public class Window {
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

}