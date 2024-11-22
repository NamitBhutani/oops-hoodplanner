package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorObject;
import hoodplanner.models.Furniture;
import hoodplanner.models.Room;
import hoodplanner.models.RoomType;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import javax.swing.*;

public class RightPanel<T extends FloorObject, L extends ObjectLabel<T>> extends JPanel {

    private L selectedObjectLabel;
    private final LeftPanel leftPanel;

    private final JLabel objectName;
    private final JLabel positionLabel;
    private final JLabel dimensionLabel;
    private final JComboBox<RoomType> roomTypeDropdown;
    private final JPanel addObjectPanel;
    private final JPanel addItemPanel;
    private final CardLayout cardLayout;
    private final JButton removeObjectButton;
    private final RoomController roomController;

    private final ArrayList<Furniture> availableFurniture;
    private final JPanel furnitureSelectionPanel;

    public RightPanel(LeftPanel leftPanel, RoomController roomController) {
        this.leftPanel = leftPanel;
        this.roomController = roomController;
        this.availableFurniture = new ArrayList<>();
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        addObjectPanel = new JPanel();
        addObjectPanel.setLayout(new BoxLayout(addObjectPanel, BoxLayout.Y_AXIS));
        addObjectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        objectName = new JLabel("Details Panel");
        objectName.setFont(objectName.getFont().deriveFont(Font.BOLD, 16f));
        objectName.setAlignmentX(Component.LEFT_ALIGNMENT);
        addObjectPanel.add(objectName);
        addObjectPanel.add(Box.createVerticalStrut(20));
        
        // Position and Dimensions
        positionLabel = new JLabel("Position: ");
        positionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addObjectPanel.add(positionLabel);
        addObjectPanel.add(Box.createVerticalStrut(10));
        
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addObjectPanel.add(dimensionLabel);
        addObjectPanel.add(Box.createVerticalStrut(20));
        
        // Room type selection
        JLabel typeLabel = new JLabel("Select Room Type:");
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addObjectPanel.add(typeLabel);
        addObjectPanel.add(Box.createVerticalStrut(5));
        
        roomTypeDropdown = new JComboBox<>(RoomType.values());
        roomTypeDropdown.setMaximumSize(new Dimension(200, 25));
        roomTypeDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        addObjectPanel.add(roomTypeDropdown);
        addObjectPanel.add(Box.createVerticalStrut(20));
        
        // Remove button
        removeObjectButton = new JButton("Remove Room");
        removeObjectButton.setMaximumSize(new Dimension(200, 30));
        removeObjectButton.setBackground(new Color(220, 53, 69));
        removeObjectButton.setForeground(Color.WHITE);
        removeObjectButton.setFocusPainted(false);
        removeObjectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addObjectPanel.add(removeObjectButton);
        
        // Your existing action listeners can remain the same
        roomTypeDropdown.addActionListener(e -> {
            if (selectedObjectLabel != null && selectedObjectLabel.getObject() instanceof Room room) {
                RoomType selectedType = (RoomType) roomTypeDropdown.getSelectedItem();
                room.setType(selectedType);
                selectedObjectLabel.setColor(selectedType.getColor());
                leftPanel.repaint();
            }
        });
        
        removeObjectButton.addActionListener(e -> {
            if (selectedObjectLabel != null && selectedObjectLabel.getObject() instanceof Room) {
                Room room = (Room) selectedObjectLabel.getObject();
                roomController.deleteRoom(room, leftPanel);
                selectedObjectLabel = null;
                positionLabel.setText("Position: ");
                dimensionLabel.setText("Dimensions: ");
            }
        });

        // Add Items Panel
        addItemPanel = new JPanel();
        addItemPanel.setLayout(new BorderLayout());

        JLabel addItemLabel = new JLabel("Available Furniture");
        addItemLabel.setFont(objectName.getFont().deriveFont(Font.BOLD, 16f));
        addItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        addItemPanel.add(addItemLabel, BorderLayout.NORTH);

        furnitureSelectionPanel = new JPanel();
        furnitureSelectionPanel.setLayout(new GridLayout(0, 2, 10, 10));
        JScrollPane scrollPane = new JScrollPane(furnitureSelectionPanel);
        addItemPanel.add(scrollPane, BorderLayout.CENTER);

        add(addObjectPanel, "AddObject");
        add(addItemPanel, "AddItem");

        // Load available furniture and update the panel
        loadAvailableFurniture();
        refreshFurnitureDisplay();
    }

