package hoodplanner.ui;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import hoodplanner.models.RoomType;

public class RightPanel extends JPanel {

    private RoomLabel selectedRoom;
    private final LeftPanel leftPanel;
    private final JLabel positionLabel;
    private final JLabel dimensionLabel;
    private final JComboBox<RoomType> roomTypeDropdown;
    private final JPanel addRoomPanel;
    private final JPanel addItemPanel;
    private final CardLayout cardLayout;
    private final JButton removeRoomButton;

    public RightPanel(LeftPanel leftPanel) {
        this.leftPanel = leftPanel;

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Panel for the 'Add Room' view
        addRoomPanel = new JPanel();
        addRoomPanel.setLayout(null);
        positionLabel = new JLabel("Position: ");
        positionLabel.setBounds(10, 10, 300, 25);
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setBounds(10, 40, 300, 25);

        addRoomPanel.add(positionLabel);
        addRoomPanel.add(dimensionLabel);

        // Dropdown for room type selection
        roomTypeDropdown = new JComboBox<>(RoomType.values());
        JLabel typeLabel = new JLabel("Select Room Type:");
        typeLabel.setBounds(10, 70, 300, 25);
        roomTypeDropdown.setBounds(10, 100, 200, 25);

        addRoomPanel.add(typeLabel);
        addRoomPanel.add(roomTypeDropdown);

        // Add listener for room type changes
        roomTypeDropdown.addActionListener(e -> {
            if (selectedRoom != null) {
                RoomType selectedType = (RoomType) roomTypeDropdown.getSelectedItem();
                selectedRoom.setColor(selectedType.getColor());
                this.leftPanel.repaint();
            }
        });

        // "Remove Room" button
        removeRoomButton = new JButton("Remove Room");
        removeRoomButton.setBounds(10, 140, 200, 25);
        addRoomPanel.add(removeRoomButton);

        // Add listener for "Remove Room" button
        removeRoomButton.addActionListener(e -> {
            if (selectedRoom != null) {
                leftPanel.remove(selectedRoom); // Remove room label from LeftPanel
                leftPanel.revalidate();
                leftPanel.repaint();
                selectedRoom = null; // Clear selection in RightPanel
                positionLabel.setText("Position: ");
                dimensionLabel.setText("Dimensions: ");
            }
        });

        // Panel for the 'Add Items' view
        addItemPanel = new JPanel();
        addItemPanel.setLayout(null);
        JLabel addItemLabel = new JLabel("Add Items Panel");
        addItemLabel.setBounds(10, 10, 300, 25);
        addItemPanel.add(addItemLabel);

        // Add both views to the CardLayout
        add(addRoomPanel, "AddRoom");
        add(addItemPanel, "AddItem");
    }

    // Method to switch to the 'Add Room' view
    public void showAddRoomView() {
        cardLayout.show(this, "AddRoom");
    }

    // Method to switch to the 'Add Items' view
    public void showAddItemView() {
        cardLayout.show(this, "AddItem");
    }

    public void setSelectedRoom(RoomLabel room) {
        selectedRoom = room;
        update(room);
    }

    // Method to update the 'Add Room' panel with a room's properties
    private void update(RoomLabel room) {
        if (room == null) {
            return;
        }
        int x = room.getX();
        int y = room.getY();
        int width = room.getWidth();
        int height = room.getHeight();

        // Update the labels with the room's position and size
        positionLabel.setText("Position: X = " + x + ", Y = " + y);
        dimensionLabel.setText("Dimensions: Width = " + width + ", Height = " + height);

        Color bgColor = room.getBackground();
        for (RoomType type : RoomType.values()) {
            if (bgColor.equals(type.getColor())) {
                roomTypeDropdown.setSelectedItem(type);
                break;
            }
        }
    }
}
