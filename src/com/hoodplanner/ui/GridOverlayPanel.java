package hoodplanner.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GridOverlayPanel extends JPanel {

    public GridOverlayPanel() {
        super(null);
        setOpaque(false); // Ensure the grid panel is transparent
        setBounds(0, 0, 1920, 1080); // Set to cover the full panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 255, 255, 77));
        int gridSize = 25;

        for (int x = 0; x < getWidth(); x += gridSize) {
            for (int y = 0; y < getHeight(); y += gridSize) {
                g2d.fillOval(x - 2, y - 2, 2, 2);
            }
        }
    }
    
}
