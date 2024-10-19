package hoodplanner.ui;

import java.awt.Color;
import javax.swing.JPanel;

public class LeftPanel extends JPanel {
    public LeftPanel() {
        // Set layout to null for manual component positioning
        setLayout(null);

        // Add the gridOverlay on top of the leftPanel (grid will appear above rooms)
        JPanel gridOverlay = new GridOverlayPanel();
        add(gridOverlay);
        setBackground(Color.DARK_GRAY);
    }
}
