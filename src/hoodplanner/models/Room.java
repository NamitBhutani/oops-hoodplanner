package hoodplanner.models;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room extends FloorObject {
    private String name;
    private RoomType type;
    private final List<Furniture> containedFurniture; // List to store furniture

    public Room(String name, double length, double width, double x, double y) {
        super(length, width, x, y);
        this.name = name;
        this.containedFurniture = new ArrayList<>();
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

    // Paint method to render the room and the furniture inside it
    public void paint(Graphics g) {
        // Paint the room background first
        g.setColor(Color.LIGHT_GRAY);  // Or any other room color
        g.fillRect(0, 0, (int) getWidth(), (int) getLength());

        // Paint the furniture inside the room
        for (Furniture furniture : containedFurniture) {
            // Make sure each furniture item gets drawn at its specified position
            furniture.draw(g, (int) getX(), (int) getY());
        }
    }

    // Implementing the highlight feature
    public void setHighlight(boolean highlight) {
        // This can be used to highlight the room when dragging and dropping
        // You can change the room's background color or draw a border around the room to indicate highlighting
    }

    public Point getLocation() {
        return new Point((int) getX(), (int) getY());
    }
}
