package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Room;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;

public class RoomLabel extends ObjectLabel<Room> {

    private final RoomController roomController;
    private boolean isHighlighted = false; // Track if the room is highlighted

    public RoomLabel(Room room, RoomController roomController) {
        super(room);
        this.roomController = roomController;
        if (room.getType() != null) {
            setColor(room.getType().getColor());
        } else {
            setColor(Color.WHITE);
        }
    }

    // Highlight the room
    public void setHighlight(boolean highlight) {
        this.isHighlighted = highlight;
        if (highlight) {
            setBackground(Color.YELLOW); // Highlight with a yellow color
            setBorder(BorderFactory.createLineBorder(Color.RED, 3)); // Set a red border for highlight
        } else {
            setBackground(Color.WHITE); // Reset to white background when unhighlighted
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Default border
        }
    }

    // Method to move the room label with mouse drag
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

    // Check if this room label is overlapping with any other room labels
    @Override
    public boolean isOverlappingAny() {
        return roomController.isOverlappingAny(this);
    }
}
