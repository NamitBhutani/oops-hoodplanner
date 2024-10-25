package hoodplanner.models;

public class Room extends FloorObject {
    public Room(double length, double width, double x, double y) {
        super(length, width, x, y);
    }

    public Room(double length, double width) {
        super(length, width, 0, 0);
    }
}
