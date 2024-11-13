package hoodplanner.controllers;

import hoodplanner.models.FloorObject;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import hoodplanner.models.RoomType;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.RightPanel;
import hoodplanner.ui.RoomLabel;
import java.util.List;
import java.util.stream.Collectors;

public class RoomController extends FloorObjectController<Room, RoomLabel> {
    private FloorPlan floorPlan;

    public RoomController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void setFloorPlan(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void addRoom(String name, RoomType type, double width, double height, LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel) {
        // Row major order placement of rooms
        double x = 0;
        double y = 0;
        boolean placed = false;
        double spacing = 25;  // Optional gap between rooms
    
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
        
        addRoomAt(name, type, width, height, x, y, leftPanel, rightPanel);
    }

    public void addRoom(String name, RoomType type, double width, double height, LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel, Room referenceRoom, String position, String alignment) {
        double x = referenceRoom.getX();
        double y = referenceRoom.getY();
        double spacing = 0;  // Optional gap between rooms
    
        // Adjust x and y based on the specified position and alignment
        switch (position.toLowerCase()) {
            case "north":
                y -= height + spacing;
                if ("left".equalsIgnoreCase(alignment)) {
                    x = referenceRoom.getX();
                } else if ("right".equalsIgnoreCase(alignment)) {
                    x = referenceRoom.getX() + referenceRoom.getWidth() - width;
                }
                break;
    
            case "south":
                y += referenceRoom.getHeight() + spacing;
                if ("left".equalsIgnoreCase(alignment)) {
                    x = referenceRoom.getX();
                } else if ("right".equalsIgnoreCase(alignment)) {
                    x = referenceRoom.getX() + referenceRoom.getWidth() - width;
                }
                break;
    
            case "east":
                x += referenceRoom.getWidth() + spacing;
                if ("top".equalsIgnoreCase(alignment)) {
                    y = referenceRoom.getY();
                } else if ("bottom".equalsIgnoreCase(alignment)) {
                    y = referenceRoom.getY() + referenceRoom.getHeight() - height;
                }
                break;
    
            case "west":
                x -= width + spacing;
                if ("top".equalsIgnoreCase(alignment)) {
                    y = referenceRoom.getY();
                } else if ("bottom".equalsIgnoreCase(alignment)) {
                    y = referenceRoom.getY() + referenceRoom.getHeight() - height;
                }
                break;    
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    
        // Ensure the new position does not overlap with existing FloorObjects
        for (FloorObject existing : floorPlan.getFloorObjects()) {
            if (isOverlapping(existing, x, y, width, height, spacing)) {
                throw new IllegalArgumentException("The specified position results in an overlap with another room.");
            }
        }
    
        // Create and add the room at the calculated position
        addRoomAt(name, type, width, height, x, y, leftPanel, rightPanel);
    }
    

    private void addRoomAt(String name, RoomType type, double width, double height, double x, double y, LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel) {
        int count = 2;
        String originalName = name;
        String newName = name;
        for (Room room : getRooms()) {
            if (room.getName().equals(newName)) {
                newName = originalName + " " + count;
                count++;
            }
        }
        name = newName;

        Room room = new Room(name, width, height, x, y);
        room.setType(type);
        floorPlan.addFloorObject(room);

        RoomLabel roomLabel = new RoomLabel(room, this);
        createObjectLabel(room, roomLabel, leftPanel, rightPanel);
    }                                   



    public List<Room> getRooms() {
        return floorPlan.getFloorObjects().stream()
                .filter(obj -> obj instanceof Room)
                .map(obj -> (Room) obj)
                .collect(Collectors.toList());
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
