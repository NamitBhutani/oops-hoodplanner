package hoodplanner.models;

public class Room extends FloorObject {
    private String name;
    private RoomType type; 

    public Room(String name, double length, double width, double x, double y) {
        super(length, width, x, y);
        this.name = name;
    }

    public Room(String name, double length, double width) {
        super(length, width, 0, 0);
        this.name = name;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public RoomType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + (type != null ? " (" + type.toString() + ")" : "");
    }
}
