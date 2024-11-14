package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Door;
import hoodplanner.models.Room;
import hoodplanner.models.Wall;
import hoodplanner.models.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
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
