package hoodplanner.models;

import java.util.ArrayList;
import java.util.List;

public class Wall extends FloorObject {
    private final List<Door> doors; // List to store doors
    private final List<Window> windows; // List to store windows

    public Wall(double length, double thickness, double x, double y) {
        super(length, thickness, x, y);
        this.doors = new ArrayList<>();
        this.windows = new ArrayList<>();
    }

    public Wall(double length, double thickness) {
        this(length, thickness, 0, 0);
    }

    // Method to add a door
    public void addDoor(Door door) {
        doors.add(door);
    }

    // Method to remove a door
    public void removeDoor(Door door) {
        doors.remove(door);
    }

    // Method to get the list of doors
    public List<Door> getDoors() {
        return doors;
    }

    // Method to add a window
    public void addWindow(Window window) {
        windows.add(window);
    }

    // Method to remove a window
    public void removeWindow(Window window) {
        windows.remove(window);
    }

    // Method to get the list of windows
    public List<Window> getWindows() {
        return windows;
    }
}
