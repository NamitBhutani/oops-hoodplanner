package hoodplanner.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Wall extends FloorObject {

    private final List<Window> windows;
    private final List<Door> doors;

    public Wall(double length, double width, double x, double y) {
        super(length, width, x, y);

        this.windows = new ArrayList<>();
        this.doors = new ArrayList<>();

        // this.doors.add(new Door(25, x, y));
        // this.windows.add(new Window(25, x, y + 50));

        // this.addDoor(45, 150);
        // this.addDoor(25, 100);

        // this.addWindow(60, 30);
    }


    public List<Door> getDoors() {
        return doors;
    }

    public List<Window> getWindows() {
        return windows;
    }

    public void addDoor(Door door) {
        doors.add(door);

        doors.sort(Comparator.comparingInt(Door::getDistanceFromStart));
    }

    public void addDoor(int length, int distFromStart) {
        addDoor(new Door(length, distFromStart));   
    }

    public void addWindow(Window window) {
        windows.add(window);

        windows.sort(Comparator.comparingInt(Window::getDistanceFromStart));
    }

    public void addWindow(int length, int distFromStart) {
        addWindow(new Window(length, distFromStart));
    }
}
