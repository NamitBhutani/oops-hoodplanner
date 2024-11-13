package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Room;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class RoomLabel extends ObjectLabel<Room> {

    private final RoomController roomController;

    public RoomLabel(Room room, RoomController roomController) {
        super(room);
        this.roomController = roomController;
        setColor(Color.WHITE);
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
