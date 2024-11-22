package hoodplanner.ui;

import hoodplanner.models.Furniture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class FurnitureLabel extends ObjectLabel<Furniture> {
    private final ImageIcon icon;

    public FurnitureLabel(Furniture furniture) {
        super(furniture);
        icon = new ImageIcon(furniture.getImagePath());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback if image fails to load
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString(getObject().getName(), 5, getHeight() / 2);
        }
    }

    @Override
    public void move(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public boolean isOverlappingAny() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOverlappingAny'");
    }
}
