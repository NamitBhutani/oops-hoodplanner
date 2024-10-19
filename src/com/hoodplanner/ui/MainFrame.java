package hoodplanner.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class MainFrame extends JFrame {

    private final JLabel positionLabel;
    private final JLabel dimensionLabel;
    private JComboBox<String> colorDropdown;
    private RoomLabel selectedRoom; // To store the currently selected room

    public MainFrame() {

        TopMenuBar menuBar = new TopMenuBar();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);

        JPanel leftPanel = new JPanel(null);
        // Create a grid panel that will paint the grid over everything
        JPanel gridOverlay = new GridOverlayPanel();

        leftPanel.setBackground(Color.DARK_GRAY);
        // Add the gridOverlay on top of the leftPanel (grid will appear above rooms)
        leftPanel.add(gridOverlay);
        leftPanel.setBackground(Color.DARK_GRAY);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null); // Set layout to null for manual component positioning
        // rightPanel.setBackground(Color.LIGHT_GRAY);

        // Labels to show selected room's properties
        positionLabel = new JLabel("Position: ");
        positionLabel.setBounds(10, 10, 300, 25); // Set bounds for manual positioning
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setBounds(10, 40, 300, 25); // Set bounds for manual positioning

        // Add the labels to the right panel
        rightPanel.add(positionLabel);
        rightPanel.add(dimensionLabel);

        // Dropdown for color selection
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        colorDropdown = new JComboBox<>(colors);
        JLabel colorLabel = new JLabel("Select Room Color:");
        colorLabel.setBounds(10, 70, 300, 25); // Set bounds for manual positioning
        colorDropdown.setBounds(10, 100, 200, 25); // Set bounds for manual positioning

        rightPanel.add(colorLabel);
        rightPanel.add(colorDropdown);

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
            }
        });

        // Action to add new rooms
        menuBar.getItem("Add Room").addActionListener(e -> {
            RoomLabel room = new RoomLabel(100, 100);
            room.setBounds(50, 50, 100, 100);
            leftPanel.add(room);
            leftPanel.revalidate();
            leftPanel.repaint();

            // Add a mouse listener to each room to update the properties panel when clicked
            room.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedRoom = room;
                    System.out.println("Room clicked: " + selectedRoom.getX() + ", " + selectedRoom.getY());
                    updateRightPanel(room); // Update right panel with the selected room's properties
                }
            });

            room.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    updateRightPanel(selectedRoom);
                }
            });
        });

        setJMenuBar(menuBar);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        // splitPane.setEnabled(false);

        getContentPane().add(splitPane);

        // Load the image and set it as the title bar icon
        ImageIcon icon = new ImageIcon("src\\com\\hoodplanner\\public\\home.png");
        setIconImage(icon.getImage());
        setTitle("Hood Planner 2D");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to update the right panel with the room's properties
    private void updateRightPanel(RoomLabel room) {
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

