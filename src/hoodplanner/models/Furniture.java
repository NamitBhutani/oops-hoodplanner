package hoodplanner.models;

public class Furniture extends FloorObject {
    private String name;
    private String pngFIleString;
    private double rotation;

    public Furniture(double length, double width, double x, double y, String name, String pngFIleString) {
        super(length, width, x, y);
        this.name = name;
        this.pngFIleString = pngFIleString;
        this.rotation = 0.0;
    }

    public void rotate() {
        this.rotation += 90.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void move(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
}
