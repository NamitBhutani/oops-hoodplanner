package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(RoomController roomController, FloorPlan floorPlan) {
        LeftPanel leftPanel = new LeftPanel();

        RightPanel rightPanel = new RightPanel(leftPanel);
        TopMenuBar menuBar = new TopMenuBar(rightPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.8);

        // Loop through all rooms in the floor plan and add them to the left panel
        for (Room room : floorPlan.getRooms()) {
            roomController.createRoomLabel(room, leftPanel, rightPanel);
        }

        // Action to add new rooms
        menuBar.getItem("Add Room").addActionListener(e -> {
            roomController.addRoom(200, 200, leftPanel, rightPanel);
        });

        setJMenuBar(menuBar);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        // splitPane.setEnabled(false);

        getContentPane().add(splitPane);

        // Load the image and set it as the title bar icon
        ImageIcon icon = new ImageIcon("src\\com\\hoodplanner\\public\\home.png");
        setIconImage(icon.getImage());
        setTitle("Hood Planner 2D - " + floorPlan.name);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
