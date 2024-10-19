package hoodplanner.ui;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RightPanel extends JPanel{

    private RoomLabel selectedRoom;
    LeftPanel leftPanel;
    JLabel positionLabel;
    JLabel dimensionLabel;
    JComboBox<String> colorDropdown;


    public RightPanel(LeftPanel leftPanel) {
        setLayout(null); // Set layout to null for manual component positioning
        // Labels to show selected room's properties
        this.leftPanel = leftPanel;
        positionLabel = new JLabel("Position: ");
        positionLabel.setBounds(10, 10, 300, 25); 
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setBounds(10, 40, 300, 25);

        add(positionLabel);
        add(dimensionLabel);

        // Dropdown for color selection
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        colorDropdown = new JComboBox<>(colors);
        JLabel colorLabel = new JLabel("Select Room Color:");
        colorLabel.setBounds(10, 70, 300, 25); // Set bounds for manual positioning
        colorDropdown.setBounds(10, 100, 200, 25); // Set bounds for manual positioning

        add(colorLabel);
        add(colorDropdown);

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

                leftPanel.repaint();
            }
        });
    }


    public void setSelectedRoom(RoomLabel room) {
        selectedRoom = room;
        update(room);
    }

    // Method to update the right panel with a room's properties
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
