package hoodplanner.ui;

import hoodplanner.models.FloorObject;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public abstract class ObjectLabel<T extends FloorObject> extends JLabel {

    private final T object;
    Point initialClick;
    private boolean resizing = false;
    private Color color = new Color(155, 40, 40, 255); // Default color
    private final int SNAP_SIZE = 25;
    private final int RESIZE_MARGIN = 40;
    private MouseAdapter extraAdapter;
    private Point lastValidPosition;
    private Dimension lastValidSize;

    public ObjectLabel(T object) {
        this.object = object;
        int width = (int) object.getLength();
        int height = (int) object.getWidth();

        setBounds((int) object.getX(), (int) object.getY(), width, height);
        setOpaque(true);
        setBackground(color);
        setPreferredSize(new Dimension(width, height));

        lastValidPosition = getLocation();
        lastValidSize = getSize();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (resizing) {
                    // Snap the size to the grid
                    Dimension size = getSize();
                    int width = Math.round((float) size.width / SNAP_SIZE) * SNAP_SIZE;
                    int height = Math.round((float) size.height / SNAP_SIZE) * SNAP_SIZE;

                    setPreferredSize(new Dimension(width, height));
                    setSize(width, height);

                    if (isOverlappingAny()) {
                        showOverlapDialog(true);
                    } else {
                        lastValidSize = getSize();
                    }

                    resizing = false;
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } else {
                    int X = getLocation().x;
                    int Y = getLocation().y;

                    X = (X / SNAP_SIZE) * SNAP_SIZE;
                    Y = (Y / SNAP_SIZE) * SNAP_SIZE;
                    setLocation(X, Y);

                    if (isOverlappingAny()) {
                        showOverlapDialog(false);
                    } else {
                        lastValidPosition = getLocation();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                int width = getWidth();
                int height = getHeight();

                if (initialClick.x >= width - RESIZE_MARGIN && initialClick.y >= height - RESIZE_MARGIN) {
                    resizing = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
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
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    int newWidth = e.getX();
                    int newHeight = e.getY();

                    setPreferredSize(new Dimension(newWidth, newHeight));
                    setSize(newWidth, newHeight);
                } else {
                    move(e);
                }
            }
        });
    }

    private void showOverlapDialog(boolean wasResizing) {
        JOptionPane.showMessageDialog(
                this,
                "Object placement overlaps with another object. Please adjust.",
                "Overlap Detected",
                JOptionPane.WARNING_MESSAGE);

        if (wasResizing) {
            setSize(lastValidSize);
        } else {
            setLocation(lastValidPosition);
        }
    }

    public abstract void move(MouseEvent e);

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        object.setX(x);
        object.setY(y);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        object.setLength(width);
        object.setWidth(height);
    }

    public T getObject() {
        return object;
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

    public abstract boolean isOverlappingAny();
}