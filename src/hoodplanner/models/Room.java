package hoodplanner.models;

public class Room extends FloorObject {
    private RoomType type; 

    public Room(double length, double width, double x, double y) {
        super(length, width, x, y);
    }

    public Room(double length, double width) {
        super(length, width, 0, 0);
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public RoomType getType() {
        return type;
    }
}
