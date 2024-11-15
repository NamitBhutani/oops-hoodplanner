package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Door;
import hoodplanner.models.Room;
import hoodplanner.models.Wall;
import hoodplanner.models.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class RoomLabel extends ObjectLabel<Room> {

    private final Room room;
    private final RoomController roomController;
    private final int wallThickness = 4; // Adjust thickness as needed

    public RoomLabel(Room room, RoomController roomController) {
        super(room);
        this.room = room;
        this.roomController = roomController;
        if (room.getType() != null) {
            setColor(room.getType().getColor());
        } else {
            setColor(Color.WHITE);
        }

        // Add padding to account for wall thickness
        setBorder(new EmptyBorder(wallThickness, wallThickness, wallThickness, wallThickness));
        // Add mouse listener for right-click detection
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    handleRightClick(e);
                }
            }
        });
    }

    private void handleRightClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int width = getWidth();
        int height = getHeight();
        
        Wall clickedWall = null;
        int distanceFromStart = 0;
        
        // Determine which wall was clicked and calculate distance from start
        if (y <= wallThickness) {
            clickedWall = room.northWall;  // Top wall
            distanceFromStart = x;  // Horizontal distance from left to right
        } else if (y >= height - wallThickness) {
            clickedWall = room.southWall;  // Bottom wall
            distanceFromStart = x;  // Horizontal distance from left to right
        } else if (x <= wallThickness) {
            clickedWall = room.westWall;   // Left wall
            distanceFromStart = y;  // Vertical distance from top to bottom
        } else if (x >= width - wallThickness) {
            clickedWall = room.eastWall;   // Right wall
            distanceFromStart = y;  // Vertical distance from top to bottom
        }


        distanceFromStart = distanceFromStart / 25 * 25; // Round to nearest 25

        if (clickedWall != null) {
            openWallOptionsDialog(clickedWall, distanceFromStart);
        }
    }

    private void openWallOptionsDialog(Wall wall, int distanceFromStart) {
        // Show a dialog with options to add a door or window
        String[] options = {"Add Door", "Add Window"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an option:",
                "Wall Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            addDoorDialog(wall, distanceFromStart);
        } else if (choice == 1) {
            addWindowDialog(wall, distanceFromStart);
        }
    }

    private void addDoorDialog(Wall wall, int distanceFromStart) {
        // Show input dialog for door length
        String lengthStr = JOptionPane.showInputDialog(this, "Enter door length:");

        try {
            int length = Integer.parseInt(lengthStr);
            Door door = new Door(length, distanceFromStart);

            wall.addDoor(door); // Add the door to the selected wall
            repaint(); // Repaint to show the added door
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for length.");
        }
    }

    private void addWindowDialog(Wall wall, int distanceFromStart) {
        // Show input dialog for window length
        String lengthStr = JOptionPane.showInputDialog(this, "Enter window length:");

        try {
            int length = Integer.parseInt(lengthStr);
            Window window = new Window(length, distanceFromStart);

            wall.addWindow(window); // Add the window to the selected wall
            repaint(); // Repaint to show the added window
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for length.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw walls
        g.setColor(Color.WHITE);  // Wall color
        int width = getWidth();
        int height = getHeight();
        
        // Check for adjacent rooms
        // boolean hasTopNeighbor = roomController.hasNeighbor(room, "north");
        // boolean hasBottomNeighbor = roomController.hasNeighbor(room, "south");
        // boolean hasLeftNeighbor = roomController.hasNeighbor(room, "east");
        // boolean hasRightNeighbor = roomController.hasNeighbor(room, "west");

        boolean hasTopNeighbor = false;
        boolean hasBottomNeighbor = false;
        boolean hasLeftNeighbor = false;
        boolean hasRightNeighbor = false;

        drawWallWithDoorsAndWindows(g, room.northWall, 0, 0, width, wallThickness, hasTopNeighbor, "horizontal");
        drawWallWithDoorsAndWindows(g, room.southWall, 0, height - wallThickness, width, wallThickness, hasBottomNeighbor, "horizontal");
        drawWallWithDoorsAndWindows(g, room.westWall, 0, 0, height, wallThickness, hasLeftNeighbor, "vertical");
        drawWallWithDoorsAndWindows(g, room.eastWall, width - wallThickness, 0, height, wallThickness, hasRightNeighbor, "vertical");
    }

    // Helper method to draw a wall with doors and windows
    private void drawWallWithDoorsAndWindows(Graphics g, Wall wall, int x, int y, int length, int thickness, boolean hasNeighbor, String orientation) {
        int halfThickness = thickness / 2;
        int actualThickness = hasNeighbor ? halfThickness : thickness;
    
        // Sort doors and windows by distance from start
        int currentPos = 0;
    
        // Draw wall segments, skipping parts for doors
        for (Door door : wall.getDoors()) {
            int doorStart = door.getDistanceFromStart();
            int doorEnd = doorStart + door.getLength();
    
            // Draw wall segment up to the start of the door
            if (currentPos < doorStart) {
                if (orientation.equals("horizontal")) {
                    g.fillRect(x + currentPos, y, doorStart - currentPos, actualThickness);
                } else {
                    g.fillRect(x, y + currentPos, actualThickness, doorStart - currentPos);
                }
            }
            
            // Skip the door segment
            currentPos = doorEnd;
        }
    
        // Draw the remaining wall segment after the last door, if any
        if (currentPos < length) {
            if (orientation.equals("horizontal")) {
                g.fillRect(x + currentPos, y, length - currentPos, actualThickness);
            } else {
                System.out.println("x: " + x + " y: " + y + " currentPos: " + currentPos + " actualThickness: " + actualThickness + " length: " + length + " currentPos: " + currentPos);
                g.fillRect(x, y + currentPos,  actualThickness, length - currentPos);
            }
        }
    
        // Draw dashed lines for windows
        g.setColor(Color.DARK_GRAY); // Set color for windows
        int dashLength = 5;          // Length of each dash
        int spaceLength = 3;         // Space between dashes
    
        for (Window window : wall.getWindows()) {
            int windowStart = window.getDistanceFromStart();
            int windowEnd = windowStart + window.getLength();
    
            // Draw dashes along the window segment
            if (orientation.equals("horizontal")) {
                for (int i = windowStart; i < windowEnd; i += dashLength + spaceLength) {
                    g.fillRect(x + i, y, Math.min(dashLength, windowEnd - i), actualThickness);
                }
            } else {
                for (int i = windowStart; i < windowEnd; i += dashLength + spaceLength) {
                    g.fillRect(x, y + i, actualThickness, Math.min(dashLength, windowEnd - i));
                }
            }
        }
        g.setColor(Color.WHITE); // Reset color for walls
    }
    
    @Override
    public void move(MouseEvent e) {
        int thisX = getLocation().x;
        int thisY = getLocation().y;

        int xMoved = e.getX() - initialClick.x;
        int yMoved = e.getY() - initialClick.y;

        int X = thisX + xMoved;
        int Y = thisY + yMoved;

        setLocation(X, Y);
    }

    @Override
    public boolean isOverlappingAny() {
        return roomController.isOverlappingAny(this);
    }
}
