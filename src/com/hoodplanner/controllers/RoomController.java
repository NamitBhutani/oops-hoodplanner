package hoodplanner.controllers;

import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.RightPanel;
import hoodplanner.ui.RoomLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoomController {
    private final FloorPlan floorPlan;

    public RoomController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;


    }

    public void createRoomLabel(Room room, LeftPanel leftPanel, RightPanel rightPanel) {
        RoomLabel roomLabel = new RoomLabel(room);
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

    public void addRoom(int width, int height, LeftPanel leftPanel, RightPanel rightPanel) {
        Room room = new Room(width, height);
        floorPlan.addRoom(room);

        createRoomLabel(room, leftPanel, rightPanel);
    }

    // Other methods for room movement, resizing, etc.
}