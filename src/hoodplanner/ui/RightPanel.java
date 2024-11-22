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

        // Add Object Panel
        addObjectPanel = new JPanel();
        addObjectPanel.setLayout(null);

        objectName = new JLabel("Details Panel");
        objectName.setBounds(10, 10, 300, 25);
        objectName.setFont(objectName.getFont().deriveFont(16.0f));

        positionLabel = new JLabel("Position: ");
        positionLabel.setBounds(10, 40, 300, 25);
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setBounds(10, 70, 300, 25);

        addObjectPanel.add(objectName);
        addObjectPanel.add(positionLabel);
        addObjectPanel.add(dimensionLabel);

        roomTypeDropdown = new JComboBox<>(RoomType.values());
        JLabel typeLabel = new JLabel("Select Room Type:");
        typeLabel.setBounds(10, 95, 300, 25);
        roomTypeDropdown.setBounds(10, 120, 200, 25);

        addObjectPanel.add(typeLabel);
        addObjectPanel.add(roomTypeDropdown);

        roomTypeDropdown.addActionListener(e -> {
            if (selectedObjectLabel != null && selectedObjectLabel.getObject() instanceof Room room) {
                RoomType selectedType = (RoomType) roomTypeDropdown.getSelectedItem();
                room.setType(selectedType);
                selectedObjectLabel.setColor(selectedType.getColor());
                this.leftPanel.repaint();
            }
        });

        removeObjectButton = new JButton("Remove Room");
        removeObjectButton.setBounds(10, 160, 200, 25);
        addObjectPanel.add(removeObjectButton);

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
        availableFurniture.add(new Furniture(5, 5, 50.0, 90.0, "Sofa", "src/hoodplanner/public/sofa(1).png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 90.0, "Bed", "src/hoodplanner/public/bed.png"));
        availableFurniture.add(new Furniture(5, 5, 50.0, 80.0, "Toilet", "src/hoodplanner/public/toilet.png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 80.0, "Sink", "src/hoodplanner/public/sink.png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 80.0, "Bath", "src/hoodplanner/public/bath.png"));
        availableFurniture.add(new Furniture(5, 5, 60.0, 80.0, "Couch", "src/hoodplanner/public/couch.png"));
    }

    private void refreshFurnitureDisplay() {
        furnitureSelectionPanel.removeAll();

        for (Furniture furniture : availableFurniture) {
            JPanel furnitureItemPanel = new JPanel(new BorderLayout());
            furnitureItemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

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
        selectedObjectLabel = objectLabel;
        update(objectLabel);
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
}
