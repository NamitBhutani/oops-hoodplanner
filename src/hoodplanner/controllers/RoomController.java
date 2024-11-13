package hoodplanner.controllers;

import hoodplanner.models.FloorObject;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.RightPanel;
import hoodplanner.ui.RoomLabel;

public class RoomController extends FloorObjectController<Room, RoomLabel> {
    private FloorPlan floorPlan;

    public RoomController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void setFloorPlan(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void addRoom(double width, double height, LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel) {
        
        double x = 0;
        double y = 0;
        boolean placed = false;
        double spacing = 10;  // Optional gap between rooms
    
        // Loop until we find a non-overlapping position for the new room
        while (!placed) {
            boolean hasSpace = true;
    
            // Check if the current (x, y) position overlaps with any existing FloorObjects
            for (FloorObject existing : floorPlan.getFloorObjects()) {
                if (isOverlapping(existing, x, y, width, height, spacing)) {
                    hasSpace = false;
                    break;
                }
            }
    
            if (hasSpace) {
                // Position found, exit loop
                placed = true;
            } else {
                // Move to the next position in row-major order
                x += width + spacing;
                if (x + width > floorPlan.width) {
                    x = 0;
                    y += height + spacing;
                }
            }
        }
    
        // Create the room with the found coordinates
        Room room = new Room(width, height, x, y);
        floorPlan.addFloorObject(room);

        RoomLabel roomLabel = new RoomLabel(room, this);
        createObjectLabel(room, roomLabel, leftPanel, rightPanel);
    }

    public void deleteRoom(Room room, LeftPanel leftPanel) {
        floorPlan.removeFloorObject(room);

        RoomLabel roomLabelToDelete = null;
        for (RoomLabel label : getObjectLabels()) {
            if (label.getObject().equals(room)) {
                roomLabelToDelete = label;
                break;
            }
        }

        if (roomLabelToDelete != null) {
            deleteObjectLabel(roomLabelToDelete, leftPanel);
        }
    }

    // Helper method to check overlap with a spacing buffer
    private boolean isOverlapping(FloorObject existing, double x, double y, double width, double height, double spacing) {
        double buffer = spacing / 2.0;
        return !(x + width + buffer <= existing.getX() || 
                x >= existing.getX() + existing.getWidth() + buffer ||
                y + height + buffer <= existing.getY() ||
                y >= existing.getY() + existing.getHeight() + buffer);
    }
}
