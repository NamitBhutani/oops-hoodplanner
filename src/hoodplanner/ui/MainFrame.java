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

    @SuppressWarnings("CallToPrintStackTrace")
    public MainFrame(List<FloorObjectController<?, ?>> controllers, FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        this.roomController = new RoomController(floorPlan);
        this.leftPanel = new LeftPanel();
        this.rightPanel = new RightPanel<>(leftPanel, roomController);

        TopMenuBar menuBar = new TopMenuBar(rightPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.8);
        // Loop through each controller and add objects to the left panel
        for (FloorObjectController<?, ?> controller : controllers) {
            for (Object label : controller.getObjectLabels()) {
                if (label instanceof JComponent jComponent) {
                    leftPanel.add(jComponent);
                }
            }
        }

        menuBar.getItem("Add Room").addActionListener(e -> {
            AddRoomPopup.showAddRoomDialog(roomController, leftPanel, rightPanel);
            // roomController.addRoom(200, 200, leftPanel, (RightPanel<Room, RoomLabel>) rightPanel);
            // leftPanel.revalidate();
            // leftPanel.repaint();
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
                    this.floorPlan.saveToFile(filePath);
                    setTitle(this.floorPlan.displayName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to save floor plan", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        menuBar.getItem("Save").addActionListener(e -> {
            if (this.floorPlan.saveFilePath == null) {
                menuBar.getItem("Save As").doClick();
            } else {
                try {
                    this.floorPlan.saveToFile(this.floorPlan.saveFilePath);
                    setTitle(this.floorPlan.displayName());
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
        this.floorPlan = floorPlan;
        leftPanel.reset();
        roomController = new RoomController(floorPlan);
        
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
