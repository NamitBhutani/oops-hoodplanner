package hoodplanner.ui;

import hoodplanner.models.Room;
import hoodplanner.controllers.RoomController;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import hoodplanner.models.Movable;

public class RoomLabel extends JLabel implements Movable {

    private Room room;
    private Point initialClick;
    private boolean resizing = false;
    private Color color = new Color(155, 40, 40, 255); // Initial color
    private final int SNAP_SIZE = 50;
    private final int RESIZE_MARGIN = 40;
    private MouseAdapter extraAdapter;
    private RoomController roomController;
    private Point lastValidPosition;
    private Dimension lastValidSize;

    public RoomLabel(Room room, RoomController roomController) {
        this.room = room;
        this.roomController = roomController;
        int width = (int) room.getLength();
        int height = (int) room.getWidth();

        setBounds((int) room.getX(), (int) room.getY(), width, height);
        setOpaque(true);
        setBackground(color);
        setPreferredSize(new Dimension(width, height));

        // init last valid position and size
        lastValidPosition = getLocation();
        lastValidSize = getSize();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (resizing) {
                    // Snap the size to the grid
                    Dimension size = getSize();
                    int width = (size.width / SNAP_SIZE) * SNAP_SIZE;
                    int height = (size.height / SNAP_SIZE) * SNAP_SIZE;

                    // Set snapped size
                    setPreferredSize(new Dimension(width, height));
                    setSize(width, height);

                    resizing = false;
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } else {
                    // Snap to 50x50 grid when not resizing
                    int X = getLocation().x;
                    int Y = getLocation().y;

                    X = (X / SNAP_SIZE) * SNAP_SIZE;
                    Y = (Y / SNAP_SIZE) * SNAP_SIZE;
                    setLocation(X, Y);
                }

                // save the position and size after a legal release
                lastValidPosition = getLocation();
                lastValidSize = getSize();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                int width = getWidth();
                int height = getHeight();

                if (initialClick.x >= width - RESIZE_MARGIN && initialClick.y >= height - RESIZE_MARGIN) {
                    resizing = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    getComponentAt(initialClick);
                }

                lastValidPosition = getLocation();
                lastValidSize = getSize();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (extraAdapter != null) {
                    extraAdapter.mouseClicked(e);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    // Resizing logic
                    int newWidth = e.getX();
                    int newHeight = e.getY();

                    setPreferredSize(new Dimension(newWidth, newHeight));
                    setSize(newWidth, newHeight);

                    // Check for overlap after resizing
                    if (roomController.isOverlappingAny(RoomLabel.this)) {
                        System.out.println("Overlap detected during resize! Reverting to last valid size.");
                        setSize(lastValidSize);
                    } else {
                        lastValidSize = getSize();
                    }

                } else {
                    move(e);

                    // Check for overlap after moving
                    if (roomController.isOverlappingAny(RoomLabel.this)) {
                        System.out.println("Overlap detected during move! Reverting to last valid position.");
                        setLocation(lastValidPosition);
                    } else {
                        lastValidPosition = getLocation();
                    }
                }
            }
        });
    }

    @Override
    public void move(MouseEvent e) {
        // Movement logic for RoomLabel
        int thisX = getLocation().x;
        int thisY = getLocation().y;

        int xMoved = e.getX() - initialClick.x;
        int yMoved = e.getY() - initialClick.y;

        int X = thisX + xMoved;
        int Y = thisY + yMoved;

        setLocation(X, Y);
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        room.setX(x);
        room.setY(y);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        room.setLength(width);
        room.setWidth(height);
    }

    public Room getRoom() {
        return room;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }

    public void addMouseClickListener(MouseAdapter adapter) {
        extraAdapter = adapter;
    }
}