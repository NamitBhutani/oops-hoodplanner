package hoodplanner.ui;

import hoodplanner.controllers.FloorObjectController;
import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorObject;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Furniture;
import hoodplanner.models.Room;
import javax.swing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final RoomController roomController;
    private final LeftPanel leftPanel;
    private final RightPanel<Room, RoomLabel> rightPanel;
    private final FloorPlan floorPlan;

    public MainFrame(List<FloorObjectController<?, ?>> controllers, FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        this.roomController = new RoomController(floorPlan);
        this.leftPanel = new LeftPanel();
        this.rightPanel = new RightPanel<>(leftPanel, roomController);

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
        menuBar.getItem("Load").addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    FloorPlan loadedFloorPlan = FloorPlan.loadFromFile(fileChooser.getSelectedFile().getPath());
                    loadFloorPlan(loadedFloorPlan);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to load floor plan", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        menuBar.getItem("Save").addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    floorPlan.saveToFile(fileChooser.getSelectedFile().getPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to save floor plan", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setJMenuBar(menuBar);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        getContentPane().add(splitPane);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void loadFloorPlan(FloorPlan floorPlan) {
        roomController.setFloorPlan(floorPlan);
        leftPanel.removeAll();

        List<FloorObject> floorObjectsCopy = new ArrayList<>(floorPlan.getFloorObjects());

        for (FloorObject floorObject : floorObjectsCopy) {
            System.out.println(floorObject);
            if (floorObject instanceof Room) {
                System.out.println("Adding room" + floorObject);
                Room room = (Room) floorObject;
                roomController.addRoom(room.getWidth(), room.getHeight(), leftPanel, rightPanel);
            } else if (floorObject instanceof Furniture) {
            }
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

}
