package hoodplanner.models;

public class Room extends FloorObject {
    private String name;
    private RoomType type; 
    public Wall northWall;
    public Wall southWall;
    public Wall eastWall;
    public Wall westWall;

    public Room(String name, double length, double width, double x, double y) {
        super(length, width, x, y);
        this.name = name;

        this.northWall = new Wall(length, 0, x, y);
        this.southWall = new Wall(length, 0, x, y + width);
        this.eastWall = new Wall(0, width, x + length, y);
        this.westWall = new Wall(0, width, x, y);
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


    @Override
    public void setLength(double length) {
        super.setLength(length);
        northWall.setLength(length);
        southWall.setLength(length);
    }

    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        eastWall.setWidth(width);
        westWall.setWidth(width);
    }
}
