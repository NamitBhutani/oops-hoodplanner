package hoodplanner.models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class Room extends FloorObject {
    private String name;
    private RoomType type;
    private final List<Furniture> containedFurniture; // List to store furniture
    public Wall northWall;
    public Wall southWall;
    public Wall eastWall;
    public Wall westWall;

    public Room(String name, double length, double width, double x, double y) {
        super(length, width, x, y);
        this.name = name;
        this.containedFurniture = new ArrayList<>();

        this.northWall = new Wall(length, 0, x, y);
        this.southWall = new Wall(length, 0, x, y + width);
        this.eastWall = new Wall(0, width, x + length, y);
        this.westWall = new Wall(0, width, x, y);
    }

    public List<Wall> getWalls() {
        return Arrays.asList(northWall, southWall, eastWall, westWall);
    }

    public Room(String name, double length, double width) {
        super(length, width, 0, 0);
        this.name = name;
        this.containedFurniture = new ArrayList<>();
    }

    // Setters and Getters
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


    // Furniture Management
    public void addContainedObject(Furniture furniture) {
        if (furniture != null) {
            // Ensure furniture does not overlap with existing furniture in the room
            for (Furniture existingFurniture : containedFurniture) {
                if (furnitureOverlaps(existingFurniture, furniture)) {
                    System.out.println("Cannot add furniture: " + furniture.getName() + " because it overlaps with " + existingFurniture.getName());
                    return; // Exit if overlap detected
                }
            }
            containedFurniture.add(furniture);
        }
    }

    public void removeContainedObject(Furniture furniture) {
        containedFurniture.remove(furniture);
    }

    public List<Furniture> getContainedFurniture() {
        return new ArrayList<>(containedFurniture); // Return a copy to prevent external modification
    }

    public int getFurnitureCount() {
        return containedFurniture.size();
    }

    // Method to get furniture images
    public List<ImageIcon> getFurnitureImages() {
        List<ImageIcon> images = new ArrayList<>();
        for (Furniture furniture : containedFurniture) {
            try {
                ImageIcon icon = new ImageIcon(furniture.getImagePath());
                images.add(icon);
            } catch (Exception e) {
                System.err.println("Error loading image for furniture: " + furniture.getName());
                images.add(null); // Add null if image loading fails
            }
        }
        return images;
    }

    // Helper method to check overlap based on coordinates and size
    private boolean furnitureOverlaps(Furniture existingFurniture, Furniture newFurniture) {
        // Check if the new furniture overlaps with the existing furniture based on their positions and sizes
        return !(newFurniture.getX() + newFurniture.getWidth() <= existingFurniture.getX() ||
                 newFurniture.getX() >= existingFurniture.getX() + existingFurniture.getWidth() ||
                 newFurniture.getY() + newFurniture.getLength() <= existingFurniture.getY() ||
                 newFurniture.getY() >= existingFurniture.getY() + existingFurniture.getLength());
    }


    public Point getLocation() {
        return new Point((int) getX(), (int) getY());

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

    @Override
    public void setX(double x) {
        // System.out.println("Setting x to " + x);
        super.setX(x);
        updateWallPositions();
    }

    @Override
    public void setY(double y) {
        // System.out.println("Setting y to " + y);
        super.setY(y);
        updateWallPositions();
    }

    private void updateWallPositions() {
        northWall.setX(getX());
        northWall.setY(getY());
        southWall.setX(getX());
        southWall.setY(getY() + getWidth());
        eastWall.setX(getX() + getLength());
        eastWall.setY(getY());
        westWall.setX(getX());
        westWall.setY(getY());
    }
}
