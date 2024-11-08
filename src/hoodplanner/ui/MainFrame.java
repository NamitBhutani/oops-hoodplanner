package hoodplanner.ui;

import hoodplanner.controllers.FloorObjectController;
import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Room;
import javax.swing.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final RoomController roomController;

    public MainFrame(List<FloorObjectController<?, ?>> controllers, FloorPlan floorPlan) {
        this.roomController = new RoomController(floorPlan);
        LeftPanel leftPanel = new LeftPanel();

        RightPanel<Room, RoomLabel> rightPanel = new RightPanel<>(leftPanel, roomController);
        TopMenuBar menuBar = new TopMenuBar(rightPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.8);
        // Loop through each controller and add objects to the left panel
        for (FloorObjectController<?, ?> controller : controllers) {
            for (Object label : controller.getObjectLabels()) {
                if (label instanceof JComponent) {
                    leftPanel.add((JComponent) label);
                }
            }
        }

        menuBar.getItem("Add Room").addActionListener(e -> {
            roomController.addRoom(200, 200, leftPanel, (RightPanel<Room, RoomLabel>) rightPanel);
            leftPanel.revalidate();
            leftPanel.repaint();
        });

        setJMenuBar(menuBar);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        getContentPane().add(splitPane);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
