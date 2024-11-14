package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Room;
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
        
        // Half thickness for shared walls
        int halfThickness = wallThickness / 2;

        // Check for adjacent rooms
        // boolean hasTopNeighbor = roomController.hasNeighbor(room, "north");
        // boolean hasBottomNeighbor = roomController.hasNeighbor(room, "south");
        // boolean hasLeftNeighbor = roomController.hasNeighbor(room, "east");
        // boolean hasRightNeighbor = roomController.hasNeighbor(room, "west");

        boolean hasTopNeighbor = false;
        boolean hasBottomNeighbor = false;
        boolean hasLeftNeighbor = false;
        boolean hasRightNeighbor = false;

        // Draw top wall
        if (hasTopNeighbor) {
            g.fillRect(0, 0, width, halfThickness); // Draw only half-thickness wall
        } else {
            g.fillRect(0, 0, width, wallThickness); // Draw full-thickness wall
        }

        // Draw bottom wall
        if (hasBottomNeighbor) {
            g.fillRect(0, height - halfThickness, width, halfThickness);
        } else {
            g.fillRect(0, height - wallThickness, width, wallThickness);
        }

        // Draw left wall
        if (hasLeftNeighbor) {
            g.fillRect(0, 0, halfThickness, height);
        } else {
            g.fillRect(0, 0, wallThickness, height);
        }

        // Draw right wall
        if (hasRightNeighbor) {
            g.fillRect(width - halfThickness, 0, halfThickness, height);
        } else {
            g.fillRect(width - wallThickness, 0, wallThickness, height);
        }
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
