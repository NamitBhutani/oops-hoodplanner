package hoodplanner.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FloorPlan implements Serializable {
    public String name;
    public String saveFilePath;
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
        this.saveFilePath = filePath;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    public static FloorPlan loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            FloorPlan plan = (FloorPlan) ois.readObject();
            plan.saveFilePath = filePath;
            return plan;
        }
    }

    public static FloorPlan load() {
        FloorPlan floorPlan = new FloorPlan("Sample Floor Plan");
        return floorPlan;
    }

    public String displayName() {
        if (saveFilePath == null) {
            return name;
        } else {
            return name + " | " + saveFilePath;
        }
    }
}