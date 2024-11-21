package hoodplanner.controllers;

import hoodplanner.models.Door;
import hoodplanner.models.FloorObject;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import hoodplanner.models.RoomType;
import hoodplanner.models.Wall;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.RightPanel;
import hoodplanner.ui.RoomLabel;
import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Collectors;

public class RoomController extends FloorObjectController<Room, RoomLabel> {
    private FloorPlan floorPlan;

    public RoomController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public FloorPlan getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void addRoom(String name, RoomType type, double width, double length, LeftPanel leftPanel,
            RightPanel<Room, RoomLabel> rightPanel) {
        // Row major order placement of rooms
        double x = 0;
        double y = 0;
        boolean placed = false;
        double spacing = 25; // Optional gap between rooms

        // Loop until we find a non-overlapping position for the new room
        while (!placed) {
            boolean hasSpace = true;

            // Check if the current (x, y) position overlaps with any existing FloorObjects
            for (FloorObject existing : floorPlan.getFloorObjects()) {
                if (isOverlapping(existing, x, y, width, length, spacing)) {
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
                    y += length + spacing;
                }
            }
        }

        // Create the room with the found coordinates
        addRoomAt(name, type, width, length, x, y, leftPanel, rightPanel);
    }

    public void addRoom(String name, RoomType type, double width, double length, LeftPanel leftPanel,
            RightPanel<Room, RoomLabel> rightPanel, Room referenceRoom, String position, String alignment) {
        double x = referenceRoom.getX();
        double y = referenceRoom.getY();

        // NOTE: The reference room's width and length are swapped, IDK why but it works
        // ;-;

        // System.out.println("Reference room: " + referenceRoom.getName());
        // System.out.println("REFERENCE ROOM X: " + referenceRoom.getX());
        // System.out.println("REFERENCE ROOM Y: " + referenceRoom.getY());
        // System.out.println("REFERENCE ROOM WIDTH: " + referenceRoom.getWidth());
        // System.out.println("REFERENCE ROOM LENGTH: " + referenceRoom.getLength());
        double spacing = 0; // Optional gap between rooms

        // Adjust x and y based on the specified position and alignment
        switch (position.toLowerCase()) {
            case "north" -> {
                y -= length + spacing;
                switch (alignment.toLowerCase()) {
                    case "left" -> x = referenceRoom.getX();
                    case "right" -> x = referenceRoom.getX() + referenceRoom.getLength() - width;
                    case "center", "middle" -> x = referenceRoom.getX() + (referenceRoom.getLength() - width) / 2;
                    default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
                }
            }

            case "south" -> {
                y += referenceRoom.getWidth() + spacing;
                switch (alignment.toLowerCase()) {
                    case "left" -> x = referenceRoom.getX();
                    case "right" -> x = referenceRoom.getX() + referenceRoom.getLength() - width;
                    case "center", "middle" -> x = referenceRoom.getX() + (referenceRoom.getLength() - width) / 2;
                    default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
                }
            }

            case "east" -> {
                x += referenceRoom.getLength() + spacing;
                switch (alignment.toLowerCase()) {
                    case "top" -> y = referenceRoom.getY();
                    case "bottom" -> y = referenceRoom.getY() + referenceRoom.getWidth() - length;
                    case "middle", "center" -> y = referenceRoom.getY() + (referenceRoom.getWidth() - length) / 2;
                    default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
                }
            }

            case "west" -> {
                x -= width + spacing;
                switch (alignment.toLowerCase()) {
                    case "top" -> y = referenceRoom.getY();
                    case "bottom" -> y = referenceRoom.getY() + referenceRoom.getWidth() - length;
                    case "middle", "center" -> y = referenceRoom.getY() + (referenceRoom.getWidth() - length) / 2;
                    default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
                }
            }
            default -> throw new IllegalArgumentException("Invalid position: " + position);
        }

        // Ensure the new position does not overlap with existing FloorObjects
        for (FloorObject existing : floorPlan.getFloorObjects()) {
            if (isOverlapping(existing, x, y, width, length, spacing)) {
                throw new IllegalArgumentException("The specified position results in an overlap with another room.");
            }
        }

        // Create and add the room at the calculated position
        addRoomAt(name, type, width, length, x, y, leftPanel, rightPanel);
    }

