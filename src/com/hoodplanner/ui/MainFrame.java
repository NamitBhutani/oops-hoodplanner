package hoodplanner.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class MainFrame extends JFrame {

    private RoomLabel selectedRoom; // To store the currently selected room

    public MainFrame() {

        TopMenuBar menuBar = new TopMenuBar();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);

        LeftPanel leftPanel = new LeftPanel();

        RightPanel rightPanel = new RightPanel(leftPanel);

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
                    rightPanel.setSelectedRoom(selectedRoom);
                }
            });

            room.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    rightPanel.setSelectedRoom(selectedRoom);
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
}

