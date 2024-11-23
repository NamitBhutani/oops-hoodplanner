package hoodplanner.ui;

import hoodplanner.models.Furniture;
import hoodplanner.models.Room;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.util.ArrayList;
import javax.swing.*;

public class LeftPanel extends JPanel implements DropTargetListener {
    private RoomLabel targetRoom;
    private ArrayList<Furniture> availableFurniture;

    public LeftPanel() {
        setLayout(null);
        JPanel gridOverlay = new GridOverlayPanel();
        gridOverlay.setOpaque(false); // Ensure overlay is transparent
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
        gridOverlay.setOpaque(false); // Ensure overlay is transparent
        add(gridOverlay);
        revalidate();
        repaint();
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

        RoomLabel newTargetRoom = findRoomAtLocation(location);
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
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

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

                    Room room = targetRoom.getRoom();
                    Point dropLocation = dtde.getLocation();
                    Point relativeLocation = convertToRoomCoordinates(dropLocation, room);

                    Furniture furniture = createFurnitureByName(furnitureName, relativeLocation);
                    // System.out.println("Furniture: " + furniture);
                    if (furniture != null) {
                        boolean result = room.addContainedObject(furniture);
                        if (!result) {
                            JOptionPane.showMessageDialog(this, "Furniture overlaps with existing furniture");
                        } else {
                            targetRoom.setHighlight(false);
                            repaint();
                            JOptionPane.showMessageDialog(this,
                                    furniture.getName() + " added to " + room.getName());
                        }
                    }

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

    private RoomLabel findRoomAtLocation(Point location) {
        for (Component comp : getComponents()) {
            if (comp instanceof RoomLabel roomLabel) {
                Rectangle bounds = comp.getBounds();
                if (bounds.contains(location)) {
                    return roomLabel;
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
        // Match the name to the available furniture and return the corresponding
        // Furniture object
        for (Furniture f : availableFurniture) {
            if (f.getName().equalsIgnoreCase(name)) {
                return new Furniture(f.getWidth(), f.getLength(), location.x, location.y, f.getName(),
                        f.getImagePath());
            }
        }
        return null;
    }

    private void loadAvailableFurniture() {
        // Adding all available furniture items
        availableFurniture.add(new Furniture(100, 100, 50.0, 90.0, "Sofa", "src/hoodplanner/public/sofa(1).png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 90.0, "Bed", "src/hoodplanner/public/bed.png"));
        availableFurniture.add(new Furniture(100, 100, 50.0, 80.0, "Toilet", "src/hoodplanner/public/toilet.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Sink", "src/hoodplanner/public/sink.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Bath", "src/hoodplanner/public/bath.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Couch", "src/hoodplanner/public/couch.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Table", "src/hoodplanner/public/table.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Armchair", "src/hoodplanner/public/armchair.png"));

    }
}
