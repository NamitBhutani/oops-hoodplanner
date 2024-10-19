package hoodplanner.models;

import java.util.ArrayList;
import java.util.List;

public class FloorPlan {
    public String name;
    private final List<Room> rooms;

    public FloorPlan(String name) {
        this.name = name;
        rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void save() {
        // Save the floor plan to a file
    }

    public static FloorPlan load() {
        // TODO: Load the floor plan from a file

        // For now return a sample floor plan
        FloorPlan floorPlan = new FloorPlan("Sample Floor Plan");
        floorPlan.addRoom(new Room(100, 100, 0, 0));
        floorPlan.addRoom(new Room(200, 200, 100, 200));

        return floorPlan;
    }
}