package hoodplanner.ui;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RightPanel extends JPanel {

    private RoomLabel selectedRoom;
    private final LeftPanel leftPanel;
    private final JLabel positionLabel;
    private final JLabel dimensionLabel;
    private final JComboBox<String> colorDropdown;
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

        // Dropdown for color selection in 'Add Room' view
        String[] colors = { "Red", "Blue", "Green", "Yellow" };
        colorDropdown = new JComboBox<>(colors);
        JLabel colorLabel = new JLabel("Select Room Color:");
        colorLabel.setBounds(10, 70, 300, 25);
        colorDropdown.setBounds(10, 100, 200, 25);

        addRoomPanel.add(colorLabel);
        addRoomPanel.add(colorDropdown);

        // Add listener for color changes
        colorDropdown.addActionListener(e -> {
            if (selectedRoom != null) {
                String selectedColor = (String) colorDropdown.getSelectedItem();
                switch (selectedColor) {
                    case "Red" -> selectedRoom.setColor(new Color(155, 40, 40));
                    case "Blue" -> selectedRoom.setColor(new Color(40, 40, 155));
                    case "Green" -> selectedRoom.setColor(new Color(40, 155, 40));
                    case "Yellow" -> selectedRoom.setColor(new Color(130, 131, 40));
                }
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

        // Update the dropdown based on the current room's color
        Color bgColor = room.getBackground();
        if (bgColor.equals(new Color(155, 40, 40))) {
            colorDropdown.setSelectedItem("Red");
        } else if (bgColor.equals(new Color(40, 40, 155))) {
            colorDropdown.setSelectedItem("Blue");
        } else if (bgColor.equals(new Color(40, 155, 40))) {
            colorDropdown.setSelectedItem("Green");
        } else if (bgColor.equals(new Color(130, 131, 40))) {
            colorDropdown.setSelectedItem("Yellow");
        }
    }
}
