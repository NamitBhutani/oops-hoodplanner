package hoodplanner.controllers;

import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.RightPanel;
import hoodplanner.ui.RoomLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class RoomController {
    private final FloorPlan floorPlan;
    private final List<RoomLabel> roomLabels;

    public RoomController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        this.roomLabels = new ArrayList<>();
    }

    public void createRoomLabel(Room room, LeftPanel leftPanel, RightPanel rightPanel) {
        RoomLabel roomLabel = new RoomLabel(room, this);
        roomLabels.add(roomLabel); // Add to list
        leftPanel.add(roomLabel);
        leftPanel.revalidate();
        leftPanel.repaint();

        roomLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rightPanel.setSelectedRoom(roomLabel);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                rightPanel.setSelectedRoom(roomLabel);
            }
        });
    }

    public void deleteRoomLabel(RoomLabel roomLabel, LeftPanel leftPanel) {
        roomLabels.remove(roomLabel); // Remove from list
        leftPanel.remove(roomLabel);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public void addRoom(int width, int height, LeftPanel leftPanel, RightPanel rightPanel) {
        Room room = new Room(width, height);
        floorPlan.addRoom(room);

        createRoomLabel(room, leftPanel, rightPanel);
    }

    public void deleteRoom(Room room, LeftPanel leftPanel) {
        floorPlan.removeRoom(room);

        // Find the RoomLabel associated with the Room
        RoomLabel roomLabelToDelete = null;
        for (RoomLabel label : roomLabels) {
            if (label.getRoom().equals(room)) {
                roomLabelToDelete = label;
                break;
            }
        }

        // Delete the RoomLabel if found
        if (roomLabelToDelete != null) {
            deleteRoomLabel(roomLabelToDelete, leftPanel);
        }
    }

    public boolean isOverlappingAny(RoomLabel roomLabel) {
        for (RoomLabel otherLabel : roomLabels) {
            if (roomLabel != otherLabel && roomLabel.getRoom().checkOverlap(otherLabel.getRoom())) {
                return true; // Overlap detected
            }
        }
        return false; // No overlap
    }
}
