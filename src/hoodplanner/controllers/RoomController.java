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

 
    public void addRoom(String name, RoomType type, double width, double length, LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel, Room referenceRoom, String position, String alignment) {

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
                x >= existing.getX() + existing.getLength() + buffer ||
                y + length + buffer <= existing.getY() ||
                y >= existing.getY() + existing.getWidth() + buffer);
    }

    public void syncAdjacentRoomDoors(Room room1) {
        // Iterate through all rooms

        for (FloorObject obj2 : floorPlan.getFloorObjects()) {
            if (!(obj2 instanceof Room) || room1 == obj2)
                continue;
            Room room2 = (Room) obj2;

            // Check if rooms are now adjacent
            if (areRoomsAdjacent(room1, room2)) {
                System.err.println("Rooms are adjacent: " + room1.getName() + " and " + room2.getName());
                syncAdjacentRoomWalls(room1, room2);
            }
        }

        // repaint all room labels
        repaintRooms();
    }

    public void repaintRooms() {
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
        // Compare north and south walls between room1 and room2
        if (areWallsAdjacent(room1.northWall, room2.southWall)) {
            System.out.println("Room 1 North and Room 2 South are adjacent");
            syncWallDoors(room1.northWall, room2.southWall);
        }
        if (areWallsAdjacent(room1.southWall, room2.northWall)) {
            System.out.println("Room 1 South and Room 2 North are adjacent");
            syncWallDoors(room1.southWall, room2.northWall);
        }

        // Compare east and west walls between room1 and room2
        if (areWallsAdjacent(room1.eastWall, room2.westWall)) {
            System.out.println("Room 1 East and Room 2 West are adjacent");
            syncWallDoors(room1.eastWall, room2.westWall);
        }
        if (areWallsAdjacent(room1.westWall, room2.eastWall)) {
            System.out.println("Room 1 West and Room 2 East are adjacent");
            syncWallDoors(room1.westWall, room2.eastWall);
        }
    }

    public boolean areWallsAdjacent(Wall wall1, Wall wall2) {
        // Check North-South adjacency
        if (wall1.getWidth() == 0 && wall2.getWidth() == 0) {

            if (wall1.getY() != wall2.getY()) {
                return false;
            }

            if (wall1.getX() < wall2.getX()) {
                if (wall2.getX() < wall1.getX() + wall1.getLength()) {
                    return true;
                }
            } else {
                if (wall1.getX() < wall2.getX() + wall2.getLength()) {
                    return true;
                }
            }
        }

        // Check East-West adjacency
        if (wall1.getLength() == 0 && wall2.getLength() == 0) {

            if (wall1.getX() != wall2.getX()) {
                return false;
            }

            if (wall1.getY() < wall2.getY()) {
                if (wall2.getY() < wall1.getY() + wall1.getWidth()) {
                    return true;
                }
            } else {
                if (wall1.getY() < wall2.getY() + wall2.getWidth()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void syncWallDoors(Wall wall1, Wall wall2) {
        // Copy doors from wall1 to wall2, adjusting positions

        // ArrayList<Door> doors = new ArrayList<>(wall2.getDoors());
        // for (Door door : doors) {
        // // Remove any door that lie inside the bounds of wall1

        // // North-South walls
        // if (wall1.getWidth() == 0 && wall2.getWidth() == 0) {
        // if (door.getDistanceFromStart() > wall1.getX() && door.getDistanceFromStart()
        // + door.getLength() < wall1.getX() + wall1.getLength()) {
        // wall2.removeDoor(door);
        // }
        // }

        // // East-West walls
        // if (wall1.getLength() == 0 && wall2.getLength() == 0) {
        // if (door.getDistanceFromStart() > wall1.getY() && door.getDistanceFromStart()
        // + door.getLength() < wall1.getY() + wall1.getWidth()) {
        // wall2.removeDoor(door);
        // }
        // }
        // }

        for (Door door : wall1.getDoors()) {

            // Create a new door on wall2 with mirrored position
            int pos = calculateMirroredPosition(wall1, wall2, door);
            if (pos == -1) {
                continue;
            }
            Door mirroredDoor = new Door(door.getLength(), pos);
            System.out.println("Mirrored door position on wall2: " + mirroredDoor.getDistanceFromStart());
            wall2.addDoor(mirroredDoor);
        }

        for (Door door : wall2.getDoors()) {
            int pos = calculateMirroredPosition(wall2, wall1, door);
            if (pos == -1) {
                continue;
            }
            Door mirroredDoor = new Door(door.getLength(),pos);
            System.out.println("Mirrored door position on wall1: " + mirroredDoor.getDistanceFromStart());
            wall1.addDoor(mirroredDoor);
        }

        wall1.simplyfyDoors();
        wall2.simplyfyDoors();
    }

    public int calculateMirroredPosition(Wall wall1, Wall wall2, Door door) {

        // If the walls are horizontal (North-South)
        if (wall1.getWidth() == 0 && wall2.getWidth() == 0) {
            if (wall2.getX() > wall1.getX() + door.distFromStart) {
                // Not possible to mirror
                return -1;
            } else {
                return (int) (wall1.getX() + door.distFromStart - wall2.getX());
            }
        }

        // If the walls are vertical (East-West)
        if (wall1.getLength() == 0 && wall2.getLength() == 0) {
            if (wall2.getY() > wall1.getY() + door.distFromStart) {
                // Not possible to mirror
                return -1;
            } else {
                return (int) (wall1.getY() + door.distFromStart - wall2.getY());
            }
        }

        return -1;
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
