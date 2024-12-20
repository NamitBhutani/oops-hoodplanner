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


    public int size() {
        return getLength() > getWidth() ? (int) getLength() : (int) getWidth();
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

    public void removeDoor(Door door) {
        doors.remove(door);
    }

    public void removeWindow(Window window) {
        windows.remove(window);
    }

    public Door getDoorAt(int distanceFromStart) {
        for (Door door: doors) {
            if (door.getDistanceFromStart() <= distanceFromStart && door.getDistanceFromStart() + door.getLength() >= distanceFromStart) {
                return door;
            }
        }

        return null;
    }

    public Window getWindowAt(int distanceFromStart) {
        for (Window window: windows) {
            if (window.getDistanceFromStart() <= distanceFromStart && window.getDistanceFromStart() + window.getLength() >= distanceFromStart) {
                return window;
            }
        }

        return null;
    }

    public Door findAdjacentDoor(Wall wall, Door door) {
        // check if I am horizontal or vertical
        int myStart;
        int otherStart;

        // North South
        if (getWidth() == 0 && wall.getWidth() == 0) {
            myStart = (int) getX();
            otherStart = (int)wall.getX();
        } else {
            myStart = (int) getY();
            otherStart = (int) wall.getY();
        }


        for (Door otherDoor: doors) {
            if (otherDoor == door) {
                continue;
            }

            if (myStart + otherDoor.getDistanceFromStart() == otherStart + door.getDistanceFromStart() && otherDoor.getLength() == door.getLength()) {
                return otherDoor;
            }
        }
        return null;
    }


    public void simplyfyDoors() {

        ArrayList<Door> olddoors = new ArrayList<>(this.doors);
        System.out.println("Old doors: " + olddoors.size());
        for (Door door: olddoors) {
            for (Door otherDoor: olddoors) {
                if (door == otherDoor || !doors.contains(door) || !doors.contains(otherDoor)) {
                    continue; // TODO: Fix this bug it doesnt simplify all doors
                }
                
                if (otherDoor.getDistanceFromStart() + otherDoor.getLength() < door.getDistanceFromStart() + door.getLength() && otherDoor.getDistanceFromStart() >= door.getDistanceFromStart()) {
                    System.out.println("Deleting consumed door");
                    doors.remove(otherDoor);
                }
                if (otherDoor.getDistanceFromStart() < door.getDistanceFromStart() + door.getLength() && otherDoor.getDistanceFromStart() >= door.getDistanceFromStart()) {
                    System.out.println("Merging doors");
                    door.setLength(otherDoor.getDistanceFromStart() - door.getDistanceFromStart() + otherDoor.getLength());
                    doors.remove(otherDoor);
                }
            }
        }
    }
}
