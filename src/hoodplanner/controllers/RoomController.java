package hoodplanner.controllers;

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
        Room room = new Room(width, height);
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
}
