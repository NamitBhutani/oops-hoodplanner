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
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame {
    private RoomController roomController;
    private final LeftPanel leftPanel;
    private final RightPanel<Room, RoomLabel> rightPanel;
    private FloorPlan floorPlan;

    public MainFrame(List<FloorObjectController<?, ?>> controllers, FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        this.roomController = new RoomController(floorPlan);
        this.leftPanel = new LeftPanel();
        this.rightPanel = new RightPanel<>(leftPanel, roomController);

        // Load furniture into the RightPanel
        loadAvailableFurniture();

        TopMenuBar menuBar = new TopMenuBar(rightPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(1);

        // Add all object labels to the left panel
        for (FloorObjectController<?, ?> controller : controllers) {
            for (Object label : controller.getObjectLabels()) {
                if (label instanceof JComponent jComponent) {
                    leftPanel.add(jComponent);
                }
            }
        }

        // Menu Actions
        menuBar.getItem("Add Room").addActionListener(e -> {
            AddRoomPopup.showAddRoomDialog(roomController, leftPanel, rightPanel);
        });

        menuBar.getItem("Load").addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Hood Files (*.hood)", "hood"));

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (!fileChooser.getSelectedFile().getName().endsWith(".hood")) {
                    JOptionPane.showMessageDialog(null, "Invalid file type. Please select a .hood file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    FloorPlan loadedFloorPlan = FloorPlan.loadFromFile(fileChooser.getSelectedFile().getPath());
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

        menuBar.getItem("Save As").addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Hood Files (*.hood)", "hood"));

            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                if (!filePath.endsWith(".hood")) {
                    filePath += ".hood"; // Automatically append the .hood extension
                }

                try {
                    floorPlan.saveToFile(filePath);
                    setTitle(floorPlan.displayName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to save floor plan", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        menuBar.getItem("Save").addActionListener(e -> {
            if (floorPlan.saveFilePath == null) {
                menuBar.getItem("Save As").doClick();
            } else {
                try {
                    floorPlan.saveToFile(floorPlan.saveFilePath);
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

    public void loadAvailableFurniture() {
        // Load available furniture into the RightPanel
        rightPanel.loadAvailableFurniture();
    }

    public void loadFloorPlan(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        leftPanel.reset();
        roomController = new RoomController(floorPlan);

        setTitle(floorPlan.displayName());
        for (FloorObject floorObject : floorPlan.getFloorObjects()) {
            if (floorObject instanceof Room room) {
                RoomLabel roomLabel = new RoomLabel(room, roomController);
                roomController.createObjectLabel(room, roomLabel, leftPanel, rightPanel);
            } else if (floorObject instanceof Furniture furniture) {
                // Add furniture to the RightPanel and associated rooms
                rightPanel.addFurnitureToRoom(furniture);
            }
        }
        leftPanel.revalidate();
        leftPanel.repaint();
        // RightPanel<Room, RoomLabel> rightPanel = new RightPanel<>(leftPanel,
        // roomController);
        // rightPanel.loadAvailableFurniture();
        this.rightPanel.setController(roomController);
        this.rightPanel.loadAvailableFurniture();
    }
}
