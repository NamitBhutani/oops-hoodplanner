package hoodplanner.ui;

import hoodplanner.models.Furniture;
import hoodplanner.models.Room;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.util.ArrayList;
import javax.swing.*;

public class LeftPanel extends JPanel implements DropTargetListener {
    private Room targetRoom;
    private ArrayList<Furniture> availableFurniture;

    public LeftPanel() {
        setLayout(null);
        JPanel gridOverlay = new GridOverlayPanel();
        gridOverlay.setOpaque(false);  // Ensure overlay is transparent
        add(gridOverlay);
        setBackground(Color.DARK_GRAY);

        // Initialize the list of available furniture
        availableFurniture = new ArrayList<>();
        loadAvailableFurniture();

        // Enable drop target for the panel
        new DropTarget(this, this);
    }

    public void reset() {
        removeAll();
        JPanel gridOverlay = new GridOverlayPanel();
        gridOverlay.setOpaque(false);  // Ensure overlay is transparent
        add(gridOverlay);
        revalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw all rooms and their contained furniture
        for (Component comp : getComponents()) {
            if (comp instanceof RoomLabel roomLabel) {
                Room room = roomLabel.getObject();
                Rectangle bounds = roomLabel.getBounds();

                // Draw the room
                g.setColor(room.getType() != null ? room.getType().getColor() : Color.LIGHT_GRAY);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

                // Draw furniture in the room
                for (Furniture furniture : room.getContainedFurniture()) {
                    // Use the draw method to render the furniture image
                    furniture.draw(g, bounds.x + (int) furniture.getX(), bounds.y + (int) furniture.getY());
                }
            }
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        Point location = dtde.getLocation();
        targetRoom = findRoomAtLocation(location);
        if (targetRoom != null) {
            targetRoom.setHighlight(true);
            repaint();
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        Point location = dtde.getLocation();
        Room newTargetRoom = findRoomAtLocation(location);
        if (newTargetRoom != targetRoom) {
            if (targetRoom != null) {
                targetRoom.setHighlight(false);
            }
            targetRoom = newTargetRoom;
            if (targetRoom != null) {
                targetRoom.setHighlight(true);
            }
            repaint();
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {}

    @Override
    public void dragExit(DropTargetEvent dte) {
        if (targetRoom != null) {
            targetRoom.setHighlight(false);
            targetRoom = null;
            repaint();
        }
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable transferable = dtde.getTransferable();
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                String furnitureName = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                if (targetRoom != null) {
                    Point dropLocation = dtde.getLocation();
                    Point relativeLocation = convertToRoomCoordinates(dropLocation, targetRoom);

                    Furniture furniture = createFurnitureByName(furnitureName, relativeLocation);
                    if (furniture != null) {
                        targetRoom.addContainedObject(furniture);
                        JOptionPane.showMessageDialog(this,
                            furniture.getName() + " added to " + targetRoom.getName());
                    }

                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Drop target not found");
                }

                dtde.dropComplete(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            dtde.dropComplete(false);
        }
    }

    private Room findRoomAtLocation(Point location) {
        for (Component comp : getComponents()) {
            if (comp instanceof RoomLabel roomLabel) {
                Rectangle bounds = comp.getBounds();
                if (bounds.contains(location)) {
                    return roomLabel.getObject();
                }
            }
        }
        return null;
    }

    private Point convertToRoomCoordinates(Point panelLocation, Room room) {
        // Translate the panel-based coordinates to the room's local coordinate system
        int relativeX = (int) (panelLocation.x - room.getX());
        int relativeY = (int) (panelLocation.y - room.getY());
        return new Point(relativeX, relativeY);
    }

    private Furniture createFurnitureByName(String name, Point location) {
        // Match the name to the available furniture and return the corresponding Furniture object
        for (Furniture f : availableFurniture) {
            if (f.getName().equalsIgnoreCase(name)) {
                return new Furniture(f.getWidth(), f.getLength(), location.x, location.y, f.getName(), f.getImagePath());
            }
        }
        return null;
    }

    private void loadAvailableFurniture() {
        // Adding all available furniture items
        availableFurniture.add(new Furniture(5, 5, 50.0, 90.0, "Sofa", "src/hoodplanner/public/sofa(1).png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 90.0, "Bed", "src/hoodplanner/public/bed.png"));
        availableFurniture.add(new Furniture(5, 5, 50.0, 80.0, "Toilet", "src/hoodplanner/public/toilet.png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 80.0, "Sink", "src/hoodplanner/public/sink.png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 80.0, "Bath", "src/hoodplanner/public/bath.png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 80.0, "Couch", "src/hoodplanner/public/couch.png"));
    }
}
