package hoodplanner.models;

public class Door {
    public int length;
    public int distFromStart;

    public Door(int length, int distFromStart) {
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
