package hoodplanner.controllers;

import hoodplanner.models.FloorPlan;
import hoodplanner.models.Wall;
import hoodplanner.models.Window;
import hoodplanner.models.Door;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.RightPanel;
import hoodplanner.ui.WallLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class WallController {
    private final FloorPlan floorPlan;
    private final List<WallLabel> wallLabels;

    public WallController(FloorPlan floorPlan) {
        this.floorPlan = floorPlan;
        this.wallLabels = new ArrayList<>();
    }

    public void createWallLabel(Wall wall, LeftPanel leftPanel, RightPanel rightPanel) {
        WallLabel wallLabel = new WallLabel(wall);
        wallLabels.add(wallLabel); // Add to list
        leftPanel.add(wallLabel);
        leftPanel.revalidate();
        leftPanel.repaint();

        wallLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rightPanel.setSelectedWall(wallLabel);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                wall.setXPosition(e.getX());
                wall.setYPosition(e.getY());
                wallLabel.updatePosition();
                rightPanel.setSelectedWall(wallLabel);
            }
        });
    }

    public void deleteWallLabel(WallLabel wallLabel, LeftPanel leftPanel) {
        wallLabels.remove(wallLabel); // Remove from list
        leftPanel.remove(wallLabel);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public void addWall(double length, double thickness, LeftPanel leftPanel, RightPanel rightPanel) {
        Wall wall = new Wall(length, thickness);
        floorPlan.addWall(wall);
        createWallLabel(wall, leftPanel, rightPanel);
    }

    public void deleteWall(Wall wall, LeftPanel leftPanel) {
        // Remove associated windows and doors
        List<Window> windows = wall.getWindows(); // Get the list of windows
        List<Door> doors = wall.getDoors(); // Get the list of doors

        // Remove all windows associated with the wall
        for (Window window : windows) {
            floorPlan.removeWindow(window); // Remove from floor plan
        }

        // Remove all doors associated with the wall
        for (Door door : doors) {
            floorPlan.removeDoor(door); // Remove from floor plan
        }

        // Now remove the wall itself
        floorPlan.removeWall(wall);

        // Find the WallLabel associated with the Wall
        WallLabel wallLabelToDelete = null;
        for (WallLabel label : wallLabels) {
            if (label.getWall().equals(wall)) {
                wallLabelToDelete = label;
                break;
            }
        }

        // Delete the WallLabel if found
        if (wallLabelToDelete != null) {
            deleteWallLabel(wallLabelToDelete, leftPanel);
        }
    }
}
