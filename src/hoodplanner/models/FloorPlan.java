package hoodplanner.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FloorPlan implements Serializable {
    public String name;
    private final List<FloorObject> floorObjects;

    public FloorPlan(String name) {
        this.name = name;
        floorObjects = new ArrayList<>();
    }

    public void addFloorObject(FloorObject floorObject) {
        floorObjects.add(floorObject);
    }

    public void removeFloorObject(FloorObject floorObject) {
        floorObjects.remove(floorObject);
    }

    public List<FloorObject> getFloorObjects() {
        return floorObjects;
    }

    public void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    public static FloorPlan loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (FloorPlan) ois.readObject();
        }
    }

    public static FloorPlan load() {
        FloorPlan floorPlan = new FloorPlan("Sample Floor Plan");
        floorPlan.addFloorObject(new Room(100, 100, 0, 0));
        floorPlan.addFloorObject(new Room(200, 200, 100, 200));
        return floorPlan;
    }
}