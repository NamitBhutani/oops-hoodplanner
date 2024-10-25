package hoodplanner.ui;

import hoodplanner.models.Room;
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
    private Color color = new Color(155, 40, 40, 255); // Keep initial color with opacity, but no changes later
    private final int SNAP_SIZE = 50;
    private final int RESIZE_MARGIN = 40;
    private MouseAdapter extraAdapter;

    public RoomLabel(Room room) {
        this.room = room;

        int width = (int) room.getLength();
        int height = (int) room.getWidth();

        setBounds((int) room.getX(), (int) room.getY(), width, height);
        setOpaque(true);
        setBackground(color); // Use initial color without modifying opacity during interactions
        setPreferredSize(new Dimension(width, height));

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
            }

            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                int width = getWidth();
                int height = getHeight();

                // Check if the click is within the resize margin (bottom-right corner)
                if (initialClick.x >= width - RESIZE_MARGIN && initialClick.y >= height - RESIZE_MARGIN) {
                    resizing = true;
                    System.out.println("Resizing");
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    getComponentAt(initialClick);
                }
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
            public void mouseMoved(MouseEvent e) {
                int width = getWidth();
                int height = getHeight();
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

                // Check if the mouse is within the resize margin (bottom-right corner)
                if (e.getX() >= width - RESIZE_MARGIN && e.getY() >= height - RESIZE_MARGIN) {
                    System.out.println("Resize cursor");
                } else {
                    // setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    // Resizing logic
                    int newWidth = e.getX();
                    int newHeight = e.getY();

                    // Set new size during drag
                    setPreferredSize(new Dimension(newWidth, newHeight));
                    setSize(newWidth, newHeight);
                    setBackground(color); // Ensure the background color remains the same without changing opacity
                } else {
                    move(e); // Invoke move if Movable
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