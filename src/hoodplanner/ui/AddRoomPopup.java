package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.Room;
import hoodplanner.models.RoomType;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;



public class AddRoomPopup {

    public static void showAddRoomDialog(RoomController roomController, LeftPanel leftPanel, RightPanel<Room, RoomLabel> rightPanel) {
        JDialog dialog = new JDialog((Frame) null, "Add Room", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout(10, 10));

        // Main panel for room details
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        // Name and type (compulsory fields)
        JTextField nameField = new JTextField();
        JComboBox<RoomType> typeField = new JComboBox<>(RoomType.values()); // example types
        
        mainPanel.add(new JLabel("Room Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Room Type:"));
        mainPanel.add(typeField);

        JTextField widthField = new JTextField("200");
        JTextField heightField = new JTextField("200");
        mainPanel.add(new JLabel("Width:"));
        mainPanel.add(widthField);
        mainPanel.add(new JLabel("Height:"));
        mainPanel.add(heightField);


        // More Details collapsible section
        JPanel moreDetailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        moreDetailsPanel.setVisible(false);

        // Relative positioning to another room
        List<Room> rooms = roomController.getRooms();
        Room[] roomArray = rooms.toArray(Room[]::new);
        JComboBox<Room> referenceRoomDropdown = new JComboBox<>(roomArray);

        JComboBox<String> positionDropdown = new JComboBox<>(new String[] {"North", "South", "East", "West"});
        JComboBox<String> alignmentDropdown = new JComboBox<>(new String[] {"Left", "Center", "Right"}); // Adjust for NSEW
        
        positionDropdown.addActionListener(e -> {
            if (positionDropdown.getSelectedItem().equals("North") || positionDropdown.getSelectedItem().equals("South")) {
                alignmentDropdown.removeAllItems();
                alignmentDropdown.addItem("Left");
                alignmentDropdown.addItem("Center");
                alignmentDropdown.addItem("Right");
            } else {
                alignmentDropdown.removeAllItems();
                alignmentDropdown.addItem("Top");
                alignmentDropdown.addItem("Middle");
                alignmentDropdown.addItem("Bottom");
            }
        });

        moreDetailsPanel.add(new JLabel("Reference Room:"));
        moreDetailsPanel.add(referenceRoomDropdown);
        moreDetailsPanel.add(new JLabel("Position:"));
        moreDetailsPanel.add(positionDropdown);
        moreDetailsPanel.add(new JLabel("Alignment:"));
        moreDetailsPanel.add(alignmentDropdown);

        // Button to toggle More Details panel
        JButton moreDetailsButton = new JButton("Relative Positioning");
        moreDetailsButton.addActionListener(e -> moreDetailsPanel.setVisible(!moreDetailsPanel.isVisible()));
        moreDetailsButton.setVisible(true);

        // Submit button
        JButton submitButton = new JButton("Add Room");
        submitButton.addActionListener((ActionEvent e) -> {
            String roomName = nameField.getText();
            RoomType roomType = (RoomType) typeField.getSelectedItem();
            double width = Double.parseDouble(widthField.getText());
            double height = Double.parseDouble(heightField.getText());
            
            Room referenceRoom = (Room) referenceRoomDropdown.getSelectedItem();
            String position = (String) positionDropdown.getSelectedItem();
            String alignment = (String) alignmentDropdown.getSelectedItem();
            
            // Call addRoom method with inputs
            try {
                if (moreDetailsPanel.isVisible()) {
                    roomController.addRoom(roomName, roomType, width, height, leftPanel, rightPanel, referenceRoom, position, alignment);
                } else {
                    roomController.addRoom(roomName, roomType, width, height, leftPanel, rightPanel);
                }
                leftPanel.revalidate();
                leftPanel.repaint();
                dialog.dispose();
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        // Assemble dialog layout
        dialog.add(mainPanel, BorderLayout.NORTH);
        dialog.add(moreDetailsButton, BorderLayout.WEST);
        dialog.add(moreDetailsPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
