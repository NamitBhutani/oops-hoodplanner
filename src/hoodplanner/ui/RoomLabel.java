package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Door;
import hoodplanner.models.FloorObject;
import hoodplanner.models.FloorPlan;
import hoodplanner.models.Furniture;
import hoodplanner.models.Room;
import hoodplanner.models.RoomType;
import hoodplanner.models.Wall;
import hoodplanner.models.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


public class RoomLabel extends ObjectLabel<Room> {

    private final Room room;
    private final RoomController roomController;
    private boolean isHighlighted = false; // Track if the room is highlighted
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

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    roomController.syncAdjacentRoomDoors(room);
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
            clickedWall = room.northWall; // Top wall
            distanceFromStart = x; // Horizontal distance from left to right
        } else if (y >= height - wallThickness) {
            clickedWall = room.southWall; // Bottom wall
            distanceFromStart = x; // Horizontal distance from left to right
        } else if (x <= wallThickness) {
            clickedWall = room.westWall; // Left wall
            distanceFromStart = y; // Vertical distance from top to bottom
        } else if (x >= width - wallThickness) {
            clickedWall = room.eastWall; // Right wall
            distanceFromStart = y; // Vertical distance from top to bottom
        }

        distanceFromStart = distanceFromStart / 25 * 25; // Round to nearest 25

        if (clickedWall != null) {
            openWallOptionsDialog(clickedWall, distanceFromStart);
        }

        // Check if any furniture is clicked
        System.out.println("Clicked x:" + x + "y:" + y);
        for (Furniture furniture : room.getContainedFurniture()) {
            if (furniture.containsPoint(x, y)) {
                handleFurnitureClick(furniture);
            return;
            }
        }
    }

    private void handleFurnitureClick(Furniture furniture) {

        // Show a dialog with options to remove or rotate the furniture
        String[] options = { "Remove Furniture", "Rotate Furniture" };
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select an option:",
            "Furniture Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);

        if (choice == 0) {
            room.removeContainedObject(furniture);
            roomController.repaintRooms();
        } else if (choice == 1) {
            furniture.rotate();
            roomController.repaintRooms();
        }
    }

    // private void openWallOptionsDialog(Wall wall, int distanceFromStart) {
    // // Show a dialog with options to add a door or window
    // String[] options = { "Add Door", "Add Window" };
    // int choice = JOptionPane.showOptionDialog(
    // this,
    // "Select an option:",
    // "Wall Options",
    // JOptionPane.DEFAULT_OPTION,
    // JOptionPane.PLAIN_MESSAGE,
    // null,
    // options,
    // options[0]);

    // if (choice == 0) {
    // addDoorDialog(wall, distanceFromStart);
    // } else if (choice == 1) {
    // addWindowDialog(wall, distanceFromStart);
    // }
    // }

    private void openWallOptionsDialog(Wall wall, int distanceFromStart) {
        Door existingDoor = wall.getDoorAt(distanceFromStart);
        Window existingWindow = wall.getWindowAt(distanceFromStart);

        if (existingDoor != null) {
            String[] options = { "Edit Door", "Remove Door" };
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Select an option:",
                    "Door Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                editDoorDialog(wall, existingDoor);
            } else if (choice == 1) {
                Wall other = findAdjacentWall(wall);

                System.out.println("Other wall: " + other);

                if (other != null) {
                    Door otherDoor = other.findAdjacentDoor(wall, existingDoor);

                    System.out.println("Other door: " + otherDoor);
                    if (otherDoor != null) {
                        other.removeDoor(otherDoor);

                        System.out.println("Left doors on other" + other.getDoors().size());
                    }
                }
                wall.removeDoor(existingDoor);
                System.out.println("Left doors on me" + wall.getDoors().size());

                // roomController.syncAdjacentRoomDoors(this.room);
                roomController.repaintRooms();
            }
        } else if (existingWindow != null) {
            String[] options = { "Edit Window", "Remove Window" };
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Select an option:",
                    "Window Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                editWindowDialog(wall, existingWindow);
            } else if (choice == 1) {
                wall.removeWindow(existingWindow);
                repaint();
            }
        } else {
            String[] options = { "Add Door", "Add Window" };
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Select an option:",
                    "Wall Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                addDoorDialog(wall, distanceFromStart);
            } else if (choice == 1) {
                addWindowDialog(wall, distanceFromStart);
            }
        }
    }

    private void editDoorDialog(Wall wall, Door door) {
        int maxLength = wall.size();
        Integer[] fittableLengths = new Integer[maxLength / 25];
        for (int i = 0; i < maxLength / 25; i++) {
            fittableLengths[i] = (i + 1) * 25;
        }

        Integer lengthStr = (Integer) JOptionPane.showInputDialog(
                this,
                "Select new door length:",
                "Edit Door Length",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fittableLengths,
                door.getLength());

        if (lengthStr == null) {
            System.err.println("User canceled the dialog");
            return; // User canceled the dialog
        }

        try {
            int length = lengthStr;
            door.setLength(length);
            roomController.syncAdjacentRoomDoors(this.room);
            repaint(); // Repaint to show the updated door
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for length.");
        }
    }

    private void editWindowDialog(Wall wall, Window window) {
        int maxLength = wall.size();
        Integer[] fittableLengths = new Integer[maxLength / 25];
        for (int i = 0; i < maxLength / 25; i++) {
            fittableLengths[i] = (i + 1) * 25;
        }

        Integer lengthStr = (Integer) JOptionPane.showInputDialog(
                this,
                "Select new window length:",
                "Edit Window Length",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fittableLengths,
                window.getLength());

        if (lengthStr == null) {
            System.err.println("User canceled the dialog");
            return; // User canceled the dialog
        }

        try {
            int length = lengthStr;
            window.setLength(length);
            roomController.syncAdjacentRoomDoors(this.room);
            repaint(); // Repaint to show the updated window
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for length.");
        }
    }

    private void addDoorDialog(Wall wall, int distanceFromStart) {
        if (room.getType() == RoomType.BATHROOM || room.getType() == RoomType.BEDROOM) {
            Wall adjacentWall = findAdjacentWall(wall);
            if (adjacentWall == null) {
                JOptionPane.showMessageDialog(this,
                        "Doors can only be added to walls adjacent to another room's wall for bathrooms and bedrooms.");
                return;
            }
        }
        int maxLength = wall.size();

        Integer[] fittableLengths = new Integer[maxLength / 25];
        for (int i = 0; i < maxLength / 25; i++) {
            fittableLengths[i] = (i + 1) * 25;
        }

        Integer lengthStr = (Integer) JOptionPane.showInputDialog(
                this,
                "Select door length:",
                "Door Length",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fittableLengths,
                fittableLengths[0]);

        if (lengthStr == null) {
            System.err.println("User canceled the dialog");
            return; // User canceled the dialog
        }

        try {
            int length = lengthStr;
            Door door = new Door(length, distanceFromStart);

            wall.addDoor(door);
            roomController.syncAdjacentRoomDoors(this.room);
            roomController.repaintRooms(); // Repaint to show the added door
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for length.");
        }
    }

    private void addWindowDialog(Wall wall, int distanceFromStart) {
        Wall adjacentWall = findAdjacentWall(wall);
        if (adjacentWall != null) {
            JOptionPane.showMessageDialog(this, "Windows cannot be added to adjacent walls.");
            return;
        }
        // Show input dialog for window length
        int maxLength = wall.size();

        Integer[] fittableLengths = new Integer[maxLength / 25];
        for (int i = 0; i < maxLength / 25; i++) {
            fittableLengths[i] = (i + 1) * 25;
        }

        Integer lengthStr = (Integer) JOptionPane.showInputDialog(
                this,
                "Select window length:",
                "Window Length",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fittableLengths,
                fittableLengths[0]);

        if (lengthStr == null) {
            System.err.println("User canceled the dialog");
            return; // User canceled the dialog
        }

        try {
            int length = lengthStr;
            Window window = new Window(length, distanceFromStart);

            wall.addWindow(window); // Add the window to the selected wall
            roomController.syncAdjacentRoomDoors(this.room);
            repaint(); // Repaint to show the added window
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for length.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw furniture in the room

        for (Furniture furniture : room.getContainedFurniture()) {
            // Use the draw method to render the furniture image
            furniture.draw(g, (int) furniture.getX(), (int) furniture.getY(), this);
        }

        // Draw walls
        g.setColor(Color.WHITE); // Wall color
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
        drawWallWithDoorsAndWindows(g, room.southWall, 0, height - wallThickness, width, wallThickness,
                hasBottomNeighbor, "horizontal");
        drawWallWithDoorsAndWindows(g, room.westWall, 0, 0, height, wallThickness, hasLeftNeighbor, "vertical");
        drawWallWithDoorsAndWindows(g, room.eastWall, width - wallThickness, 0, height, wallThickness, hasRightNeighbor,
                "vertical");




    }

    // Helper method to draw a wall with doors and windows
    private void drawWallWithDoorsAndWindows(Graphics g, Wall wall, int x, int y, int length, int thickness,
            boolean hasNeighbor, String orientation) {
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
                g.fillRect(x, y + currentPos, actualThickness, length - currentPos);
            }
        }

        // Draw dashed lines for windows
        g.setColor(Color.DARK_GRAY); // Set color for windows
        int dashLength = 5; // Length of each dash
        int spaceLength = 3; // Space between dashes

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

    private Wall findAdjacentWall(Wall wall) {
        FloorPlan floorPlan = roomController.getFloorPlan();
        for (FloorObject floorObject : floorPlan.getFloorObjects()) {
            if (floorObject instanceof Room otherRoom) {
                if (otherRoom.equals(room)) {
                    continue;
                }
                for (Wall otherWall : otherRoom.getWalls()) {
                    if (roomController.areWallsAdjacent(wall, otherWall)) {
                        return otherWall;
                    }
                }
            }
        }
        return null;
    }

    // Highlight the room
    public void setHighlight(boolean highlight) {
        this.isHighlighted = highlight;

        System.out.println("Highlighting room: " + highlight);
        if (highlight) {
            Color originalColor = room.getType().getColor();
            Color highlightColor = originalColor.brighter();
            setBackground(highlightColor);
            // setBorder(BorderFactory.createLineBorder(Color.RED, 3)); // Set a red border for highlight
        } else {
            setBackground(room.getType().getColor()); // Reset to the room's original color
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
        room.setX(X);
        room.setY(Y);
        checkAndRemoveAdjacentWindows();
        getParent().repaint();
    }

    private void checkAndRemoveAdjacentWindows() {
        FloorPlan floorPlan = roomController.getFloorPlan();
        for (FloorObject floorObject : floorPlan.getFloorObjects()) {
            if (floorObject instanceof Room otherRoom) {
                if (otherRoom.equals(room)) {
                    continue;
                }
                for (Wall otherWall : otherRoom.getWalls()) {
                    for (Wall wall : room.getWalls()) {
                        if (roomController.areWallsAdjacent(wall, otherWall)) {
                            removeWindowsFromAdjacentWalls(wall, otherWall);
                            removeWindowsFromAdjacentWalls(otherWall, wall);
                        }

                    }
                }
            }
        }
    }

    private void removeWindowsFromAdjacentWalls(Wall wall1, Wall wall2) {
        if (roomController.areWallsAdjacent(wall1, wall2)) {
            List<Window> windowsToRemove = new ArrayList<>(wall1.getWindows());
            for (Window window : windowsToRemove) {
                wall1.removeWindow(window);
            }
        }
    }

    // Check if this room label is overlapping with any other room labels
    @Override
    public boolean isOverlappingAny() {
        return roomController.isOverlappingAny(this);
    }


    public Room getRoom() {
        return room;
    }

}