    private void addRoomAt(String name, RoomType type, double width, double length, double x, double y,
            LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel) {
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

        Room room = new Room(name, width, length, x, y);
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
    private boolean isOverlapping(FloorObject existing, double x, double y, double width, double length,
            double spacing) {
        double buffer = spacing / 2.0;
        return !(x + width + buffer <= existing.getX() ||
                x >= existing.getX() + existing.getWidth() + buffer ||
                y + length + buffer <= existing.getY() ||
                y >= existing.getY() + existing.getLength() + buffer);
    }

    public void syncAdjacentRoomDoors() {
        FloorPlan floorPlan = getFloorPlan();

        // Iterate through all rooms
        for (FloorObject obj1 : floorPlan.getFloorObjects()) {
            if (!(obj1 instanceof Room))
                continue;
            Room room1 = (Room) obj1;

            for (FloorObject obj2 : floorPlan.getFloorObjects()) {
                if (!(obj2 instanceof Room) || obj1 == obj2)
                    continue;
                Room room2 = (Room) obj2;

                // Check if rooms are now adjacent
                if (areRoomsAdjacent(room1, room2)) {
                    syncAdjacentRoomWalls(room1, room2);
                }
            }
        }

        // repaint all room labels
        for (RoomLabel label : getObjectLabels()) {
            label.repaint();
        }
    }

    private boolean areRoomsAdjacent(Room room1, Room room2) {
        // Check if rooms are touching
        Rectangle bounds1 = new Rectangle(
                (int) room1.getX(),
                (int) room1.getY(),
                (int) room1.getWidth(),
                (int) room1.getLength());
        Rectangle bounds2 = new Rectangle(
                (int) room2.getX(),
                (int) room2.getY(),
                (int) room2.getWidth(),
                (int) room2.getLength());

        // Check if rectangles touch or overlap
        return bounds1.intersects(bounds2) ||
                bounds1.x + bounds1.width == bounds2.x ||
                bounds1.x == bounds2.x + bounds2.width ||
                bounds1.y + bounds1.height == bounds2.y ||
                bounds1.y == bounds2.y + bounds2.height;
    }

    private void syncAdjacentRoomWalls(Room room1, Room room2) {
        Wall[] walls1 = { room1.northWall, room1.southWall, room1.eastWall, room1.westWall };
        Wall[] walls2 = { room2.northWall, room2.southWall, room2.eastWall, room2.westWall };

        for (Wall wall1 : walls1) {
            for (Wall wall2 : walls2) {
                if (areWallsAdjacent(wall1, wall2)) {
                    syncWallDoors(wall1, wall2);
                }
            }
        }
    }

    private boolean areWallsAdjacent(Wall wall1, Wall wall2) {
        double tolerance = 5.0; // Tolerance for considering walls adjacent

        // Check North-South adjacency
        if (Math.abs(wall1.getLength() - wall2.getLength()) < tolerance) {
            // Horizontal walls (North and South)
            if (Math.abs(wall1.getY() - wall2.getY()) < tolerance) {
                return Math.abs(wall1.getX() + wall1.getLength() - wall2.getX()) < tolerance ||
                        Math.abs(wall2.getX() + wall2.getLength() - wall1.getX()) < tolerance;
            }
        }

        // Check East-West adjacency
        if (Math.abs(wall1.getWidth() - wall2.getWidth()) < tolerance) {
            // Vertical walls (East and West)
            if (Math.abs(wall1.getX() - wall2.getX()) < tolerance) {
                return Math.abs(wall1.getY() + wall1.getWidth() - wall2.getY()) < tolerance ||
                        Math.abs(wall2.getY() + wall2.getWidth() - wall1.getY()) < tolerance;
            }
        }

        return false;
    }

    private void syncWallDoors(Wall wall1, Wall wall2) {
        // Copy doors from wall1 to wall2, adjusting positions
        for (Door door : wall1.getDoors()) {
            // Check if this door already exists on wall2
            boolean doorExists = wall2.getDoors().stream()
                    .anyMatch(existingDoor -> Math
                            .abs(existingDoor.getDistanceFromStart()
                                    - calculateMirroredPosition(wall1, wall2, door.getDistanceFromStart())) < 5
                            &&
                            existingDoor.getLength() == door.getLength());

            if (!doorExists) {
                // Create a new door on wall2 with mirrored position
                Door mirroredDoor = new Door(
                        door.getLength(),
                        calculateMirroredPosition(wall1, wall2, door.getDistanceFromStart()));
                wall2.addDoor(mirroredDoor);
            }
        }
    }

    private int calculateMirroredPosition(Wall wall1, Wall wall2, int originalPosition) {
        // For North-South walls (horizontal)
        if (Math.abs(wall1.getLength() - wall2.getLength()) < 5) {
            // Maintain proportional position
            return (int) (originalPosition * (wall2.getLength() / wall1.getLength()));
        }

        // For East-West walls (vertical)
        if (Math.abs(wall1.getWidth() - wall2.getWidth()) < 5) {
            // Maintain proportional position
            return (int) (originalPosition * (wall2.getWidth() / wall1.getWidth()));
        }

        // Fallback to simple mirroring
        return (int) (wall1.getLength() - originalPosition);
    }

    // public boolean hasNeighbor(FloorObject object, String direction) {
    // double x = object.getX();
    // double y = object.getY();
    // double width = object.getWidth();
    // double length = object.getLength();

    // for (FloorObject existing : floorPlan.getFloorObjects()) {
    // if (existing.equals(object)) {
    // continue;
    // }

    // double buffer = 0;
    // switch (direction.toLowerCase()) {
    // case "north" -> {
    // if (x + width + buffer > existing.getX() && x < existing.getX() +
    // existing.getWidth() + buffer && y - buffer < existing.getY() +
    // existing.getLength()) {
    // return true;
    // }
    // }
    // case "south" -> {
    // if (x + width + buffer > existing.getX() && x < existing.getX() +
    // existing.getWidth() + buffer && y + length + buffer > existing.getY()) {
    // return true;
    // }
    // }
    // case "east" -> {
    // if (y + length + buffer > existing.getY() && y < existing.getY() +
    // existing.getLength() + buffer && x + width + buffer > existing.getX()) {
    // return true;
    // }
    // }
    // case "west" -> {
    // if (y + length + buffer > existing.getY() && y < existing.getY() +
    // existing.getLength() + buffer && x - buffer < existing.getX() +
    // existing.getWidth()) {
    // return true;
    // }
    // }
    // }
    // }

    // return false;
    // }
}
