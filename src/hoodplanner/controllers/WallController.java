package hoodplanner.controllers;

import hoodplanner.models.FloorPlan;
import hoodplanner.models.Wall;
import hoodplanner.models.Room;

public class WallController {
    private final FloorPlan floorPlan;

    public WallController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void handleRoomDrop(Room room) {
        // Draw walls based on adjacent rooms
        createWallsForRoom(room);

        // Add listeners for room resizing
        room.addResizeListener(this::resizeAssociatedWalls);
    }

    private void addWallsOnAllSides(Room room) {
        // Create walls on all four edges of the room
        Wall wallTop = new Wall(room.getWidth(), 10, room.getX(), room.getY() - 10);
        Wall wallBottom = new Wall(room.getWidth(), 10, room.getX(), room.getY() + room.getHeight());
        Wall wallLeft = new Wall(10, room.getHeight(), room.getX() - 10, room.getY());
        Wall wallRight = new Wall(10, room.getHeight(), room.getX() + room.getWidth(), room.getY());

        floorPlan.addWall(wallTop);
        floorPlan.addWall(wallBottom);
        floorPlan.addWall(wallLeft);
        floorPlan.addWall(wallRight);
    }

    private void resizeAssociatedWalls(Room room) {
        // Resize walls based on the new dimensions of the room
        for (Wall wall : floorPlan.getWallsConnectedToRoom(room)) {
            // Update wall length based on the new room size
            if (wall.isVertical()) {
                wall.setLength(room.getHeight());
                wall.setThickness(10); // Assuming thickness remains constant
            } else {
                wall.setLength(room.getWidth());
                wall.setThickness(10); // Assuming thickness remains constant
            }
            // Adjust wall position if necessary based on new room position
            adjustWallPosition(wall, room);
        }

        // Check for new adjacencies after resizing
        checkNewAdjacencies(room);
    }

    private void adjustWallPosition(Wall wall, Room room) {
        // Check if the wall is horizontal
        if (wall.isHorizontal()) {
            // If the wall is at the top of the room
            if (wall.getY() + wall.getThickness() == room.getY()) {
                // Update wall position for the top wall
                wall.setX(room.getX());
                wall.setY(room.getY() - wall.getThickness());
            }
            // If the wall is at the bottom of the room
            else if (wall.getY() == room.getY() + room.getHeight()) {
                wall.setX(room.getX());
                wall.setY(room.getY() + room.getHeight());
            }
        }
        // Check if the wall is vertical
        else {
            // If the wall is on the left side of the room
            if (wall.getX() + wall.getThickness() == room.getX()) {
                // Update wall position for the left wall
                wall.setX(room.getX() - wall.getThickness());
                wall.setY(room.getY());
            }
            // If the wall is on the right side of the room
            else if (wall.getX() == room.getX() + room.getWidth()) {
                wall.setX(room.getX() + room.getWidth());
                wall.setY(room.getY());
            }
        }
    }


    private void checkNewAdjacencies(Room room) {
        List<Room> adjacentRooms = floorPlan.getAdjacentRooms(room);

        // Check if the room has become adjacent to new rooms
        for (Room adjacentRoom : adjacentRooms) {
           // Ensure wall fits only the remaining free part if rooms are partially adjacent
            adjustWallForPartialAdjacency(room, adjacentRoom);
        }

        // If the room is no longer adjacent to any other room, ensure proper wall creation
        if (adjacentRooms.isEmpty()) {
            addWallsOnAllSides(room);
        }
    }
}