    public final void loadAvailableFurniture() {
        availableFurniture.add(new Furniture(100, 100, 50.0, 90.0, "Sofa", "src/hoodplanner/public/sofa(1).png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 90.0, "Bed", "src/hoodplanner/public/bed.png"));
        availableFurniture.add(new Furniture(100, 100, 50.0, 80.0, "Toilet", "src/hoodplanner/public/toilet.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Sink", "src/hoodplanner/public/sink.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Bath", "src/hoodplanner/public/bath.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Couch", "src/hoodplanner/public/couch.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Table", "src/hoodplanner/public/table.png"));
        availableFurniture.add(new Furniture(100, 100, 60.0, 80.0, "Armchair", "src/hoodplanner/public/armchair.png"));
                
    }

    private void refreshFurnitureDisplay() {
        furnitureSelectionPanel.removeAll();

        for (Furniture furniture : availableFurniture) {
            JPanel furnitureItemPanel = new JPanel(new BorderLayout());
            // furnitureItemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            // Load furniture image
            JLabel imageLabel = new JLabel();
            try {
                ImageIcon furnitureIcon = new ImageIcon(furniture.getImagePath());
                Image scaledImage = furnitureIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                imageLabel.setText("No Image");
            }
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            furnitureItemPanel.add(imageLabel, BorderLayout.CENTER);

            // Furniture name
            JLabel furnitureName = new JLabel(furniture.getName());
            furnitureName.setHorizontalAlignment(SwingConstants.CENTER);
            furnitureName.setVerticalAlignment(SwingConstants.CENTER);
            furnitureItemPanel.add(furnitureName, BorderLayout.SOUTH);

            // Enable drag for furniture
            furnitureItemPanel.setTransferHandler(new TransferHandler("furniture") {
                @Override
                public int getSourceActions(JComponent c) {
                    return COPY;
                }

                @Override
                protected Transferable createTransferable(JComponent c) {
                    return new StringSelection(furniture.getName());
                }
            });

            furnitureItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    JComponent comp = (JComponent) evt.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    handler.exportAsDrag(comp, evt, TransferHandler.COPY);
                }
            });

            furnitureSelectionPanel.add(furnitureItemPanel);
        }

        furnitureSelectionPanel.revalidate();
        furnitureSelectionPanel.repaint();
    }

    public void addFurnitureToRoom(Furniture furniture) {
        if (selectedObjectLabel != null && selectedObjectLabel.getObject() instanceof Room room) {
            room.addContainedObject(furniture);
            JOptionPane.showMessageDialog(this, furniture.getName() + " added to " + room.getName());
            roomController.repaintRooms();
            leftPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room before adding furniture.", "No Room Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void showAddObjectView() {
        cardLayout.show(this, "AddObject");
    }

    public void showAddItemView() {
        cardLayout.show(this, "AddItem");
    }

    public void setSelectedObjectLabel(L objectLabel) {

        if (objectLabel == null) {
            if (selectedObjectLabel instanceof RoomLabel roomLabel) {
                roomLabel.setHighlight(false);
            }
            selectedObjectLabel = null;
            positionLabel.setText("Position: ");
            dimensionLabel.setText("Dimensions: ");
            showAddObjectView();
            return;
        }
        if (selectedObjectLabel instanceof RoomLabel roomLabel) {
            roomLabel.setHighlight(false);
        }
        selectedObjectLabel = objectLabel;
        if (objectLabel instanceof RoomLabel roomLabel) {
            roomLabel.setHighlight(true);
        }
        update(objectLabel);
        showAddObjectView();
    }

    private void update(L objectLabel) {
        if (objectLabel == null) {
            return;
        }
        T object = objectLabel.getObject();
        double x = object.getX();
        double y = object.getY();
        double width = object.getWidth();
        double length = object.getLength();

        // Update the labels with the object's position and size
        positionLabel.setText("Position: X = " + x + ", Y = " + y);
        dimensionLabel.setText("Dimensions: Width = " + width + ", Length = " + length);

        if (object instanceof Room room) {
            objectName.setText("Details Panel: " + room.getName());
            Color bgColor = objectLabel.getBackground();
            for (RoomType type : RoomType.values()) {
                if (bgColor.equals(type.getColor())) {
                    roomTypeDropdown.setSelectedItem(type);
                    break;
                }
            }
        } else {
            roomTypeDropdown.setEnabled(false);
        }
    }

    public L getSelectedObjectLabel() {
        return selectedObjectLabel;
    }
}
