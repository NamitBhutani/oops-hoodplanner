package hoodplanner.ui;

import hoodplanner.controllers.FloorObjectController;
import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorObject;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Furniture;
import hoodplanner.models.Room;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

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
                    System.out.println(loadedFloorPlan.name);
                    
                    loadFloorPlan(loadedFloorPlan);
                    leftPanel.revalidate();
                    leftPanel.repaint();
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
                    setTitle(floorPlan.displayName());
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

        setTitle(floorPlan.displayName());
        getContentPane().add(splitPane);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void loadFloorPlan(FloorPlan floorPlan) {
        roomController.setFloorPlan(floorPlan);
        leftPanel.removeAll();
        setTitle(floorPlan.displayName());
        for (FloorObject floorObject : floorPlan.getFloorObjects()) {
            if (floorObject instanceof Room room) {
                RoomLabel roomLabel = new RoomLabel(room, roomController);
                roomController.createObjectLabel(room, roomLabel, leftPanel, (RightPanel<Room, RoomLabel>) rightPanel);
            } else if (floorObject instanceof Furniture) {
                // Handle loading of Furniture objects
            }
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

}
