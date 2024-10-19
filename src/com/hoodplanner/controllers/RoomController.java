package hoodplanner.controllers;

import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import hoodplanner.ui.RoomLabel;

public class RoomController {
    private final FloorPlan floorPlan;

    public RoomController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
    }

    public void addRoom(int width, int height, RoomLabel roomPanel) {
        Room room = new Room(width, height);
        floorPlan.addRoom(room);
        // roomPanel.addRoomToView(room);
    }

    // Other methods for room movement, resizing, etc.
}